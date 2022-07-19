package com.lads.truecaller

import android.telecom.Call
import java.util.*

object CallManager {
//    private val subject = BehaviorSubject.create<GsmCall>()
    private var currentCall: Call? = null

//    fun updates(): Observable<GsmCall> = subject

//    fun updateCall(call: Call?) {
//        call?.let {
//            subject.onNext(it.toGsmCall())
//        }
//    }
    fun cancelCall() {
        currentCall?.let {
            when (it.state) {
                Call.STATE_RINGING -> rejectCall()
                else               -> disconnectCall()
            }
        }
    }

    fun acceptCall() {
        currentCall?.let {
            it.answer(it.details.videoState)
        }
    }

    private fun rejectCall() {
        currentCall?.reject(false, "")
    }

    private fun disconnectCall() {
        currentCall?.disconnect()
    }

    fun Call.toGsmCall() = GsmCall(
        status = state.toGsmCallStatus(),
        displayName = details.handle.schemeSpecificPart // This will return the phone number as dialed
    )

    private fun Int.toGsmCallStatus() = when (this) {
        Call.STATE_ACTIVE -> GsmCall.Status.ACTIVE
        Call.STATE_RINGING -> GsmCall.Status.RINGING
        Call.STATE_CONNECTING -> GsmCall.Status.CONNECTING
        Call.STATE_DIALING -> GsmCall.Status.DIALING
        Call.STATE_DISCONNECTED -> GsmCall.Status.DISCONNECTED
        else -> GsmCall.Status.UNKNOWN
    }
}