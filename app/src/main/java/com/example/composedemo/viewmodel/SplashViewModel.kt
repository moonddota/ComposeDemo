package com.example.composedemo.viewmodel

import androidx.lifecycle.MutableLiveData
import com.example.composedemo.repository.SplashRepository
import com.example.composedemo.util.MMkvHelper
import kotlinx.coroutines.delay

class SplashViewModel : BaseViewModel() {

    private val repository = SplashRepository()

    fun listProjectsTab(jumpMainActivity: () -> Unit) = launch(null, {}, {
        val listRes = repository.listProjectsTab()
        if (listRes.data?.isNullOrEmpty() == false) {
            MMkvHelper.getInstance().saveProjectTabs(listRes.data)
        }
        delay(1000)
    }, {
        jumpMainActivity()
    })

}