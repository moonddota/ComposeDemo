package com.example.composedemo.viewmodel

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.example.composedemo.bean.*
import com.example.composedemo.model.PlayError
import com.example.composedemo.model.PlayState
import com.example.composedemo.model.PlaySuccess
import com.example.composedemo.network.BaseData
import com.example.composedemo.network.api.RequestService
import com.example.composedemo.repository.Repository
import com.example.composedemo.ui.CourseTabs
import com.example.composedemo.ui.MainActions
import com.example.composedemo.util.CacheUtil
import com.example.composedemo.util.MMkvHelper
import com.example.composedemo.util.ResourceUtil
import com.example.composedemo.util.toast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MyViewModel : BaseViewModel() {
    val repository by lazy { Repository() }

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

    val showLoging = MutableLiveData<Boolean>()

    fun login(actions: MainActions, username: String, password: String) = launch(null, {}, {
        val res = repository.login(username, password)
        MMkvHelper.getInstance().saveUserInfo(res.data)
        userInfo.postValue(res.data)
    }, {

    })

    fun register(actions: MainActions, username: String, password: String, repassword: String) =
        launch(null, {}, {
            val res = repository.register(username, password,repassword)
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

    val listIntegralData = MutableLiveData<PlayState>()
    private var listIntegralPage = 1
    fun listIntegral(isMore: Boolean) = launchUI() {
        listIntegralPage = if (isMore) ++listIntegralPage else 1
        val res = repository.listIntegral(listIntegralPage)
        listIntegralData.postValue(PlaySuccess(res.data))
    }

    val squarePagePosition = MutableLiveData(0)
    fun changeSquarePagePosition(index: Int) {
        squarePagePosition.value = index
    }


    val SquareTrees = MutableLiveData<List<TreeListRes>>()
    fun listTrees() = launchUI(SquareTrees) {
        val listRes = repository.listTrees()
        listRes.data ?: listOf()
    }

    val SquareNavis = MutableLiveData<List<TreeListRes>>()
    fun listNavis() = launchUI(SquareNavis) {
        val listRes = repository.listNavis()
        listRes.data ?: listOf()
    }

    val articleList = MutableLiveData<PlayState>()
    val articleLists = mutableListOf<ArticleBean>()
    var listArticlePage = 0
    fun getlistArticle(isMore: Boolean, id: String) = launchUI(articleList) {
        if (isMore) listArticlePage += 1 else listArticlePage = 0
        val listRes = repository.listArticle(listArticlePage, id)
        if (!isMore) articleLists.clear()
        articleLists.addAll(listRes.data?.datas ?: mutableListOf())
        PlaySuccess(articleLists)
    }

    val listScore = MutableLiveData<MutableList<RankBean>>()
    private val scoreList by lazy { mutableListOf<RankBean>() }
    private var scoreListPage = 1
    fun listScoreRank(isMore: Boolean) = launchUI(listScore) {
        scoreListPage = if (isMore) ++scoreListPage else 1
        val list = RequestService.instance.listScoreRank(scoreListPage)
        scoreList.apply {
            if (!isMore) scoreList.clear()
            addAll(list.data?.datas ?: listOf())
            Log.e("tag", "$isMore    ${scoreList.size}")
        }
    }

    val MyCollectList = MutableLiveData<PlayState>()
    private val MyCollectLists by lazy { mutableListOf<ArticleBean>() }
    private var listMyCollectPage = 0
    fun listMyCollect(isMore: Boolean) = launchUI(MyCollectList) {
        listMyCollectPage = if (isMore) ++listMyCollectPage else 0
        val res = repository.listMyCollect(listMyCollectPage)
        PlaySuccess(MyCollectLists.apply {
            if (!isMore) clear()
            addAll(res.data?.datas ?: mutableListOf())
        })
    }

    val HotkeyList = MutableLiveData<MutableList<SearchHotkeyBean>>()
    fun searchHotkey(hotkey: String) = launchUI(HotkeyList) {
        val res = repository.searchHotkey(hotkey)
        res.data ?: mutableListOf()
    }

    val searchList = MutableLiveData<MutableList<ArticleBean>>()
    fun searchArticle(page: Int, k: String) = launchUI(searchList) {
        val res = repository.searchArticle(page, k)
        res.data?.datas ?: mutableListOf()
    }

}



