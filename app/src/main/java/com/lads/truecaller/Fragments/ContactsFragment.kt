package com.lads.truecaller.Fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lads.truecaller.R
import com.lads.truecaller.model.ItemsViewModel
import com.lads.truecaller.adaptor.CustomAdapter


class ContactsFragment : Fragment() {
    lateinit var recyclerView: RecyclerView
//    lateinit var listView: ListView
//    val arrayList = ArrayList<String>()
//    lateinit var adapter: ArrayAdapter<String>
//    var arrayList = listOf("Mumbai", "Dehli", "Islamabad", "DIK", "Peshawar", "a", 'b', 'c')

    @SuppressLint("UseRequireInsteadOfGet")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_contacts, container, false)
//        listView = view.findViewById(R.id.listView_items)
//        adapter = getActivity()?.let {
//            ArrayAdapter<String>(
//                it,
//                android.R.layout.simple_list_item_1,
//                arrayList
//            )
//        }!!
//        listView.adapter = adapter
//        arrayList.add("a")
//        arrayList.add("b")
//        arrayList.add("c")
//        arrayList.add("d")
//        arrayList.add("e")
//        arrayList.add("f")
//        arrayList.add("a")
//        arrayList.add("b")
//        arrayList.add("c")
//        arrayList.add("d")
//        arrayList.add("e")
//        arrayList.add("f")
//        arrayList.add("a")
//        arrayList.add("b")
//        arrayList.add("c")
//        arrayList.add("d")
//        arrayList.add("e")
//        arrayList.add("f")
//        listView.setOnItemClickListener { parent, view, position, id ->
//            val s = adapter.getPosition(position.toString())
//
////            val element = adapter.getItemAtPosition(position) // The item that was clicked
//
//        }

        recyclerView = view.findViewById<RecyclerView>(R.id.rv_ContactFragment)

        recyclerView.layoutManager = LinearLayoutManager(context)

        val data = ArrayList<ItemsViewModel>()
        for (i in 1..10) {
            data.add(ItemsViewModel(R.drawable.ic_launcher_background, "Item " + i, i.toString()))
        }
        val adapter = CustomAdapter(data)
        recyclerView.adapter = adapter

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        listView = view?.findViewById(R.id.list)
//        var list = listOf("Mumbai", "Dehli", "Islamabad", "DIK", "Peshawar", "a", 'b', 'c')
//        val aa = activity?.let { ArrayAdapter(it, R.id.list, list) }
    }

    companion object {

    }
}