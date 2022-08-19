package com.lads.truecaller.Fragments

import android.annotation.SuppressLint
import android.database.Cursor
import android.os.Build
import android.os.Bundle
import android.provider.CallLog
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lads.truecaller.R
import com.lads.truecaller.adaptor.RecentContactAdapter
import com.lads.truecaller.model.RecentContactsModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*
import java.util.concurrent.Executors

//import kotlinx.android.synthetic.main.fragment_recent.*

class RecentFragment : Fragment() {
    private var adapter: RecentContactAdapter? = null

    private lateinit var rv_recentContact: RecyclerView
    private var recentContactArrayList: ArrayList<RecentContactsModel>? = null
    lateinit var recentContactSearch: EditText

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @SuppressLint("Range")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_recent, container, false)

        rv_recentContact = view?.findViewById(R.id.rv_recentContactFragment)!!
        rv_recentContact.layoutManager = LinearLayoutManager(context)

        recentContactSearch = view.findViewById(R.id.recentContactSearchBox)
        recentContactArrayList = ArrayList()

        displayRecentContacts()
        return view
    }

    @SuppressLint("Range", "LogNotTimber")
    private fun displayRecentContacts() {
        var cols = arrayListOf<String>(
            CallLog.Calls.CACHED_NAME,
            CallLog.Calls.NUMBER,
            CallLog.Calls.TYPE,
            CallLog.Calls.DATE,
            CallLog.Calls.DURATION,
            CallLog.Calls._ID
        ).toTypedArray()

        val cursor: Cursor? = requireActivity().contentResolver.query(
            CallLog.Calls.CONTENT_URI,
            cols, null, null, "${CallLog.Calls.LAST_MODIFIED} DESC"
        )
        while (cursor?.moveToNext()!!) {
            val recentName = cursor.getString(cursor.getColumnIndex(CallLog.Calls.CACHED_NAME))
            val recentNumber = cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER))
            var type = cursor.getString(cursor.getColumnIndex(CallLog.Calls.TYPE))
            val date = cursor.getString(cursor.getColumnIndex(CallLog.Calls.DATE))
            val duration = cursor.getString(cursor.getColumnIndex(CallLog.Calls.DURATION))


            when (cursor.getString(2).toInt()) {
                1 -> type = "Incoming"
                2 -> type = "Outgoing"
                3 -> type = "Missed"
                4 -> type = "Received"
                5 -> type = "Rejected"
            }
            val recentContactModel = RecentContactsModel()
            recentContactModel.recentContactName = recentName
            recentContactModel.recentContasNumber = recentNumber
            recentContactModel.contactType = type

            recentContactArrayList!!.add(recentContactModel)

        }
        cursor.close()
        var adapter = RecentContactAdapter(recentContactArrayList!!)
        rv_recentContact.adapter = adapter

        recentContactSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.isNullOrBlank()) {
                    recentContactArrayList?.let {
                        adapter.submitList(it)
                    }
                    return
                }
                GlobalScope.launch(Dispatchers.Main) {
                    recentContactArrayList?.filter {
                        it.recentContactName?.lowercase(Locale.getDefault())
                            ?.contains(s.toString().lowercase(Locale.getDefault())) == true
                    }
                        .apply {
                            adapter.submitList(this?.toMutableList() as ArrayList<RecentContactsModel>)
                        }
                }

            }
        })
    }
}


