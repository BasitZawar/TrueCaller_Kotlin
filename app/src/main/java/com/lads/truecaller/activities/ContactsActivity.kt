package com.lads.truecaller.activities

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.lads.truecaller.adaptor.AllContactsCustomAdapter
import com.lads.truecaller.databinding.ActivityContactsBinding
import com.lads.truecaller.model.ContactModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*


class ContactsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityContactsBinding
    private var contactModelArrayList: ArrayList<ContactModel>? = null
    private var adapter: AllContactsCustomAdapter? = null
    private var phoneNumber: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContactsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvContactActivity.layoutManager = LinearLayoutManager(this)
        contactModelArrayList = java.util.ArrayList()

        binding.imageViewSearch.setOnClickListener {
            binding.layoutSearchBox.isVisible = true
            binding.tvTrueCaller.isVisible = false
        }
        binding.ContactsActivitySearchBox.setOnClickListener {
            binding.ContactsActivitySearchBox.text.clear()
            binding.layoutSearchBox.isVisible = false
            binding.tvTrueCaller.isVisible = true
        }
        displayContacts()


        binding.ContactsActivitySearchBox.addTextChangedListener(object : TextWatcher {
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
                    contactModelArrayList?.let {
                        adapter!!.submitList(it)
                    }
                    return
                }
                GlobalScope.launch(Dispatchers.Main) {
                    contactModelArrayList?.filter {
                        it.name?.lowercase(Locale.getDefault())
                            ?.contains(s.toString().lowercase(Locale.getDefault())) == true
                    }
                        .apply {
                            adapter!!.submitList(this?.toMutableList() as ArrayList<ContactModel>)
                        }
                }

            }
        })
    }

    @SuppressLint("Range")
    private fun displayContacts() {
        val cursor = contentResolver.query(
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
            val contactModel = ContactModel()
            contactModel.name = name

            if ((phoneNumber!!.startsWith("03") || phoneNumber!!.startsWith("+") || phoneNumber!!.startsWith(
                    "92"
                ) || phoneNumber!!.startsWith(
                    "+92"
                ))
            ) {
                contactModel.number = phoneNumber
            }
            val photo_uri =
                cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI))
            if (photo_uri != null) {
                contactModel.image = MediaStore.Images.Media.getBitmap(
                    contentResolver,
                    Uri.parse(photo_uri)
                )
            }
            contactModelArrayList!!.add(contactModel)
            adapter = AllContactsCustomAdapter(contactModelArrayList!!)

            adapter = AllContactsCustomAdapter(contactModelArrayList!!)
            binding.rvContactActivity.adapter = adapter
        }
        cursor.close()
    }
}