package com.lads.truecaller

import android.telecom.Call
import android.telecom.InCallService
import com.lads.truecaller.activities.CallingActivtiy

class CallService : InCallService() {

    override fun onCallAdded(call: Call) {
        OngoingCall.call = call
        CallingActivtiy.start(this, call)
    }

    override fun onCallRemoved(call: Call) {
        OngoingCall.call = null
    }
}