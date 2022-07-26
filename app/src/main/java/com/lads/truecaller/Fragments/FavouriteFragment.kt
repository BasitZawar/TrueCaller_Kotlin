package com.lads.truecaller.Fragments

import android.database.Cursor
import android.os.Bundle
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.SimpleCursorAdapter
import androidx.fragment.app.Fragment
import com.lads.truecaller.R


class FavouriteFragment : Fragment() {
    private lateinit var listViewFavoriteContacts: ListView
    var projection = arrayOf(
        ContactsContract.Contacts._ID,
        ContactsContract.Contacts.DISPLAY_NAME,
        ContactsContract.Contacts.STARRED
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_favourite, container, false)
        listViewFavoriteContacts = view.findViewById(R.id.listViewFavoriteContacts)

        displayFavoriteContacts()
        return view
    }

    private fun displayFavoriteContacts() {

        val to =
            intArrayOf(R.id.recentContactNumber, R.id.recentContactName)


        val cursor: Cursor = requireActivity().managedQuery(
            ContactsContract.Contacts.CONTENT_URI,
            projection,
            "starred=?",
            arrayOf("1"),
            "${ContactsContract.Contacts.STARRED} DESC"
        )


        var adapter = SimpleCursorAdapter(
            context,
            R.layout.itemview_layout_favorite_contacts,
            cursor,
            projection,
            to,
            0
        )
        listViewFavoriteContacts.adapter = adapter
    }

    companion object {}
}