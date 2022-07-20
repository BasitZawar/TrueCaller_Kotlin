package com.lads.truecaller.activities

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.telecom.Call
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.lads.truecaller.OngoingCall
import com.lads.truecaller.R
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import java.util.concurrent.TimeUnit


class CallingActivtiy : AppCompatActivity() {
    private val disposables = CompositeDisposable()
    private lateinit var number: String
    private lateinit var hangup: ImageButton
    private lateinit var tv_Calling: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calling_activtiy)
        number = intent.data!!.schemeSpecificPart
        hangup = findViewById(R.id.btnEndCall)
        tv_Calling = findViewById(R.id.tv_callingNumber)

//        hangup.setOnClickListener(View.OnClickListener {
//            CallManager.cancelCall()
//
//        })
    }

    override fun onStart() {
        super.onStart()

//        answer.setOnClickListener {
//            OngoingCall.answer()
//        }

        hangup.setOnClickListener {
            OngoingCall.hangup()
        }

        OngoingCall.state
            .subscribe(::updateUi)
            .addTo(disposables)

        OngoingCall.state
            .filter { it == Call.STATE_DISCONNECTED }
            .delay(1, TimeUnit.SECONDS)
            .firstElement()
            .subscribe { finish() }
            .addTo(disposables)
    }

    @SuppressLint("SetTextI18n")
    private fun updateUi(state: Int) {
        tv_Calling.text = "${state}\n$number"

//        answer.isVisible = state == Call.STATE_RINGING
        hangup.isVisible = state in listOf(
            Call.STATE_DIALING,
            Call.STATE_RINGING,
            Call.STATE_ACTIVE
        )
    }

    override fun onStop() {
        super.onStop()
        disposables.clear()
    }

    companion object {
        fun start(context: Context, call: Call) {
            Intent(context, CallingActivtiy::class.java)
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                .setData(call.details.handle)
                .let(context::startActivity)
        }
    }
}