package com.lads.truecaller.activities

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.AudioManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract
import android.telecom.Call
import android.telecom.CallAudioState
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageButton
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.lads.truecaller.*
import com.lads.truecaller.OngoingCall.call
import com.lads.truecaller.databinding.ActivityCallingActivtiyBinding
import com.lads.truecaller.databinding.BottomSheetDialogBinding
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
    private lateinit var binding: ActivityCallingActivtiyBinding

    private val disposables = CompositeDisposable()
    private var number: String? = null
    private lateinit var context: Context
    private var myStringBuilder = StringBuilder()
    private var dialog: AlertDialog? = null
    private var timerStarted = false
    private var isMute = false
    private var isHold = false
    private var isSpeakerOn = false
    private lateinit var serviceIntent: Intent
    private var time = 0.0
    private var btnDial: ImageButton? = null
    lateinit var notificationChannel: NotificationChannel
    lateinit var notificationManager: NotificationManager
    lateinit var builder: Notification.Builder
    private val channelId = "12345"
    private val description = "Test Notification"
    private var audioManager: AudioManager? = null
    lateinit var bottomSheet: BottomSheetDialog
    private lateinit var pendingIntent2: PendingIntent

//    private val openAppIntent = CallingActivtiy.getStartIntent(this)
//    private val openAppPendingIntent =
//        PendingIntent.getActivity(context, 0, openAppIntent, FLAG_IMMUTABLE)


    //    val BASE_URL = "https://jsonplaceholder.typicode.com/"
    val BASE_URL = "https://jsonplaceholder.typicode.com/"

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCallingActivtiyBinding.inflate(layoutInflater)

        setContentView(binding.root)
//        number = findViewById<TextView>(R.id.tv_callingNumber).toString()
        number = intent.data?.schemeSpecificPart
        tv_Timer.isVisible = false

        showNotification()
//         val audioManager: AudioManager =
//            getSystemService(Context.AUDIO_SERVICE) as AudioManager
//        audioManager.setMode(AudioManager.MODE_IN_CALL)

        serviceIntent = Intent(applicationContext, TimerService::class.java)
        registerReceiver(updateTime, IntentFilter(TimerService.TIMER_UPDATED))
