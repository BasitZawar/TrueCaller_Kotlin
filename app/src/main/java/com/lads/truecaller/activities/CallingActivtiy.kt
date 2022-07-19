package com.lads.truecaller.activities

import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.lads.truecaller.CallManager
import com.lads.truecaller.GsmCall
import com.lads.truecaller.R

class CallingActivtiy : AppCompatActivity() {

//    private var updatesDisposable = Disposables.empty()
    private lateinit var imageButton: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calling_activtiy)

        imageButton = findViewById(R.id.btnEndCall)

        imageButton.setOnClickListener(View.OnClickListener {
            CallManager.cancelCall()

        })
    }

//    override fun onResume() {
//        super.onResume()
//        updatesDisposable = CallManager.updates()
//            .subscribe { updateView(it) }
//    }
//
//    override fun onPause() {
//        super.onPause()
//        updatesDisposable.dispose()
//    }

    private fun updateView(gsmCall: GsmCall) {
        // Update the views here
    }

}