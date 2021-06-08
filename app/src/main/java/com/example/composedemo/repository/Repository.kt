package com.example.composedemo.repository

import com.example.composedemo.bean.*
import com.example.composedemo.network.BaseData
import com.example.composedemo.network.BaseRepository
import com.example.composedemo.network.api.RequestService

class Repository : BaseRepository() {

    suspend fun getBanner(): BaseData<List<BannerRes>> = request {
        RequestService.instance.getBanner()
    }

    suspend fun listArticle(page: Int): BaseData<ArticleListRes> = request {
        RequestService.instance.listArticle(page)
    }

    suspend fun listArticle(page: Int, id: String): BaseData<ArticleListRes> = request {
        RequestService.instance.listArticle(page, id)
    }

    suspend fun unCollect(id: String): BaseData<Any> = request {
        RequestService.instance.unCollect(id)
    }


    suspend fun collect(id: String): BaseData<Any> = request {
        RequestService.instance.collect(id)
    }

    suspend fun getListProjects(page: Int, id: String): BaseData<ArticleListRes> = request {
        RequestService.instance.listProjects(page, id)
    }

    suspend fun listProjectsTab(): BaseData<List<ProjectListRes>> = request {
        RequestService.instance.listProjectsTab()
    }


    /**
     * 获取收藏列表
     *
     * @param page 页码
     */
    suspend fun getCollectList(page: Int): BaseData<ArticleListRes> = request {
        RequestService.instance.listMyCollect(page)
    }

    suspend fun cancelCollects(id: String): BaseData<Any> = request {
        RequestService.instance.unCollect(id)
    }

    suspend fun toCollects(id: String): BaseData<Any> = request {
        RequestService.instance.collect(id)
    }

    suspend fun getIntegral(): BaseData<UserInfo> = request {
        RequestService.instance.getIntegral()
    }

    suspend fun login(username: String, password: String): BaseData<UserInfo> = request {
        RequestService.instance.login(username, password)
    }

    suspend fun register(
        username: String,
        password: String,
        repassword: String
    ): BaseData<UserInfo> = request {
        RequestService.instance.register(username, password, repassword)
    }


    suspend fun logout(): BaseData<Any> = request {
        RequestService.instance.logout()
    }

    suspend fun listIntegral(page: Int): BaseData<RankListRes> = request {
        RequestService.instance.listIntegral(page)
    }

    suspend fun listTrees(): BaseData<List<TreeListRes>> = request {
        RequestService.instance.listTrees()
    }

    suspend fun listNavis(): BaseData<List<TreeListRes>> = request {
        RequestService.instance.listNavis()
    }

    suspend fun listScoreRank(page: Int): BaseData<RankListRes> = request {
        RequestService.instance.listScoreRank(page)
    }

    suspend fun listMyCollect(page: Int): BaseData<ArticleListRes> = request {
        RequestService.instance.listMyCollect(page)
    }

    suspend fun searchHotkey(hotkey: String): BaseData<MutableList<SearchHotkeyBean>> = request {
        RequestService.instance.searchHotkey(hotkey)
    }

    suspend fun searchArticle(page: Int, k: String): BaseData<ArticleListRes> = request {
        RequestService.instance.searchArticle(page, k)
    }


}