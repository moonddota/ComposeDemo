package com.example.composedemo.network.api
import com.example.composedemo.bean.*
import com.example.composedemo.network.BaseData
import retrofit2.http.*


interface RequestServiceRegister {


    /**
     * 项目分类
     */
    @GET("project/tree/json")
    suspend fun listProjectsTab(): BaseData<List<ProjectListRes>>


    /**
     * 获取轮播图
     */
    @GET("banner/json")
    suspend fun getBanner(): BaseData<List<BannerRes>>

    /**
     * 获取文章列表
     * @param page 页码，拼接在连接中，从0开始。
     */
    @GET("article/list/{page}/json")
    suspend fun listArticle(@Path("page") page: Int): BaseData<ArticleListRes>

    /**
     * 知识体系下的文章
     * @param page
     * @param id
     */
    @GET("article/list/{page}/json")
    suspend fun listArticle(
        @Path("page") page: Int,
        @Query("cid") id: String?
    ): BaseData<ArticleListRes>

    /**
     * 取消收藏
     */
    @POST("lg/uncollect_originId/{id}/json")
    suspend fun unCollect(@Path("id") id: String): BaseData<Any>

    /**
     * 收藏文章
     */
    @POST("lg/collect/{id}/json")
    suspend fun collect(@Path("id") id: String): BaseData<Any>

    /**
     * 项目列表
     * @param id   分类id
     * @param page 页码，拼接在连接中，从0开始。
     */
    @GET("project/list/{page}/json")
    suspend fun listProjects(
        @Path("page") page: Int,
        @Query("cid") id: String
    ): BaseData<ArticleListRes>

    /**
     * 获取轮播图
     */
    @GET("tree/json")
    suspend fun listTrees(): BaseData<List<TreeListRes>>

    /**
     * 获取轮播图
     */
    @GET("navi/json")
    suspend fun listNavis(): BaseData<List<TreeListRes>>

    /**
     * 公众号文章列表
     * @param id   公众号id
     * @param page 页码，拼接在连接中，从0开始。
     */
    @GET("wxarticle/list/{id}/{page}/json")
    suspend fun listArticle(
        @Path("id") id: String,
        @Path("page") page: Int
    ): BaseData<ArticleListRes>

    /**
     * 公众号作业列表
     */
    @GET("wxarticle/chapters/json")
    suspend fun listPublicAuthor(): BaseData<List<PublicAuthorListRes>>

    //获取个人积分
    @GET("lg/coin/userinfo/json")
    suspend fun getIntegral(): BaseData<UserInfo>

    @GET("user/logout/json")
    suspend fun logout(): BaseData<Any>

    /**
     * 积分排行榜接口
     */
    @GET("coin/rank/{page}/json")
    suspend fun listScoreRank(@Path("page") page: Int): BaseData<RankListRes>

    /**
     * 获取个人积分列表
     *
     * @param page
     * @return
     */
    @GET("lg/coin/list/{page}/json")
    suspend fun listIntegral(@Path("page") page: Int): BaseData<RankListRes>


    /**
     * 我的收藏列表
     */
    @GET("lg/collect/list/{page}/json")
    suspend fun listMyCollect(@Path("page") page: Int): BaseData<ArticleListRes>

    /**
     * 我的分享
     */
    @GET("user/lg/private_articles/{page}/json")
    suspend fun listMyShare(@Path("page") page: Int): BaseData<MyShareBean>

    /**
     * 删除文章
     *
     * @param id 文章id
     * @return
     */
    @POST("lg/user_article/delete/{id}/json")
    suspend fun deleteArticle(@Path("id") id: String): BaseData<Any>

    /**
     * 分享文章
     */
    @POST("lg/user_article/add/json")
    suspend fun shareArticle(
        @Query("title") title: String?,
        @Query("link") link: String?
    ): BaseData<Any>


    /**
     * 登录
     * @param username 账号
     * @param password 密码
     */
    @FormUrlEncoded
    @POST("user/login")
    suspend fun login(
        @Field("username") username: String?,
        @Field("password") password: String?
    ): BaseData<UserInfo>

    @FormUrlEncoded
    @POST("user/register")
    suspend fun register(
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("repassword") repassword: String
    ): BaseData<UserInfo>


}