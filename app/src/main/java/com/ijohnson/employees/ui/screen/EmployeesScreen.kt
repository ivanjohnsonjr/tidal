package com.ijohnson.employees.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ijohnson.employees.ui.screen.EmployeesViewModel.UIState.UiEmployee
import com.ijohnson.employees.ui.theme.EmployeesTheme

@Composable
fun EmployeesScreen(
    modifier: Modifier = Modifier,
    vm: EmployeesViewModel = viewModel()
) {
    val state by vm.uiState.collectAsStateWithLifecycle()

    InternalEmployeeScreen(
        modifier = modifier,
        employees = state.employees,
        isLoading = state.isLoading,
        errorMessage = state.errorMessage,
        onRefresh = { vm.refresh() }
    )

    //Fetch the content when the Page get render
    LaunchedEffect(null) {
        vm.refresh()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun InternalEmployeeScreen(
    employees: List<UiEmployee>,
    isLoading: Boolean,
    errorMessage: String?,
    modifier: Modifier = Modifier,
    onRefresh: () -> Unit
) {
    PullToRefreshBox(
        modifier = modifier.fillMaxSize(),
        isRefreshing = isLoading,
        onRefresh = { onRefresh() }
    ) {

        LazyColumn {
            errorMessage?.let {
                item {
                    ErrorView(errorMessage)
                }
            }

            items(employees) {
                EmployeeView(it)
            }
        }
    }
}

@Composable
private fun EmployeeView(
    employee: UiEmployee,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
    ) {
        Column {
            Text(
                text = employee.fullName
            )
        }
    }
}

@Composable
private fun ErrorView(
    text: String,
    modifier: Modifier = Modifier
) {
    Row (
        modifier = modifier.padding(8.dp).border(
            width = 2.dp, Color.Red, shape = RoundedCornerShape(8.dp)
        )
    ) {
        Image(Icons.Filled.Notifications, "", colorFilter = ColorFilter.tint(Color.Red))
        Text(
            modifier = Modifier.padding(start = 8.dp),
            text = text
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    EmployeesTheme {
        ErrorView("Android Error")
    }
}