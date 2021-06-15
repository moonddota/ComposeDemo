package com.example.composedemo.viewmodel

import android.annotation.SuppressLint
import android.util.Pair
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.composedemo.bean.*
import com.example.composedemo.model.PlayError
import com.example.composedemo.model.PlayState
import com.example.composedemo.model.PlaySuccess
import com.example.composedemo.network.api.RequestService
import com.example.composedemo.repository.Repository
import com.example.composedemo.ui.CourseTabs
import com.example.composedemo.ui.MainActions
import com.example.composedemo.util.CacheUtil
import com.example.composedemo.util.MMkvHelper

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

    val homeListData = MutableLiveData<Pair<Boolean, MutableList<ArticleBean>>>()
    fun listArticle(isLoadMore: Boolean, page: Int) = launchUI(homeListData) {
        val res = repository.listArticle(page)
        Pair(isLoadMore, res.data?.datas ?: mutableListOf())
    }

    val projectTabs = MutableLiveData<PlayState>(PlaySuccess(listOf<ProjectListRes>()))
    fun getProjectTabs() = launchUI(projectTabs) {
        var projectTabs: List<ProjectListRes> =
            MMkvHelper.getInstance().getProjectTabs(ProjectListRes::class.java)
        if (projectTabs.isNullOrEmpty()) {
            val listRes = repository.listProjectsTab()
            if (listRes.data?.isNullOrEmpty() == false)
                MMkvHelper.getInstance().saveProjectTabs(listRes.data)
            projectTabs = listRes.data ?: listOf()
        }
        if (projectTabs.isNullOrEmpty() == false)
            getListProjects(projectTabs[0].id ?: "", 0, false)
        PlaySuccess(projectTabs)
    }

    val projectsListData = MutableLiveData<Pair<Boolean, MutableList<ArticleBean>>>()
    fun getListProjects(id: String, page: Int, isLoadMore: Boolean) = launchUI(projectsListData) {
        val res = repository.getListProjects(page, id)
        Pair(isLoadMore, res.data?.datas ?: mutableListOf())
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

    val userInfo = MutableLiveData<UserInfo?>()
    fun getIntegral() = launchUI(userInfo) {
        val res = repository.getIntegral()
        MMkvHelper.getInstance().saveUserInfo(res.data)
        res.data
    }

    val showLoging = MutableLiveData<Boolean>()

    fun login(actions: MainActions, username: String, password: String) = launch(null, {}, {
        val res = repository.login(username, password)
        userInfo.postValue(res.data)
    }, {
        actions.upPress()
    })

    fun register(actions: MainActions, username: String, password: String, repassword: String) =
        launch(null, {}, {
            val res = repository.register(username, password, repassword)
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

    val listIntegralData = MutableLiveData<Pair<Boolean, RankListRes?>>()
    fun listIntegral(isMore: Boolean, page: Int) = launchUI(listIntegralData) {
        val res = repository.listIntegral(page)
        Pair(isMore, res.data)
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

    val listScore = MutableLiveData<Pair<Boolean, List<RankBean>>>()
    fun listScoreRank(isMore: Boolean, page: Int) = launchUI(listScore) {
        val list = RequestService.instance.listScoreRank(page)
        Pair(isMore, list.data?.datas ?: listOf())
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



