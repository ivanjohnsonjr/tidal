package com.ijohnson.employees.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ijohnson.employees.repo.EmployeeRepository
import com.ijohnson.employees.repo.data.Employee
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

private val Employee.asUiEmployee: EmployeesViewModel.UIState.UiEmployee
    get() {
        return EmployeesViewModel.UIState.UiEmployee(
            this
        )
    }

/**
 * EmployeesViewModel
 *
 * ViewModel for displaying employee information
 */
@HiltViewModel
class EmployeesViewModel @Inject constructor(
    private val repo: EmployeeRepository
) : ViewModel() {

    companion object {
        const val DEBOUNCE = 500L
    }

    data class UIState(
        val employees: List<UiEmployee> = emptyList(),
        val errorMessage: String? = null,
        val isLoading: Boolean = true
    ) {
        data class UiEmployee(
            val data: Employee,
            val showDetail: Boolean = false
        )
    }

    private val _uiState = MutableStateFlow(UIState())
    val uiState = _uiState.asStateFlow()

    private val _refreshAction = MutableStateFlow(false)
    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    private val _employeesDataFlow = _refreshAction
        .debounce(DEBOUNCE)
        .flatMapLatest {
            //Update loading state
            _uiState.update {
                it.copy(
                    isLoading = false
                )
            }

            //Fetch employee information
            load(it)
        }
        .onEach { result ->
            _uiState.update {
                it.copy(
                    employees = result,
                    isLoading = false
                )
            }
        }
        .catch { ex ->
            _uiState.update {
                it.copy(
                    errorMessage = ex.message,
                    isLoading = false
                )
            }
        }

    init {
        viewModelScope.launch {
            _employeesDataFlow.collect()
        }
    }

    /**
     * Internal Fetch Employee information
     */
    private suspend fun load(
        forceRefresh: Boolean
    ): Flow<List<UIState.UiEmployee>> {
        return repo.getEmployees(forceRefresh).map {
            it.map { emp -> emp.asUiEmployee }
        }.flowOn(Dispatchers.IO)
    }

    /**
     * Refresh screen info
     */
    fun refresh() {
        _refreshAction.update { true }
    }
}