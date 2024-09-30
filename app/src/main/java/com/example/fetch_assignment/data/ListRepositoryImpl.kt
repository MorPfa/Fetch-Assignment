package com.example.fetch_assignment.data

import com.example.fetch_assignment.data.network.ListItemDto
import com.example.fetch_assignment.data.network.ListService
import com.example.fetch_assignment.data.network.toListItem
import com.example.fetch_assignment.domain.ListItem
import com.example.fetch_assignment.domain.ListRepository
import com.example.fetch_assignment.domain.util.DataError
import com.example.fetch_assignment.domain.util.Result
import com.google.gson.JsonParseException
import java.io.IOException
import java.net.SocketTimeoutException

class ListRepositoryImpl(private val listService: ListService) : ListRepository {

    override suspend fun loadList(): Result<List<ListItem>, DataError.Network> {
        return try {
            val response = listService.loadList()
            if (response.isSuccessful) {
                val responseBody = response.body()
                if (responseBody != null) {
                    val formattedListItems = responseBody
                        .filter { !it.name.isNullOrEmpty() }
                        .sortedWith(compareBy<ListItemDto> { it.listId }.thenBy { it.name })
                        .map { listItemDto -> listItemDto.toListItem() }
                    Result.Success(formattedListItems)
                } else {
                    Result.Error(DataError.Network.EMPTY_RESPONSE_BODY)
                }
            } else {
                when (response.code()) {
                    400 -> Result.Error(DataError.Network.BAD_REQUEST)
                    401 -> Result.Error(DataError.Network.UNAUTHORIZED)
                    403 -> Result.Error(DataError.Network.FORBIDDEN)
                    404 -> Result.Error(DataError.Network.RESOURCE_NOT_FOUND)
                    429 -> Result.Error(DataError.Network.TOO_MANY_REQUESTS)
                    in 500..599 -> Result.Error(DataError.Network.SERVER_ERROR)
                    else -> Result.Error(DataError.Network.UNKNOWN)
                }
            }
        } catch (e: IOException) {
            Result.Error(DataError.Network.NETWORK_FAILURE)
        } catch (e: JsonParseException) {
            Result.Error(DataError.Network.PARSING_ERROR)
        } catch (e: SocketTimeoutException) {
            Result.Error(DataError.Network.REQUEST_TIMEOUT)
        } catch (e: Exception) {
            Result.Error(DataError.Network.UNKNOWN)
        }
    }
}