//        getMyData()
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
//                 myStringBuilder = StringBuilder()
                val responseBody = response.body()
                if (responseBody != null) {
                    for (myData in responseBody) {
                        myStringBuilder!!.append(+myData.id)
//                        myStringBuilder.append("userId"+myData.userId)
                        Log.d(TAG, "onResponse: data$myStringBuilder \n")
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
            btnReceive_Call.isVisible = false
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
            //hold/unHold call
            holdUnHold()
        }
        ImgMute.setOnClickListener {
            mutePhone()
        }
        ImgAudio.setOnClickListener {
//            onSpeaker()
            toggleSpeaker()
        }
        ImgAddCall.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL)
            startActivity(intent)
        }
        ImgKeypad.setOnClickListener {

            bottomSheet = BottomSheetDialog(this)
            val inflater = LayoutInflater.from(applicationContext)
            val btn_binding = BottomSheetDialogBinding.inflate(inflater)
            bottomSheet?.setContentView(btn_binding.root)
            bottomSheet?.setCancelable(true)


            fun updateText(newValue: String) {
                btn_binding.tvCallingNumber.text = "" + btn_binding.tvCallingNumber.text + newValue
                btn_binding.btnClear.isVisible = true
            }

            fun valueLimit() {
                if (btn_binding.tvCallingNumber.length() == 13) {
                    Toast.makeText(context, "Max length reached", Toast.LENGTH_SHORT).show()
                }
            }

            fun newText() {
                if (btn_binding.tvCallingNumber.length() > 0) {
                    val string1 = btn_binding.tvCallingNumber.text.toString()
                    val string = btn_binding.tvCallingNumber.text.substring(0, (string1.length - 1))
                    btn_binding.tvCallingNumber.setText(string)
//            updateText(string)
//                    btn_binding.btnClear.isVisible = true
                }
            }
            btn_binding.tv1.setOnClickListener(View.OnClickListener {
                valueLimit()
                updateText(btn_binding.tv1.tag.toString())
            })
            btn_binding.tv2.setOnClickListener(View.OnClickListener {
                valueLimit()
                updateText(btn_binding.tv2.tag.toString())
            })
            btn_binding.tv3.setOnClickListener(View.OnClickListener {
                valueLimit()
                updateText(btn_binding.tv3.tag.toString())
            })
            btn_binding.tv4.setOnClickListener(View.OnClickListener {
                valueLimit()
                updateText(btn_binding.tv4.tag.toString())
            })
            btn_binding.tv5.setOnClickListener(View.OnClickListener {
                valueLimit()
                updateText(btn_binding.tv5.tag.toString())
            })
            btn_binding.tv6.setOnClickListener(View.OnClickListener {
                valueLimit()
                updateText(btn_binding.tv6.tag.toString())
            })
            btn_binding.tv7.setOnClickListener(View.OnClickListener {
                valueLimit()
                updateText(btn_binding.tv7.tag.toString())
            })
            btn_binding.tv8.setOnClickListener(View.OnClickListener {
                valueLimit()
                updateText(btn_binding.tv8.tag.toString())
            })
            btn_binding.tv9.setOnClickListener(View.OnClickListener {
                valueLimit()
                updateText(btn_binding.tv9.tag.toString())
            })
            btn_binding.tv0.setOnClickListener(View.OnClickListener {
                valueLimit()
                updateText(btn_binding.tv0.tag.toString())
            })
            btn_binding.tvStar.setOnClickListener(View.OnClickListener {
                valueLimit()
                updateText(btn_binding.tvStar.tag.toString())
            })
            btn_binding.tvHash.setOnClickListener(View.OnClickListener {
                valueLimit()
                updateText(btn_binding.tvHash.tag.toString())
            })
            btn_binding.btnClear.setOnClickListener {
                newText()
                if (btn_binding.tvCallingNumber.length() == 0) {
                    btn_binding.btnClear.isVisible = false
                }
            }
            btn_binding.btnDial.setOnClickListener {
                val dailIntent = Intent(Intent.ACTION_CALL)
                dailIntent.data = Uri.parse("tel:" + btn_binding.tvCallingNumber.text.toString())
//              activity?.startActivity(dailIntent)
                startActivity(dailIntent)
            }
            bottomSheet.show()
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

    private fun holdUnHold() {
        if (!isHold) {
//            call!!.hold()
            OngoingCall.hold()
            Toast.makeText(this, "Hold", Toast.LENGTH_SHORT).show()
            isHold = true
        } else {
            OngoingCall.unHold()
            Toast.makeText(this, "unHold", Toast.LENGTH_SHORT).show()
            isHold = false
        }
    }

    @SuppressLint("SetTextI18n")
    private fun updateUi(state: Int) {
//        tv_calling.text = "${state}\n${callStatus.toInt()}"
        tv_calling.text = state.asString().lowercase(Locale.getDefault())
            .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }

        if (state == Call.STATE_ACTIVE) {
            tv_Timer.isVisible = true
            startTimer()
//            startStopTimer()

            time = intent.getDoubleExtra(TimerService.TIME_EXTRA, 0.0)
            tv_Timer.text = getTimeStringFromDouble(time)
//            for (i in 1..time.toInt()) {
//                Log.i(TAG, "updateUi: $i")
//            }
        } else if (state == Call.STATE_DISCONNECTED) {
            stopTimer()
//            startStopTimer()
        }
//        else if (state == Call.STATE_RINGING) {
//            tv_callingNumber.text = "Alerting"
//        }
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
                .setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                .setData(call.details.handle)
                .let(
                    context::startActivity
                )
        }

        fun getStartIntent(context: Context): Intent {
            val openAppIntent = Intent(context, CallingActivtiy::class.java)
            openAppIntent.flags =
                Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT or Intent.FLAG_ACTIVITY_CLEAR_TASK
            openAppIntent.setData(call?.details?.handle)
            openAppIntent.let {
                context::class.java
            }
            return openAppIntent
        }

    }

    private fun startStopTimer() {
        if (timerStarted)
            stopTimer()
        else
            startTimer()
    }

    private fun startTimer() {
        serviceIntent.putExtra(TimerService.TIME_EXTRA, time)
        startService(serviceIntent)
        timerStarted = true
//        Toast.makeText(this, "Timer Started", Toast.LENGTH_SHORT).show()
    }

    private fun stopTimer() {
        stopService(serviceIntent)
        timerStarted = false
//        Toast.makeText(this, "Timer Stopped", Toast.LENGTH_SHORT).show()
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
        String.format("%02d:%02d", min, sec)

    private fun mutePhone() {
        val audioManager = getSystemService(AUDIO_SERVICE) as AudioManager
        if (!isMute) {
            audioManager.isStreamMute(AudioManager.MODE_CALL_SCREENING)
//            audioManager.adjustVolume(AudioManager.ADJUST_MUTE, AudioManager.FLAG_PLAY_SOUND)
            Toast.makeText(this, "Mic Muted", Toast.LENGTH_SHORT).show()
            isMute = true
        } else {
            audioManager.adjustVolume(AudioManager.ADJUST_UNMUTE, AudioManager.FLAG_SHOW_UI)
            Toast.makeText(this, "Mic UnMuted", Toast.LENGTH_SHORT).show()
            isMute = false
        }
    }

    @SuppressLint("ResourceType", "UseCompatLoadingForDrawables")
    private fun toggleSpeaker() {
        isSpeakerOn = !isSpeakerOn
        val drawable =
            if (isSpeakerOn)
                R.drawable.ic_mic_on else R.drawable.ic_mic_off
        ImgAudio.setImageDrawable(getDrawable(drawable))
        audioManager?.isSpeakerphoneOn = isSpeakerOn
        val newRoute =
            if (isSpeakerOn) CallAudioState.ROUTE_SPEAKER else CallAudioState.ROUTE_EARPIECE
        OngoingCall.inCallService?.setAudioRoute(newRoute)
//      textSpeaker.text = getString(if (isSpeakerOn) R.string.speaker_on else R.string.speaker_off)
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
//                    return cursor.getString(cursor.getColumnIndex(ContactsContract.PhoneLookup.PHONETIC_NAME))
//                    return cursor.getStringValue(ContactsContract.PhoneLookup.DISPLAY_NAME)
                }
            }
        } catch (e: Exception) {
        }
        return number
    }

    override fun onDestroy() {
//        showNotification()
        super.onDestroy()
    }

    override fun onResume() {
//        showNotification()
        super.onResume()

    }

