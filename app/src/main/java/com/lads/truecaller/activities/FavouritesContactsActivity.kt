package com.lads.truecaller.activities

import android.annotation.SuppressLint
import android.content.ContentValues
import android.database.Cursor
import android.os.Bundle
import android.provider.ContactsContract
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.lads.truecaller.adaptor.FavoriteContactsAdapter
import com.lads.truecaller.databinding.ActivityFavouritesContactsBinding
import com.lads.truecaller.model.FavoriteContactModel
import java.util.*

class FavouritesContactsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFavouritesContactsBinding
    private var favoriteContactArrayList: ArrayList<FavoriteContactModel>? = null
    var selection = ContactsContract.Contacts.STARRED + "='1'"

    private var projection = arrayOf(
        ContactsContract.Contacts.DISPLAY_NAME,
        ContactsContract.Contacts.HAS_PHONE_NUMBER,
        ContactsContract.Contacts.STARRED,
        ContactsContract.Contacts._ID,
//        CallLog.Calls.NUMBER
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavouritesContactsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.favouritesArrowBack.setOnClickListener {
            onBackPressed()
        }

        binding.rvFavoriteContactsActivity.layoutManager = LinearLayoutManager(this)

        favoriteContactArrayList = ArrayList()

        displayFavoriteContacts()
    }

    @SuppressLint("Range")
    private fun displayFavoriteContacts() {
        val cursor: Cursor = managedQuery(
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
            Log.i(ContentValues.TAG, "displayFavoriteContacts: $name : $number :$id")

            val favoriteContactModel = FavoriteContactModel()
            favoriteContactModel.favContactName = name
            favoriteContactModel.favContasNumber = number

            favoriteContactArrayList!!.add(favoriteContactModel)
        }
        var adapter = FavoriteContactsAdapter(favoriteContactArrayList!!)
        binding.rvFavoriteContactsActivity.adapter = adapter

        binding.imageViewFavouriteSearch.setOnClickListener {
            binding.layoutFavouritesSearchBox.isVisible = true
            binding.tvFavourites.isVisible = false
        }
        binding.FavouritesSearchBox.setOnClickListener {
            binding.FavouritesSearchBox.text.clear()
            binding.layoutFavouritesSearchBox.isVisible = false
            binding.tvFavourites.isVisible = true
        }
        binding.FavouritesSearchBox.addTextChangedListener(object : TextWatcher {
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
                        adapter.submitList(this?.toMutableList() as java.util.ArrayList<FavoriteContactModel>)
                    }
            }
        })
    }
}