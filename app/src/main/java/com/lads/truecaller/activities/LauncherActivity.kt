package com.lads.truecaller.activities

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.lads.truecaller.R
import com.lads.truecaller.databinding.PricavyBottomSheetDialogBinding


class LauncherActivity : AppCompatActivity() {
    private lateinit var btnGetStarted: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launcher)
        btnGetStarted = findViewById(R.id.tv_getStarted)

        btnGetStarted.setOnClickListener {
            val dialog = BottomSheetDialog(this)
            val inflater = LayoutInflater.from(applicationContext)
            val view = PricavyBottomSheetDialogBinding.inflate(inflater)
            dialog?.setContentView(view.root)
            dialog.setCancelable(true)

            dialog.show()
        }
    }
}