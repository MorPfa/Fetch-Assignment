package com.example.fetch_assignment.domain

import com.example.fetch_assignment.domain.util.DataError
import com.example.fetch_assignment.domain.util.Result

interface ListRepository {

    suspend fun loadList() : Result<List<ListItem>, DataError.Network>
}