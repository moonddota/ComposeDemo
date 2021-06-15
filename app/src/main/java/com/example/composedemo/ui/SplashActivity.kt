package com.example.composedemo.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.ExperimentalPagingApi
import com.example.composedemo.viewmodel.SplashViewModel

class SplashActivity : ComponentActivity() {

    @ExperimentalPagingApi
    @ExperimentalFoundationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SplashContent {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }

    }
}

@Composable
private fun SplashContent(jumpMainActivity: () -> Unit) {
    val viewModel: SplashViewModel = viewModel()
    viewModel.listProjectsTab(jumpMainActivity)
}