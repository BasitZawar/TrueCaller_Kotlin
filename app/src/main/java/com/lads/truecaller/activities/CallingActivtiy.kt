package com.lads.truecaller.activities

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.telecom.Call
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.lads.truecaller.OngoingCall
import com.lads.truecaller.R
import com.lads.truecaller.asString
import com.lads.truecaller.interfaces.ApiInterface
import com.lads.truecaller.model.ApiDataItem
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import kotlinx.android.synthetic.main.activity_calling_activtiy.*
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


class CallingActivtiy : AppCompatActivity() {
    private val disposables = CompositeDisposable()
    private lateinit var number: String

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calling_activtiy)
        number = intent.data!!.schemeSpecificPart
        tv_Timer.isVisible = false


        getMyData()
    }

    private fun getMyData() {
        val retrofitBuilder = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://api.github.com/")
            .build()
            .create(ApiInterface::class.java)

        val retrofitData = retrofitBuilder.getData()
        retrofitData.enqueue(object : Callback<List<ApiDataItem>> {
            override fun onResponse(
                call: retrofit2.Call<List<ApiDataItem>>,
                response: Response<List<ApiDataItem>>
            ) {
                try {
                    val myStringBuilder = StringBuilder()
                    val responseBody = response.body()!!
                    for (myData in responseBody) {
//                    myStringBuilder.append(myData.id)
                        myStringBuilder.append("title" + myData.title)
//                    myStringBuilder.append("\n")
                        Log.i(TAG, "onResponse: Just for checking")
                        tv_calling.text = myStringBuilder
                    }
                } catch (e: Exception) {
                    Log.i(TAG, "onResponse: ${e.message}")
                }
            }

            override fun onFailure(call: retrofit2.Call<List<ApiDataItem>>, t: Throwable) {
                Log.i("CallingActivity", "onFailure:${t.message}")
            }
        })
    }

    override fun onStart() {
        super.onStart()

        btnReceive_Call.setOnClickListener {
            OngoingCall.answer()
        }

        btnEndCall.setOnClickListener {
            OngoingCall.hangup()

            btnReceive_Call.isVisible = false
        }
        ImgHold.setOnClickListener {
            OngoingCall.onHold()
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
//        tv_calling.text = "${state}\n${callStatus.toInt()}"
        tv_calling.text = "${state.asString().toLowerCase().capitalize()}"

        if (Call.STATE_ACTIVE == 4) {
            tv_Timer.isVisible = true
            tv_Timer.text =
                "counting"
        }
        tv_callingNumber.text = "${number}"
//      btnReceive_Call.isVisible = state == Call.STATE_RINGING
        btnEndCall.isVisible = state in listOf(
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

