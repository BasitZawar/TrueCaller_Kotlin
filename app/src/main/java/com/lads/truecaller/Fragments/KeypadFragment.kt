package com.lads.truecaller.Fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.lads.truecaller.R

class KeypadFragment : Fragment() {
    lateinit var tv_Calling_Number: TextView
    lateinit var btnDail: ImageButton
    lateinit var btnClear: ImageButton
    lateinit var textView1: TextView
    lateinit var textView2: TextView
    lateinit var textView3: TextView
    lateinit var textView4: TextView
    lateinit var textView5: TextView
    lateinit var textView6: TextView
    lateinit var textView7: TextView
    lateinit var textView8: TextView
    lateinit var textView9: TextView
    lateinit var textView0: TextView
    lateinit var textViewStar: TextView
    lateinit var textViewHash: TextView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_keypad, container, false)
        btnDail = view.findViewById(R.id.btnDial)
        btnClear = view.findViewById(R.id.btnClear)
        textView1 = view.findViewById(R.id.tv_1)
        textView2 = view.findViewById(R.id.tv_2)
        textView3 = view.findViewById(R.id.tv_3)
        textView4 = view.findViewById(R.id.tv_4)
        textView5 = view.findViewById(R.id.tv_5)
        textView6 = view.findViewById(R.id.tv_6)
        textView7 = view.findViewById(R.id.tv_7)
        textView8 = view.findViewById(R.id.tv_8)
        textView9 = view.findViewById(R.id.tv_9)
        textView0 = view.findViewById(R.id.tv_0)
        textViewStar = view.findViewById(R.id.tv_star)
        textViewHash = view.findViewById(R.id.tv_hash)
        tv_Calling_Number = view.findViewById(R.id.tv_callingNumber)

        btnDail.setOnClickListener(View.OnClickListener {
            val dailIntent = Intent(Intent.ACTION_CALL)
//            dailIntent.data = Uri.parse("tel:" + "8344814819")
            dailIntent.data = Uri.parse("tel:" + tv_Calling_Number.text.toString())
            activity?.startActivity(dailIntent)
        })
        btnClear.setOnClickListener {
            tv_Calling_Number.text = ""
            btnClear.isVisible = false
        }
        textView1.setOnClickListener(View.OnClickListener {
            updateText(textView1.tag.toString())
        })
        textView2.setOnClickListener(View.OnClickListener {
            updateText(textView2.tag.toString())
        })

        textView3.setOnClickListener(View.OnClickListener {
            updateText(textView3.tag.toString())
        })
        textView4.setOnClickListener(View.OnClickListener {
            updateText(textView4.tag.toString())
        })
        textView5.setOnClickListener(View.OnClickListener {
            updateText(textView5.tag.toString())
        })
        textView6.setOnClickListener(View.OnClickListener {
            updateText(textView6.tag.toString())
        })
        textView7.setOnClickListener(View.OnClickListener {
            updateText(textView7.tag.toString())
        })
        textView8.setOnClickListener(View.OnClickListener {
            updateText(textView8.tag.toString())
        })
        textView9.setOnClickListener(View.OnClickListener {
            updateText(textView9.tag.toString())
        })
        textView0.setOnClickListener(View.OnClickListener {
            updateText(textView0.tag.toString())
        })
        textViewStar.setOnClickListener(View.OnClickListener {
            updateText(textViewStar.tag.toString())
        })
        textViewHash.setOnClickListener(View.OnClickListener {
            updateText(textViewHash.tag.toString())

        })

        return view

    }

    fun updateText(newValue: String) {
        tv_Calling_Number.text = "" + tv_Calling_Number.text + newValue
        btnClear.isVisible = true
    }

}