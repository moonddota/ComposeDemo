package com.example.composedemo.viewmodel

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.example.composedemo.bean.ArticleBean
import com.example.composedemo.bean.ProjectListRes
import com.example.composedemo.bean.UserInfo
import com.example.composedemo.model.PlayError
import com.example.composedemo.model.PlayState
import com.example.composedemo.model.PlaySuccess
import com.example.composedemo.repository.Repository
import com.example.composedemo.ui.CourseTabs
import com.example.composedemo.ui.MainActions
import com.example.composedemo.util.CacheUtil
import com.example.composedemo.util.MMkvHelper
import com.example.composedemo.util.toast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MyViewModel : BaseViewModel() {
    private val repository by lazy { Repository() }

    private val bannerList = MutableLiveData<PlayState>()
    val banners: LiveData<PlayState> = bannerList
    fun getBanner() = launchUI() {
        val listRes = repository.getBanner().data ?: listOf()
        if (listRes.isEmpty()) {
            bannerList.postValue(PlayError(Throwable("未获取到数据")))
        } else {
            bannerList.postValue(PlaySuccess(listRes.onEach {
                it.desc = it.imagePath
            }))
        }
    }

    private val _position = MutableLiveData(CourseTabs.HOME_PAGE)
    val position: LiveData<CourseTabs> = _position
    fun onPositionChanged(position: CourseTabs) {
        _position.value = position
    }

    val homeListData = MutableLiveData<MutableList<ArticleBean>>()
    private val homeList by lazy { mutableListOf<ArticleBean>() }
    private var page = 0
    private var isMore = true
    fun listArticle(isLoadMore: Boolean) = launchUI() {
        if (!isMore && isLoadMore) {
            return@launchUI
        }
        page = if (isLoadMore) ++page else 0
        val listRes = repository.listArticle(page)
        if (!isLoadMore) homeList.clear()
        homeList.addAll(listRes.data?.datas ?: mutableListOf())
        if (homeList.size >= listRes.data?.total ?: 0) {
            isMore = false
        } else {
            homeListData.postValue(homeList)
            Log.e("tag", "'homeListData  " + homeListData.value?.size)
            isMore = true
        }
    }

    val projectTabs = MutableLiveData<PlayState>(PlaySuccess(listOf<ProjectListRes>()))
    fun getProjectTabs() = launchUI {
        val listRes = repository.listProjectsTab()
        if (listRes.data?.isNullOrEmpty() == false) {
            projectTabs.postValue(PlaySuccess(listRes.data))
            getListProjects(listRes.data!![0].id ?: "", false)
        } else {
            projectTabs.postValue(PlayError(Throwable("数据到头了")))
        }
    }

    val projectsListData = MutableLiveData(mutableListOf<ArticleBean>())
    private var listProjectsPage = 0
    private val projectsList by lazy { mutableListOf<ArticleBean>() }
    fun getListProjects(id: String, isLoadMore: Boolean) = launchUI {
        listProjectsPage = if (isLoadMore) ++listProjectsPage else 0
        val res = repository.getListProjects(listProjectsPage, id)
        if (!isLoadMore) projectsList.clear()
        projectsList.addAll(res.data?.datas ?: mutableListOf())
        if (projectsList.size >= res.data?.total ?: 0) {
        } else {
            projectsListData.postValue(projectsList)
        }
    }

    val collectsStatus = MutableLiveData<Boolean>()
    fun cancelCollects(id: String) = launchUI {
        repository.cancelCollects(id)
        collectsStatus.postValue(false)
    }

    fun toCollects(id: String) = launchUI {
        repository.toCollects(id)
        collectsStatus.postValue(true)
    }

    val userInfo = MutableLiveData<UserInfo>()
    fun getIntegral() = launchUI() {
        val res = repository.getIntegral()
        userInfo.postValue(res.data)
        MMkvHelper.getInstance().saveUserInfo(res.data)
    }

    fun login(actions: MainActions, username: String, password: String) =
        launch(null, {}, {
            val res = repository.login(username, password)
            MMkvHelper.getInstance().saveUserInfo(res.data)
            userInfo.postValue(res.data)
        }, {
            actions.upPress()
        })

    var cacheData = MutableLiveData<String>()
    fun getCache() = launchUI(cacheData) {
        CacheUtil.getTotalCacheSize()
    }

    fun clearCache() = launchUI(cacheData) {
        CacheUtil.clearAllCache()
        CacheUtil.getTotalCacheSize()
    }

    @SuppressLint("NullSafeMutableLiveData")
    fun logout(actions: MainActions) = launch(null, {}, {
        repository.logout()
        MMkvHelper.getInstance().logout()
        userInfo.postValue(null)
    }, {
        actions.loginOut()
    })

}



