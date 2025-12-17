package com.ijohnson.employees.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement.Absolute.spacedBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.ijohnson.employees.R
import com.ijohnson.employees.repo.data.Employee
import com.ijohnson.employees.ui.screen.EmployeesViewModel.UIState.UiEmployee
import com.ijohnson.employees.ui.theme.Dimen
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

        LazyColumn(
            modifier = Modifier.padding(horizontal = Dimen.unitx2),
            verticalArrangement = spacedBy(Dimen.unitx2)
        ) {
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
    ElevatedCard(
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(Dimen.unit)
        ) {
            AsyncImage(
                modifier = Modifier
                    .defaultMinSize(
                        minHeight = Dimen.imageMinDimension,
                        minWidth = Dimen.imageMinDimension
                    ),
                model = ImageRequest.Builder(LocalContext.current)
                    .data(employee.data.photo)
                    .crossfade(true)
                    .build(),
                placeholder = rememberVectorPainter(Icons.Filled.Person),
                contentDescription = stringResource(
                    R.string.employee_photo_desc,
                    employee.data.fullName
                ),
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier.padding(start = Dimen.unitx2),
                verticalArrangement = spacedBy(Dimen.unit)
            ) {
                Text(
                    text = employee.data.fullName,
                    style = MaterialTheme.typography.titleLarge
                )

                Text(
                    text = stringResource(R.string.email_prepend, employee.data.email),
                    style = MaterialTheme.typography.bodySmall
                )
            }
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

        EmployeeView(
            modifier = Modifier.padding(top = 8.dp),
            employee = UiEmployee(
                data = Employee(
                    id = "employee:id",
                    fullName = "John Doe",
                    phoneNumber = "(555) 555-5555",
                    email = "johndoe@unknown.com",
                    biography = "I am a person that doesn't know who I am.",
                    photo = "http://myphoto.com"
                )
            )
        )
    }
}