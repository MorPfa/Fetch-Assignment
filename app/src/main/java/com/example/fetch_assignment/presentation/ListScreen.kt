package com.example.fetch_assignment.presentation

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fetch_assignment.domain.ListItem
import com.example.fetch_assignment.presentation.ui.theme.FetchAssignmentTheme
import com.example.fetch_assignment.presentation.util.ObserveAsEvents
import org.koin.androidx.compose.koinViewModel


@Composable
fun ListScreenRoot(viewModel: ListViewModel = koinViewModel()) {
    val context = LocalContext.current
    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is ListEvent.LoadError -> {
                Toast.makeText(
                    context,
                    "Could not load list",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
    ListScreen(
        state = viewModel.state,
        onRefresh = { viewModel.onAction(ListAction.OnLoadList) }
    )
}


@Composable
fun ListScreen(
    state: ListState,
    onRefresh: () -> Unit,
) {
    Column(
        modifier = Modifier
            .padding(18.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.Start
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            IconButton(
                onClick = { onRefresh() },
            ) {
                Icon(
                    modifier = Modifier.size(48.dp),
                    imageVector = if (state.currentList.isEmpty()) {
                        Icons.Filled.Add
                    } else {
                        Icons.Default.Refresh
                    },
                    contentDescription = "Refresh"
                )
            }
            Text(
                text = "Load List Items", fontSize = 28.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
        val groupedItems = state.currentList.groupBy { it.listId }
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            groupedItems.forEach { (listId, items) ->
                item {
                    Text(
                        text = "List ID: $listId",
                        fontSize = 26.sp,
                        color = Color.Black,
                        modifier = Modifier.padding(vertical = 12.dp)
                    )
                }
                items(items = items, key = { it.id }) {
                    ListItem(item = it)
                }
            }
        }
    }

}

@Composable
fun ListItem(item: ListItem) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .border(
                width = 1.dp,
                color = Color.Gray,
                shape = RoundedCornerShape(8.dp)
            )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Text(
                text = item.id,
                fontSize = 18.sp,
                color = Color.Black,
                modifier = Modifier
                    .padding(vertical = 8.dp, horizontal = 20.dp)
                    .weight(1f)
            )
            Text(
                text = item.listId,
                fontSize = 18.sp,
                color = Color.Black,
                modifier = Modifier
                    .padding(vertical = 8.dp, horizontal = 20.dp)
                    .weight(1f)
            )
            Text(
                text = item.name,
                fontSize = 18.sp,
                color = Color.Black,
                modifier = Modifier
                    .padding(vertical = 8.dp, horizontal = 20.dp)
                    .weight(1f)
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun ListScreenPreview() {
    FetchAssignmentTheme {
        ListScreen(
            state = ListState(),
            onRefresh = {}
        )
    }
}