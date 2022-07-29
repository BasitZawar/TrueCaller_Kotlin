package com.lads.truecaller.Fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lads.truecaller.R
import com.lads.truecaller.adaptor.CustomAdapter
import com.lads.truecaller.model.ContactModel


class ContactsFragment : Fragment() {
    lateinit var recyclerView: RecyclerView
    private var contactModelArrayList: ArrayList<ContactModel>? = null


    @SuppressLint("UseRequireInsteadOfGet", "Range")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_contacts, container, false)

        recyclerView = view.findViewById<RecyclerView>(R.id.rv_ContactFragment)

        recyclerView.layoutManager = LinearLayoutManager(context)

        contactModelArrayList = ArrayList()

        val phones = requireActivity().contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null,
            null,
            null,
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC"
        )
        while (phones!!.moveToNext()) {

            val name =
                phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
            val phoneNumber =
                phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))

            val contactModel = ContactModel()
            contactModel.setNames(name)
            contactModel.setNumbers(phoneNumber)
            contactModelArrayList!!.add(contactModel)

            val adapter = CustomAdapter(contactModelArrayList!!)
            recyclerView.adapter = adapter

        }
        phones.close()

        return view
    }
}