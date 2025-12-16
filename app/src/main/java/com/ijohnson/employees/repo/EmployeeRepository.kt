package com.ijohnson.employees.repo

import com.ijohnson.employees.repo.data.Employee
import com.ijohnson.employees.repo.ds.ApiEmployee
import com.ijohnson.employees.repo.ds.EmployeeRemoteDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Private method to convert ApiEmployee to Employee to be returned to repository consumer
 */
private val ApiEmployee.toEmployee: Employee
    get() {
        return Employee(
            id = this.id,
            fullName = this.fullName,
            phoneNumber = this.phoneNumber,
            email = this.email,
            biography = this.biography,
            photo = this.photo
        )
    }

/**
 * EmployeeRepository
 *
 * Employee repository
 */
interface EmployeeRepository {
    /**
     * Get a list of Employees
     */
    suspend fun getEmployees(): Flow<List<Employee>>
}

internal class EmployeeRepositoryImpl(
    private val remoteDS: EmployeeRemoteDataSource
): EmployeeRepository {

    override suspend fun getEmployees(): Flow<List<Employee>> {
        return flow {
            val apiEmployees = remoteDS.getEmployee()
            val employees = apiEmployees.map { it.toEmployee }

            emit(employees)
        }
    }

}