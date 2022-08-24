package com.lads.truecaller2.activities

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.lads.truecaller2.databinding.ActivityDialpadBinding


class DialpadActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDialpadBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDialpadBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val REQUESTED_PERMISSIONS = arrayOf(
            Manifest.permission.CALL_PHONE,
            Manifest.permission.WRITE_CALL_LOG,
            Manifest.permission.READ_PHONE_STATE,
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val permissionGranted = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CALL_PHONE
            ) != PackageManager.PERMISSION_GRANTED
            if (permissionGranted) {
                ActivityCompat.requestPermissions(this, REQUESTED_PERMISSIONS, 22)
            } else {
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()
            }
        } else {
        }


        binding.layout1.setOnClickListener(View.OnClickListener {
            valueLimit()
            updateText(binding.tv1.tag.toString())
        })
        binding.layout2.setOnClickListener(View.OnClickListener {
            valueLimit()
            updateText(binding.tv2.tag.toString())
        })
        binding.layout3.setOnClickListener(View.OnClickListener {
            valueLimit()
            updateText(binding.tv3.tag.toString())
        })
        binding.layout4.setOnClickListener(View.OnClickListener {
            valueLimit()
            updateText(binding.tv4.tag.toString())
        })
        binding.layout5.setOnClickListener(View.OnClickListener {
            valueLimit()
            updateText(binding.tv5.tag.toString())
        })
        binding.layout6.setOnClickListener(View.OnClickListener {
            valueLimit()
            updateText(binding.tv6.tag.toString())
        })
        binding.layout7.setOnClickListener(View.OnClickListener {
            valueLimit()
            updateText(binding.tv7.tag.toString())
        })
        binding.layout8.setOnClickListener(View.OnClickListener {
            valueLimit()
            updateText(binding.tv8.tag.toString())
        })
        binding.layout9.setOnClickListener(View.OnClickListener {
            valueLimit()
            updateText(binding.tv9.tag.toString())
        })
        binding.layoutStar.setOnClickListener(View.OnClickListener {
            valueLimit()
            updateText(binding.tvStar.tag.toString())
        })
        binding.layout0.setOnClickListener(View.OnClickListener {
            valueLimit()
            updateText(binding.tv0.tag.toString())
        })
        binding.layoutHash.setOnClickListener(View.OnClickListener {
            valueLimit()
            updateText(binding.tvHash.tag.toString())
        })
        binding.iconClear.setOnClickListener {
            newText()
            if (binding.tvDialingNumber.length() == 0) {

                binding.iconClear.isVisible = false
            }
        }
        binding.iconDial.setOnClickListener {
            val dailIntent = Intent(Intent.ACTION_CALL)
            dailIntent.data = Uri.parse("tel:" + binding.tvDialingNumber.text.toString())
//          activity?.startActivity(dailIntent)
            startActivity(dailIntent)
        }
    }

    private fun updateText(newValue: String) {
        binding.tvDialingNumber.text = "" + binding.tvDialingNumber.text + newValue
        binding.iconClear.isVisible = true
    }

    private fun valueLimit() {
        if (binding.tvDialingNumber.length() == 13) {
            Toast.makeText(this, "Max length reached", Toast.LENGTH_SHORT).show()
        }
    }

    private fun newText() {
        if (binding.tvDialingNumber.length() > 0) {
            val string1 = binding.tvDialingNumber.text.toString()
            val string = binding.tvDialingNumber.text.substring(0, (string1.length - 1))
            binding.tvDialingNumber.setText(string)
//            updateText(string)
            binding.iconClear.isVisible = true

        }
    }
}