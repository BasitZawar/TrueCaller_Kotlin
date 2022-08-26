package com.lads.truecaller.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SearchViewModel : ViewModel() {

    var searchQuery = MutableLiveData<String>()

    var isSearchCleared = MutableLiveData<Boolean>()

    fun searchQuery(query: String) {
        searchQuery.postValue(query)
    }

    fun closeSearch(closeSearch: Boolean) {
        isSearchCleared.postValue(closeSearch)
    }

}

