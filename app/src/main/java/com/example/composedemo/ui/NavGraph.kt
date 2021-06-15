package com.example.composedemo.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.paging.ExperimentalPagingApi
import com.example.composedemo.R
import com.example.composedemo.bean.ArticleBean
import com.example.composedemo.common.article.ArticlePage
import com.example.composedemo.ui.MainDestinations.ARTICLE_LIST_ID
import com.example.composedemo.ui.MainDestinations.ARTICLE_LIST_TITLE
import com.example.composedemo.ui.MainDestinations.ARTICLE_ROUTE_URL
import com.example.composedemo.ui.page.*
import com.example.composedemo.util.getHtmlText
import com.example.composedemo.viewmodel.MyViewModel
import com.google.gson.Gson
import java.net.URLEncoder

object MainDestinations {
    const val HOME_PAGE_ROUTE = "home_page_route"
    const val ARTICLE_ROUTE = "article_route"
    const val ARTICLE_ROUTE_URL = "article_route_url"
    const val SETTING_PAGE = "setting_page"
    const val LOGIN_PAGE = "login_page"
    const val MYSCORE_PAGE = "myscore_page"
    const val ARTICLE_LIST = "article_list"
    const val ARTICLE_LIST_ID = "article_list_id"
    const val ARTICLE_LIST_TITLE = "article_list_title"
    const val SCORE_RANK_LIST = "score_rank_list"
    const val MY_COLLECT_PAGE = "my_collect_page"
    const val OPEN_SOURCE_PAGE = "open_source_page"
    const val SEARCH_PAGE = "search_page"
    const val DRAW_PAGE = "draw_page"
}

@ExperimentalFoundationApi
@ExperimentalPagingApi
@Composable
fun NavGraph(
    startDestination: String = MainDestinations.HOME_PAGE_ROUTE,
    modifier: Modifier
) {
    val myViewModel: MyViewModel = viewModel()

    val navController = rememberNavController()

    val actions = remember(navController) { MainActions(navController) }
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(MainDestinations.HOME_PAGE_ROUTE) {
            Home(modifier, actions, myViewModel)
        }
        composable(
            "${MainDestinations.ARTICLE_ROUTE}/{$ARTICLE_ROUTE_URL}",
            arguments = listOf(navArgument(ARTICLE_ROUTE_URL) {
                type = NavType.StringType
            })
        ) { backStackEntry ->
            val arguments = requireNotNull(backStackEntry.arguments)
            val parcelable = arguments.getString(ARTICLE_ROUTE_URL)
            val fromJson = Gson().fromJson(parcelable, ArticleBean::class.java)
            ArticlePage(
                modifier = modifier,
                article = fromJson,
                viewModel = myViewModel,
                onBack = actions.upPress
            )
        }
        composable(MainDestinations.SETTING_PAGE) {
            SettingPage(modifier, actions, myViewModel)
        }
        composable(MainDestinations.LOGIN_PAGE) {
            myViewModel.showLoging.postValue(true)
            LoginPage(modifier, actions, myViewModel)
        }
        composable(MainDestinations.MYSCORE_PAGE) {
            MyScorePage(modifier, actions, myViewModel)
        }
        composable(
            "${MainDestinations.ARTICLE_LIST}/{$ARTICLE_LIST_ID}/{$ARTICLE_LIST_TITLE}",
            arguments = listOf(
                navArgument(ARTICLE_LIST_ID) {
                    type = NavType.StringType
                },
                navArgument(ARTICLE_LIST_TITLE) {
                    type = NavType.StringType
                })
        ) { backStackEntry ->
            val arguments = requireNotNull(backStackEntry.arguments)
            val id = arguments.getString(ARTICLE_LIST_ID) ?: ""
            val title = arguments.getString(ARTICLE_LIST_TITLE) ?: ""
            ArticleListPage(
                modifier = modifier,
                id = id,
                title = title,
                actions = actions,
                myViewModel = myViewModel
            )
        }
        composable(MainDestinations.SCORE_RANK_LIST) {
            ScoreRankListPage(modifier, actions, myViewModel)
        }
        composable(MainDestinations.MY_COLLECT_PAGE) {
            MyCollectPage(modifier, actions, myViewModel)
        }
        composable(MainDestinations.OPEN_SOURCE_PAGE) {
            OpenSourcePage(modifier, actions, myViewModel)
        }
        composable(MainDestinations.SEARCH_PAGE) {
            SearchPage(modifier, actions, myViewModel)
        }
        composable(MainDestinations.DRAW_PAGE) {
            DrawPage(modifier, actions, myViewModel)
        }
    }
}

class MainActions(navController: NavHostController) {

    val homePage: () -> Unit = {
        navigate(navController, MainDestinations.HOME_PAGE_ROUTE)
    }
    val enterArticle: (ArticleBean) -> Unit = { article ->
        article.desc = ""
        article.title = getHtmlText(article.title ?: "")
        val gson = Gson().toJson(article).trim()
        val result = URLEncoder.encode(gson, "utf-8")
        navigate(navController, "${MainDestinations.ARTICLE_ROUTE}/$result")
    }
    val jumpSetting: () -> Unit = {
        navigate(navController, MainDestinations.SETTING_PAGE)
    }
    val jumpLogin: () -> Unit = {
        navigate(navController, MainDestinations.LOGIN_PAGE)
    }
    val upPress: () -> Unit = {
        navController.navigateUp()
    }
    val loginOut: () -> Unit = {
        navController.navigateUp()
        jumpLogin()
    }
    val jumpMyScorePage: () -> Unit = {
        navigate(navController, MainDestinations.MYSCORE_PAGE)
    }
    val jumpArticleList: (id: String, title: String) -> Unit = { id, title ->
        navigate(navController, "${MainDestinations.ARTICLE_LIST}/$id/$title")
    }
    val jumpScoreRankListPage: () -> Unit = {
        navigate(navController, MainDestinations.SCORE_RANK_LIST)
    }
    val jumpMyCollectPage: () -> Unit = {
        navigate(navController, MainDestinations.MY_COLLECT_PAGE)
    }
    val jumpOpenSourcePage: () -> Unit = {
        navigate(navController, MainDestinations.OPEN_SOURCE_PAGE)
    }
    val jumpSearchPage: () -> Unit = {
        navigate(navController, MainDestinations.SEARCH_PAGE)
    }
    val jumpDrawPageL: () -> Unit = {
        navigate(navController, MainDestinations.DRAW_PAGE)
    }

    private fun navigate(navController: NavHostController, route: String) {
        navController.navigate(route) {
            anim {
                enter = R.anim.in_from_right
                exit = R.anim.out_to_left
                popEnter = R.anim.in_from_right
                popExit = R.anim.out_to_left
            }
        }
    }

}
