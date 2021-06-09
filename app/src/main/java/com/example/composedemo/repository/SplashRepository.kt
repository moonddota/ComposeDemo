package com.example.composedemo.repository

import com.example.composedemo.bean.ProjectListRes
import com.example.composedemo.network.BaseData
import com.example.composedemo.network.BaseRepository
import com.example.composedemo.network.api.RequestService

class SplashRepository : BaseRepository() {

    suspend fun listProjectsTab(): BaseData<List<ProjectListRes>> = request {
        RequestService.instance.listProjectsTab()
    }
}