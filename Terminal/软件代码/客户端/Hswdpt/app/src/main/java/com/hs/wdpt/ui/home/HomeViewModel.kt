package com.hs.wdpt.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {
    public val homePageItemList = ArrayList<HomePageItem>()
}