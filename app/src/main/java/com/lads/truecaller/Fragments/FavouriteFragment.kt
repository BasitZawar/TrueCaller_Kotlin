package com.lads.truecaller.Fragments

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.database.Cursor
import android.os.Bundle
import android.provider.ContactsContract
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lads.truecaller.R
import com.lads.truecaller.adaptor.FavoriteContactsAdapter
import com.lads.truecaller.model.FavoriteContactModel
import java.util.*


class FavouriteFragment : Fragment() {
    private lateinit var rv_favoriteContact: RecyclerView
    private var favoriteSearchBox: EditText? = null
    var selection = ContactsContract.Contacts.STARRED + "='1'"
    private var favoriteContactArrayList: ArrayList<FavoriteContactModel>? = null

    private var projection = arrayOf(
        ContactsContract.Contacts.DISPLAY_NAME,
        ContactsContract.Contacts.HAS_PHONE_NUMBER,
        ContactsContract.Contacts.STARRED,
        ContactsContract.Contacts._ID,
//        CallLog.Calls.NUMBER
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_favourite, container, false)
//      listViewFavoriteContacts = view.findViewById(R.id.listViewFavoriteContacts)
        rv_favoriteContact = view?.findViewById(R.id.rv_FavoriteContactFragment)!!
        rv_favoriteContact.layoutManager = LinearLayoutManager(context)
        favoriteSearchBox = view.findViewById(R.id.favoriteContactSearchBox)

        favoriteContactArrayList = ArrayList()

        displayFavoriteContacts()
        return view
    }

    @SuppressLint("Range")
    private fun displayFavoriteContacts() {
        val cursor: Cursor = requireActivity().managedQuery(
            ContactsContract.Contacts.CONTENT_URI,
            projection,
            selection,
            null, null
        )
        while (cursor.moveToNext()!!) {
            val name =
                cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
            val id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.STARRED))
            val number =
                cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))
            Log.i(TAG, "displayFavoriteContacts: $name : $number :$id")

            val favoriteContactModel = FavoriteContactModel()
            favoriteContactModel.favContactName = name
            favoriteContactModel.favContasNumber = number

            favoriteContactArrayList!!.add(favoriteContactModel)
        }
        var adapter = FavoriteContactsAdapter(favoriteContactArrayList!!)
        rv_favoriteContact.adapter = adapter


        favoriteSearchBox?.addTextChangedListener(object : TextWatcher {
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
                    favoriteContactArrayList?.let {
                        adapter.submitList(it)
                    }
                    return
                }
                favoriteContactArrayList?.filter {
                    it.favContactName?.lowercase(Locale.getDefault())
                        ?.contains(s.toString().lowercase(Locale.getDefault())) == true
                }
                    .apply {
                        adapter.submitList(this?.toMutableList() as ArrayList<FavoriteContactModel>)
                    }
            }
        })
    }
}


