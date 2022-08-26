package com.lads.truecaller.Fragments

import android.annotation.SuppressLint
import android.database.Cursor
import android.os.Build
import android.os.Bundle
import android.provider.CallLog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lads.truecaller.R
import com.lads.truecaller.adaptor.RecentContactAdapter
import com.lads.truecaller.model.RecentContactsModel
import com.lads.truecaller.model.SearchViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*


//import kotlinx.android.synthetic.main.fragment_recent.*

class RecentFragment : Fragment() {
    private lateinit var cols: Array<String>
    private var type: String? = null

    //    private var adapter: RecentContactAdapter? = null
    private val searchViewModel: SearchViewModel by activityViewModels()
    private lateinit var rv_recentContact: RecyclerView
    private lateinit var spinner: Spinner
    private var recentContactArrayList: ArrayList<RecentContactsModel>? = null
    lateinit var recentContactSearch: EditText
    var items = arrayOf("All Calls", "Missed", "Dialed", "Received")


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
        spinner = view?.findViewById(R.id.spinner)

        recentContactSearch = view.findViewById(R.id.recentContactSearchBox)
        recentContactArrayList = ArrayList()
        val adapter =
            activity?.let { ArrayAdapter(it, android.R.layout.simple_list_item_1, items) }
        spinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                selectedItem = parent?.getItemAtPosition(position).toString()
                if (selectedItem == "All Calls") {
                    Toast.makeText(activity, selectedItem, Toast.LENGTH_LONG).show()
                    Companion.adapter = RecentContactAdapter(recentContactArrayList!!)
                    rv_recentContact.adapter = Companion.adapter
                } else {
                    Toast.makeText(context, selectedItem, Toast.LENGTH_LONG).show()
                    Companion.adapter = RecentContactAdapter(recentContactArrayList!!)
                    rv_recentContact.adapter = Companion.adapter
                    adapter?.notifyDataSetChanged()

                }
            }
        }
        spinner.setAdapter(adapter)

        displayRecentContacts()
        return view
    }

    @SuppressLint("Range", "LogNotTimber")
    private fun displayRecentContacts() {
        cols = arrayListOf<String>(
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
            type = cursor.getString(cursor.getColumnIndex(CallLog.Calls.TYPE))
            val date = cursor.getString(cursor.getColumnIndex(CallLog.Calls.DATE))
            val duration = cursor.getString(cursor.getColumnIndex(CallLog.Calls.DURATION))

            when (cursor.getString(2).toInt()) {
                1 -> type = "Incoming"
                2 -> type = "Outgoing"
                3 -> type = "Missed"
                4 -> type = "Received"
                5 -> type = "Rejected"
            }
            if (type == "Incoming") {

            }
            val recentContactModel = RecentContactsModel()
            recentContactModel.recentContactName = recentName
            recentContactModel.recentContasNumber = recentNumber
            recentContactModel.duration = duration
            recentContactModel.contactType = type

            recentContactArrayList!!.add(recentContactModel)

        }
        cursor.close()
        adapter = RecentContactAdapter(recentContactArrayList!!)
        rv_recentContact.adapter = adapter
//      ----------Below the search code of recent fragment--------
//        recentContactSearch.addTextChangedListener(object : TextWatcher {
//            override fun afterTextChanged(s: Editable?) {
//            }
//
//            override fun beforeTextChanged(
//                s: CharSequence?,
//                start: Int,
//                count: Int,
//                after: Int
//            ) {
//            }
//
//            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
//                if (s.isNullOrBlank()) {
//                    recentContactArrayList?.let {
//                        adapter!!.submitList(it)
//                    }
//                    return
//                }
//                GlobalScope.launch(Dispatchers.Main) {
//                    recentContactArrayList?.filter {
//                        it.recentContactName?.lowercase(Locale.getDefault())
//                            ?.contains(s.toString().lowercase(Locale.getDefault())) == true
//                    }
//                        .apply {
//                            adapter!!.submitList(this?.toMutableList() as ArrayList<RecentContactsModel>)
//                        }
//                }
//
//            }
//        })

        searchViewModel.searchQuery.observe(requireActivity()) { query ->
            if (query.isNullOrBlank()) {
                adapter.submitList(recentContactArrayList!!)
                return@observe
            }
            //apply filter here based on query
            GlobalScope.launch(Dispatchers.Main) {
                recentContactArrayList?.filter {
                    it.recentContactName?.lowercase(Locale.getDefault())
                        ?.contains(query.toString().lowercase(Locale.getDefault())) == true
                }
                    .apply {
                        adapter!!.submitList(this?.toMutableList() as ArrayList<RecentContactsModel>)
                    }
            }
        }
    }

    companion object {
        lateinit var adapter: RecentContactAdapter
        var selectedItem: String? = null
    }
}


