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
import com.example.composedemo.ui.MainDestinations.ARTICLE_ROUTE_URL
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
    const val SCORE_RANK_LIST_PAGE = "Score_Rank_List_Page"
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
            LoginPage(modifier, actions, myViewModel)
        }
        composable(MainDestinations.SCORE_RANK_LIST_PAGE) {
            ScoreRankListPage(modifier, actions, myViewModel)
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
    val jumpScoreRankListPage: () -> Unit = {
        navigate(navController, MainDestinations.SCORE_RANK_LIST_PAGE)
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
