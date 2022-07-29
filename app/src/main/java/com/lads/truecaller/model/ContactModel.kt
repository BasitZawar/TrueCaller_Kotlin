package com.lads.truecaller.model

import android.media.Image
import android.provider.ContactsContract

class ContactModel {
    var name: String? = null
    var number: String? = null
    var contactType: String? = null
    var image: Image? = null

    fun setNames(name: String) {
        this.name = name
    }

    fun getNumbers(): String {
        return number.toString()
    }

    fun setNumbers(number: String) {
        this.number = number
    }

    fun getNames(): String {
        return name.toString()
    }

    fun setType(contactType: String) {
        this.contactType = contactType
    }

    @JvmName("setImage1")
    fun setImage(image: Image) {
        this.image = image
    }
    fun getImage(): String {
        return image.toString()
    }
}