package com.example.fetch_assignment.presentation

import com.example.fetch_assignment.domain.ListItem

data class ListState(
    val currentList : List<ListItem> = emptyList()
)
