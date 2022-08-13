package com.lads.truecaller

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.net.Uri
import android.provider.ContactsContract.PhoneLookup
import android.telephony.TelephonyManager
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


class ServiceReceiver : BroadcastReceiver() {
    private var tv_calling: TextView? = null

    override fun onReceive(context: Context?, intent: Intent?) {

//        when (intent?.action) {
//            ACCEPT_CALL -> {
//                context?.startActivity(CallingActivtiy.getStartIntent(context))
//                OngoingCall.answer()
//            }
//            DECLINE_CALL -> {
//                OngoingCall.hangup()
//            }
//        }
        val action = intent!!.getStringExtra("action")
        Log.i(TAG, "onReceive: ${intent.getStringExtra("action")}")
        if (action.equals("actionName")) {
            OngoingCall.hangup()
            Toast.makeText(context, "call ended", Toast.LENGTH_SHORT).show()
        }
        tv_calling?.findViewById<TextView>(R.id.tv_calling)
        val audioManager =
            context?.getSystemService(AppCompatActivity.AUDIO_SERVICE) as AudioManager


        if (intent!!.getStringExtra(TelephonyManager.EXTRA_STATE) == TelephonyManager.EXTRA_STATE_OFFHOOK) {
            try {
//                getMyData()
            } catch (e: Exception) {
                Log.d(TAG, "onReceive: catch${e.message}")
            }
        } else if (intent.getStringExtra(TelephonyManager.EXTRA_STATE) == TelephonyManager.EXTRA_STATE_OFFHOOK) {
            showToast(context, "call is started....")
        } else if (intent.getStringExtra(TelephonyManager.EXTRA_STATE) == TelephonyManager.EXTRA_STATE_IDLE) {
OngoingCall.hangup()
            showToast(context, "Phone call is Ended....")
        } else if (intent.getStringExtra(TelephonyManager.EXTRA_STATE) == TelephonyManager.EXTRA_STATE_RINGING) {
            showToast(context, "Ringing....")
            val number: String? = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER)
            val name: String? = intent.getStringExtra(TelephonyManager.EXTRA_CARRIER_NAME)
            if (context != null) {
                getContactName(context.applicationContext, number)
            }
            Log.e(TAG, "incoming number : $number Incoming Name:$name")
        }
    }

    private fun showToast(context: Context?, msg: String) {
        val toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT)
        toast.show()
    }

    @SuppressLint("Range", "LogNotTimber")
    private fun getContactName(context: Context, phoneNumber: String?): String? {
        val cr = context.contentResolver
        val uri: Uri = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumber))
        val cursor =
            cr.query(uri, arrayOf(PhoneLookup.DISPLAY_NAME), null, null, null)
                ?: return null
        var contactName: String? = null
        if (cursor.moveToFirst()) {
            contactName = cursor.getString(cursor.getColumnIndex(PhoneLookup.DISPLAY_NAME))
        }
        if (cursor != null && !cursor.isClosed) {
            cursor.close()
        }
        Log.d(TAG, "getContactName: ${contactName}")
        return contactName
    }

//    private fun getMyData() {
//        val BASE_URL = "https://jsonplaceholder.typicode.com/"
//        val retrofitBuilder = Retrofit.Builder()
//            .addConverterFactory(GsonConverterFactory.create())
//            .baseUrl(BASE_URL)
//            .build()
//            .create(ApiInterface::class.java)
//
//        val retrofitData = retrofitBuilder.getData()
//        retrofitData.enqueue(object : Callback<List<ApiDataItem>> {
//            override fun onResponse(
//                call: retrofit2.Call<List<ApiDataItem>>,
//                response: Response<List<ApiDataItem>>
//            ) {
////                try {
//                val myStringBuilder = StringBuilder()
//                val responseBody = response.body()
//                if (responseBody != null) {
//                    for (myData in responseBody) {
//                        myStringBuilder.append(+myData.id)
////                        myStringBuilder.append("userId"+myData.userId)
//                        Log.d(ContentValues.TAG, "onResponse: data$myStringBuilder \n")
//
////                        tv_calling!!.text = myStringBuilder
//                    }
//                }
////                } catch (e: Exception) {
////                    Log.i(TAG, "onResponse catch: ${e.message}")
////                }
//            }
//
//            override fun onFailure(call: retrofit2.Call<List<ApiDataItem>>, t: Throwable) {
//                tv_calling!!.text = "Unknown Number"
//                Log.i("CallingActivity", "onFailure:${t.message}")
//            }
//        })
//    }


}