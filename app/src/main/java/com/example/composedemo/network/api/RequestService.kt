package com.example.composedemo.network.api

import com.example.composedemo.bean.*
import com.example.composedemo.network.BaseData
import com.example.composedemo.network.RetrofitImpl
import retrofit2.http.Path
import retrofit2.http.Query


class RequestService {


    private var requestService: RequestServiceRegister =
        RetrofitImpl.instance.getRetrofit().create(
            RequestServiceRegister::class.java
        )

    companion object {
        val instance: RequestService by
        lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) { RequestService() }
    }


    suspend fun listProjectsTab(): BaseData<List<ProjectListRes>> =
        requestService.listProjectsTab()

    suspend fun getBanner(): BaseData<List<BannerRes>> =
        requestService.getBanner()

    suspend fun listArticle(page: Int): BaseData<ArticleListRes> =
        requestService.listArticle(page)

    suspend fun listArticle(page: Int, id: String): BaseData<ArticleListRes> =
        requestService.listArticle(page, id)

    suspend fun unCollect(id: String): BaseData<Any> =
        requestService.unCollect(id)

    suspend fun collect(id: String): BaseData<Any> =
        requestService.collect(id)

    suspend fun listProjects(page: Int, id: String): BaseData<ArticleListRes> =
        requestService.listProjects(page, id)

    suspend fun listTrees(): BaseData<List<TreeListRes>> =
        requestService.listTrees()

    suspend fun listNavis(): BaseData<List<TreeListRes>> =
        requestService.listNavis()

    suspend fun listArticle(id: String, page: Int): BaseData<ArticleListRes> =
        requestService.listArticle(id, page)

    suspend fun listPublicAuthor(): BaseData<List<PublicAuthorListRes>> =
        requestService.listPublicAuthor()

    suspend fun getIntegral(): BaseData<UserInfo> =
        requestService.getIntegral()

    suspend fun logout(): BaseData<Any> =
        requestService.logout()

    suspend fun listScoreRank(page: Int): BaseData<RankListRes> =
        requestService.listScoreRank(page)

    suspend fun listIntegral(page: Int): BaseData<RankListRes> =
        requestService.listIntegral(page)

    suspend fun listMyCollect(page: Int): BaseData<ArticleListRes> =
        requestService.listMyCollect(page)

    suspend fun listMyShare(page: Int): BaseData<MyShareBean> =
        requestService.listMyShare(page)

    suspend fun deleteArticle(id: String): BaseData<Any> =
        requestService.deleteArticle(id)

    suspend fun shareArticle(title: String?, link: String?): BaseData<Any> =
        requestService.shareArticle(title, link)

    suspend fun login(username: String, password: String): BaseData<UserInfo> =
        requestService.login(username, password)

    suspend fun register(
        username: String,
        password: String,
        repassword: String
    ): BaseData<UserInfo> =
        requestService.register(username, password, repassword)

    suspend fun searchHotkey(hotkey: String): BaseData<MutableList<SearchHotkeyBean>> =
        requestService.searchHotkey(hotkey)


    suspend fun searchArticle(page: Int, k: String): BaseData<ArticleListRes> =
        requestService.searchArticle(page, k)

}