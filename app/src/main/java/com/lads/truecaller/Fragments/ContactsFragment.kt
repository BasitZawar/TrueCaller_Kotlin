package com.lads.truecaller.Fragments

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lads.truecaller.R
import com.lads.truecaller.adaptor.AllContactsCustomAdapter
import com.lads.truecaller.model.ContactModel
import java.util.*
import java.util.concurrent.Executors


class ContactsFragment : Fragment() {
    private var photoUrl: String? = null
    private var phoneNumber: String? = null
    private var adapter: AllContactsCustomAdapter? = null
    lateinit var recyclerView: RecyclerView
    private var contactModelArrayList: ArrayList<ContactModel>? = null
    lateinit var editText: EditText
    var lastnumber = "0"
    private var photoId: Int? = null


    @SuppressLint("UseRequireInsteadOfGet", "Range")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_contacts, container, false)

        recyclerView = view.findViewById<RecyclerView>(R.id.rv_ContactFragment)

        recyclerView.layoutManager = LinearLayoutManager(context)
//        searchView = view.findViewById(R.id.searchView_AllContacts)
        editText = view.findViewById(R.id.allContactSearch_EditText)

        contactModelArrayList = ArrayList()

        contactModelArrayList!!.clear()
        val cursor = requireActivity().contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null,
            null,
            null,
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC"
        )
        while (cursor!!.moveToNext()) {
            val name =
                cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
            phoneNumber =
                cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                    .trim()

//            Log.d(TAG, "onCreateView: contact list ---- :${profileImage}")

            val contactModel = ContactModel()
            contactModel.name = name
//            contactModel.imageId = profileImage
            if (phoneNumber != null || phoneNumber != lastnumber) {
                if ((phoneNumber!!.startsWith("03") || phoneNumber!!.startsWith("+") || phoneNumber!!.startsWith(
                        "92"
                    ) || phoneNumber!!.startsWith(
                        "+92"
                    ))
                ) {
                    contactModel.number = phoneNumber
                    Log.d(TAG, "onCreateView: contact list number :${contactModel.number}")
                }
            } else {
                Toast.makeText(context, "$phoneNumber", Toast.LENGTH_SHORT).show()
            }
            val photo_uri =
                cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI))
            if (photo_uri != null) {

                contactModel.image = MediaStore.Images.Media.getBitmap(
                    requireActivity().contentResolver,
                    Uri.parse(photo_uri)
                )
            }

            contactModelArrayList!!.add(contactModel)

            adapter = context?.let { AllContactsCustomAdapter(contactModelArrayList!!, it) }
            recyclerView.adapter = adapter
        }
        cursor.close()

        editText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s != null) {
                    if (s.length > 3) {
                        filter(s.toString())
                    } else {
                        filter(s.toString())
                        recyclerView.adapter = adapter
                    }
                }
            }

            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {

            }

            override fun onTextChanged(
                s: CharSequence,
                start: Int,
                before: Int,
                count: Int
            ) {

//                        if (s.isNullOrBlank()) {
//                            contactModelArrayList?.let {
//                                adapter.submitList(it)
//                            }
//                            return
//                        }
//                        contactModelArrayList?.filter {
//                            it.name?.lowercase(Locale.getDefault())
//                                ?.contains(s.toString().lowercase(Locale.getDefault())) == true
//                        }
//                            .apply {
//                                adapter.submitList(this?.toMutableList() as ArrayList<ContactModel>)
//                            }
            }
        })

        return view
    }

    private fun filter(text: String) {
        val newList: ArrayList<ContactModel> = ArrayList()
        Executors.newSingleThreadExecutor().execute {
            for (item in contactModelArrayList!!) {
                if (item!!.name!!.lowercase(Locale.getDefault())
                        .contains(text.lowercase(Locale.getDefault())) || item!!.number!!.lowercase(
                        Locale.getDefault()
                    )
                        .contains(text.lowercase(Locale.getDefault()))
                ) {
                    newList.add(item)
                }
            }
            activity?.runOnUiThread {
                adapter!!.submitList(newList)
            }
        }
    }


    //    private fun filter(toString: String) {
//        val allContactsFilteredList: ArrayList<ContactModel> = ArrayList()
//
//        for (item in contactModelArrayList!!) {
//            if (item.name!!.toLowerCase().contains(toString.toLowerCase())) {
//                allContactsFilteredList.add(item)
//            }
//        }
//        val adapter = AllContactsCustomAdapter(allContactsFilteredList!!)
//
//        recyclerView.adapter = adapter
//    }
}