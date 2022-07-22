package com.lads.truecaller.activities

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.AudioManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract
import android.telecom.Call
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.lads.truecaller.OngoingCall
import com.lads.truecaller.R
import com.lads.truecaller.TimerService
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
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.math.roundToInt


class CallingActivtiy : AppCompatActivity() {
    private val disposables = CompositeDisposable()
    private lateinit var number: String
    private lateinit var context: Context

    //    val BASE_URL = "https://jsonplaceholder.typicode.com/"
    val BASE_URL = "https://jsonplaceholder.typicode.com/"


    private var timerStarted = false
    private lateinit var serviceIntent: Intent
    private var time = 0.0


    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calling_activtiy)
        number = intent.data!!.schemeSpecificPart
        tv_Timer.isVisible = false

        serviceIntent = Intent(applicationContext, TimerService::class.java)
        registerReceiver(updateTime, IntentFilter(TimerService.TIMER_UPDATED))
        getMyData()

    }

    private fun getMyData() {
        val retrofitBuilder = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(ApiInterface::class.java)

        val retrofitData = retrofitBuilder.getData()
        retrofitData.enqueue(object : Callback<List<ApiDataItem>> {
            override fun onResponse(
                call: retrofit2.Call<List<ApiDataItem>>,
                response: Response<List<ApiDataItem>>
            ) {
//                try {
                val myStringBuilder = StringBuilder()
                val responseBody = response.body()
                if (responseBody != null) {
                    for (myData in responseBody) {
                        //myStringBuilder.append(myData.id)
                        myStringBuilder.append(+myData.id)
                        //myStringBuilder.append("\n")
                        Log.i(TAG, "onResponse try: Just for checking")
                        tv_calling.text = myStringBuilder
                    }
                }
//                } catch (e: Exception) {
//                    Log.i(TAG, "onResponse catch: ${e.message}")
//                }
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
            startTimer()
            time = intent.getDoubleExtra(TimerService.TIME_EXTRA, 0.0)
            tv_Timer.text = getTimeStringFromDouble(time)
        }

        btnEndCall.setOnClickListener {
            OngoingCall.hangup()
            stopTimer()
            btnReceive_Call.isVisible = false
            btnEndCall.isVisible = false
        }
        ImgHold.setOnClickListener {
//            OngoingCall.onHold()
//            Toast.makeText(this, "call is on hold", Toast.LENGTH_SHORT).show()
        }
        ImgMute.setOnClickListener {
            mutePhone()
        }
        ImgAudio.setOnClickListener {
            onSpeaker()
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
        tv_calling.text = state.asString().lowercase(Locale.getDefault())
            .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }

        if (state == Call.STATE_ACTIVE) {
            startTimer()
            tv_Timer.isVisible = true
            time = intent.getDoubleExtra(TimerService.TIME_EXTRA, 0.0)
            tv_Timer.text = getTimeStringFromDouble(time)

        }
        tv_callingNumber.text = "$number"
//      btnReceive_Call.isVisible = state == Call.STATE_RINGING
//        btnEndCall.isVisible = state in listOf(
//            Call.STATE_DIALING,
//            Call.STATE_RINGING,
//            Call.STATE_ACTIVE
//        )
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

    private fun startStopTimer() {
        if (timerStarted)
            stopTimer()
        else
            startTimer()
    }

    fun startTimer() {
        serviceIntent.putExtra(TimerService.TIME_EXTRA, time)
        startService(serviceIntent)
        timerStarted = true
    }

    fun stopTimer() {
        stopService(serviceIntent)
        timerStarted = false
    }

    private val updateTime: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            time = intent.getDoubleExtra(TimerService.TIME_EXTRA, 0.0)
            tv_Timer.text = getTimeStringFromDouble(time)
        }
    }

    private fun getTimeStringFromDouble(time: Double): String {
        val resultInt = time.roundToInt()
        val hours = resultInt % 86400 / 3600
        val minutes = resultInt % 86400 % 3600 / 60
        val seconds = resultInt % 86400 % 3600 % 60

        return makeTimeString(hours, minutes, seconds)
    }

    private fun makeTimeString(hour: Int, min: Int, sec: Int): String =
        String.format("%02d:%02d:%02d", hour, min, sec)

    private fun mutePhone() {
        // permission needed
        //
        val audiomanager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        audiomanager.ringerMode = AudioManager.RINGER_MODE_SILENT
        Toast.makeText(this, "call mute", Toast.LENGTH_SHORT).show()
    }

    private fun onSpeaker() {
        val name = getNameFromPhoneNumber(tv_callingNumber.text.toString())
        Log.i(TAG, "onSpeaker name is :$name")
        // permission needed
        //
        val audiomanager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        audiomanager.ringerMode = AudioManager.ADJUST_RAISE
        Toast.makeText(this, "call mute", Toast.LENGTH_SHORT).show()
    }

    @SuppressLint("Range")
    fun getNameFromPhoneNumber(number: String): String {

        val uri = Uri.withAppendedPath(
            ContactsContract.PhoneLookup.CONTENT_FILTER_URI,
            Uri.encode(number)
        )
        val projection = arrayOf(
            ContactsContract.PhoneLookup.DISPLAY_NAME
        )
        try {
            val cursor = context.contentResolver.query(uri, projection, null, null, null)
            cursor.use {
                if (cursor?.moveToFirst() == true) {
                    return cursor.getString(cursor.getColumnIndex(ContactsContract.PhoneLookup.PHONETIC_NAME))
//                    return cursor.getStringValue(ContactsContract.PhoneLookup.DISPLAY_NAME)
                }
            }
        } catch (ignored: Exception) {
        }
        return number
    }


}

