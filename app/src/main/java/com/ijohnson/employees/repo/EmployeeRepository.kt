package com.ijohnson.employees.repo

import com.ijohnson.employees.repo.data.ApiEmployee
import com.ijohnson.employees.repo.data.Employee
import com.ijohnson.employees.repo.ds.EmployeeRemoteDataSource
import com.ijohnson.employees.repo.ds.EmployeeLocalDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Private method to convert ApiEmployee to Employee to be returned to repository consumer
 */
private val ApiEmployee.toEmployee: Employee
    get() = Employee(
        id = this.id,
        fullName = this.fullName,
        phoneNumber = this.phoneNumber,
        email = this.email,
        biography = this.biography,
        photo = this.photo
    )

/**
 * EmployeeRepository
 *
 * Employee repository
 */
interface EmployeeRepository {
    /**
     * Get a list of Employees
     */
    suspend fun getEmployees(
        refresh: Boolean
    ): Flow<List<Employee>>
}

internal class EmployeeRepositoryImpl(
    private val remoteDS: EmployeeRemoteDataSource,
    private val localDS: EmployeeLocalDataSource
): EmployeeRepository {

    override suspend fun getEmployees(
        refresh: Boolean
    ): Flow<List<Employee>> {
        return flow {
            var employees: List<ApiEmployee> = localDS.getAll()
            val hasNoData = employees.isEmpty()

            if (refresh || hasNoData) {
                val apiEmployees = remoteDS.getEmployee()
                localDS.insert(*apiEmployees.toTypedArray())
                employees = localDS.getAll()
            }

            emit(employees.map { it.toEmployee })
        }
    }

}