//    private fun createNotifChannel() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            val channel = NotificationChannel(
//                CHANNEL_ID,
//                CHANNEL_NAME,
//                NotificationManager.IMPORTANCE_DEFAULT
//            ).apply {
//                lightColor = Color.BLUE
//                enableLights(true)
//            }
//            val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
//            manager.createNotificationChannel(channel)
//        }
//    }

    private fun showNotification() {
        if (intent.action === "END_CALL") {
            OngoingCall.hangup()
        }

        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as
                NotificationManager
        val intent = Intent(this, CallingActivtiy::class.java)
        val intent2 = Intent(this, ServiceReceiver::class.java)
        intent2.putExtra("action", "actionName");


        val servicePendingIntent = PendingIntent.getService(
            this,
            0, intent, PendingIntent.FLAG_IMMUTABLE
        )
        intent.flags = Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT
        intent.action = Intent.ACTION_MAIN
        intent.addCategory(Intent.CATEGORY_LAUNCHER)
//        intent.action = "END_CALL"
        if (intent.action == "END_CALL") {
            OngoingCall.hangup()
            Toast.makeText(this, "Call Ended", Toast.LENGTH_SHORT).show()
        }
        val pendingIntent =
            PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)


        pendingIntent2 =
            PendingIntent.getBroadcast(this, 0, intent2, PendingIntent.FLAG_UPDATE_CURRENT)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel =
                NotificationChannel(
                    channelId,
                    description,
                    NotificationManager.IMPORTANCE_HIGH
                )
            notificationChannel.lightColor = Color.BLUE
            notificationChannel.enableVibration(true)
            notificationManager.createNotificationChannel(notificationChannel)
            builder = Notification.Builder(this, channelId).setContentTitle(
                "TrueCaller"
            )
                .setContentText(number)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .addAction(android.R.drawable.ic_media_pause, "END_CALL", pendingIntent2)
                .setLargeIcon(
                    BitmapFactory.decodeResource(
                        this.resources, R.drawable
                            .ic_launcher_background
                    )
                )
//                .setContentIntent(openAppPendingIntent)
                .setContentIntent(pendingIntent)
                .setOngoing(true)
        }
        notificationManager.notify(12345, builder.build())
    }


}


