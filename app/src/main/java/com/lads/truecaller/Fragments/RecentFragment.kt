package com.lads.truecaller.Fragments

import android.database.Cursor
import android.os.Build
import android.os.Bundle
import android.provider.CallLog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.SimpleCursorAdapter
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.lads.truecaller.R

//import kotlinx.android.synthetic.main.fragment_recent.*

class RecentFragment : Fragment() {

    private lateinit var listViewRecentContacts: ListView
    var cols = arrayListOf<String>(
        CallLog.Calls._ID,
        CallLog.Calls.TYPE,
        CallLog.Calls.DURATION, CallLog.Calls.DATE, CallLog.Calls.NUMBER
    ).toTypedArray()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_recent, container, false)
        listViewRecentContacts = view?.findViewById(R.id.listViewRecentContacts)!!

        displayLog()
        // Inflate the layout for this fragment
        return view
    }

    private fun displayLog() {

        var from = arrayListOf<String>(
            CallLog.Calls.NUMBER,
            CallLog.Calls.DURATION,
            CallLog.Calls.TYPE
        ).toTypedArray()

        val to = intArrayOf(R.id.text_1, R.id.text_2, R.id.text_3)

        val rs: Cursor? = requireActivity().contentResolver.query(
            CallLog.Calls.CONTENT_URI,
            cols as Array<out String>?, null, null, "${CallLog.Calls.LAST_MODIFIED} DESC"
        )

        var adapter = SimpleCursorAdapter(context, R.layout.cardview_recentcalllog, rs, from, to, 0)

        listViewRecentContacts.adapter = adapter
    }

    companion object {}


}