package com.lads.truecaller

import android.telecom.Call
import android.telecom.InCallService
import com.lads.truecaller.activities.CallingActivtiy

class CallService : InCallService() {

    override fun onCallAdded(call: Call) {
        OngoingCall.call = call
        OngoingCall.inCallService=this
        CallingActivtiy.start(applicationContext, call)
//        CallingActivtiy.getStartIntent(
//            this
//        )
    }

    override fun onCallRemoved(call: Call) {
        OngoingCall.inCallService=null
        OngoingCall.call = null
    }

    private val callCallback = object : Call.Callback() {
        override fun onStateChanged(call: Call, state: Int) {
            OngoingCall.call
        }
    }
}