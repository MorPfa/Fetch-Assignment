package com.example.fetch_assignment.data.network

import com.example.fetch_assignment.domain.ListItem
import com.google.gson.annotations.SerializedName


data class ListItemDto(
    @SerializedName("id")
    val id : String,
    @SerializedName("listId")
    val listId : String,
    @SerializedName("name")
    val name : String?
)

fun ListItemDto.toListItem() : ListItem {
    return ListItem(
        id = id,
        listId = listId,
        name = name!!
    )
}
