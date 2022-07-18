package com.lads.truecaller.Fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.lads.truecaller.R

class KeypadFragment : Fragment() {
    lateinit var tv_Calling_Number: TextView
    lateinit var btnDail: ImageButton
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
            getActivity()?.startActivity(dailIntent)
//            val intent = Intent(
//                requireContext(), CallingActivtiy::class.java
//            )
//            startActivity(intent)
        })

        textView1.setOnClickListener(View.OnClickListener {
            updateText(textView1.getTag().toString())
        })
        textView2.setOnClickListener(View.OnClickListener {
            updateText(textView2.getTag().toString())
        })

        textView3.setOnClickListener(View.OnClickListener {
            updateText(textView3.getTag().toString())
        })
        textView4.setOnClickListener(View.OnClickListener {
            updateText(textView4.getTag().toString())
        })
        textView5.setOnClickListener(View.OnClickListener {
            updateText(textView5.getTag().toString())
        })
        textView6.setOnClickListener(View.OnClickListener {
            updateText(textView6.getTag().toString())
        })
        textView7.setOnClickListener(View.OnClickListener {
            updateText(textView7.getTag().toString())
        })
        textView8.setOnClickListener(View.OnClickListener {
            updateText(textView8.getTag().toString())
        })
        textView9.setOnClickListener(View.OnClickListener {
            updateText(textView9.getTag().toString())
        })
        textView0.setOnClickListener(View.OnClickListener {
            updateText(textView0.getTag().toString())
        })
        textViewStar.setOnClickListener(View.OnClickListener {
            updateText(textViewStar.getTag().toString())
        })
        textViewHash.setOnClickListener(View.OnClickListener {
            updateText(textViewHash.getTag().toString())

        })
        return view

    }

    fun updateText(newValue: String) {
        tv_Calling_Number.setText("" + tv_Calling_Number.text + newValue)
    }
}