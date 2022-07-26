package com.lads.truecaller.model

import android.media.Image

class ContactModel {
    var name: String? = null
    var number: String? = null
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

}