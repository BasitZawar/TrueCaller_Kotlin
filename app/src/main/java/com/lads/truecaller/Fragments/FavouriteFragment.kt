package com.lads.truecaller.Fragments

import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.SimpleCursorAdapter
import androidx.fragment.app.Fragment
import com.lads.truecaller.R
import kotlinx.android.synthetic.main.itemview_layout_favorite_contacts.view.*


class FavouriteFragment : Fragment() {
    private lateinit var listViewFavoriteContacts: ListView
    var selection = ContactsContract.Contacts.STARRED + "='1'"

    var projection = arrayOf(
        ContactsContract.Contacts._ID,
        ContactsContract.Contacts.DISPLAY_NAME,
        ContactsContract.Contacts.STARRED,
//        CallLog.Calls.NUMBER
    )

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
            intArrayOf(R.id.tv_RecentContactNumber, R.id.tv_RecentContactName)
        val cursor: Cursor = requireActivity().managedQuery(
            ContactsContract.Contacts.CONTENT_URI,
            projection,
            selection,
            null, null
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
        listViewFavoriteContacts.setOnItemClickListener { parent, view, position, id ->
            val dailIntent = Intent(Intent.ACTION_CALL)
            dailIntent.data =
                Uri.parse("tel:${view.tv_RecentContactNumber.text}")
            requireView().context.startActivity(dailIntent)
        }

    }
}