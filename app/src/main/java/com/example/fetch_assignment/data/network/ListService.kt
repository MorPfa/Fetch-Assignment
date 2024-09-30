package com.example.fetch_assignment.data.network

import retrofit2.Response
import retrofit2.http.GET

interface ListService {

    @GET("hiring.json")
    suspend fun loadList() : Response<List<ListItemDto>>
}