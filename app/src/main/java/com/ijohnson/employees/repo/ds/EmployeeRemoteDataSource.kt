package com.ijohnson.employees.repo.ds

import com.google.gson.annotations.SerializedName
import com.ijohnson.employees.repo.data.ApiEmployee
import retrofit2.Call
import retrofit2.http.GET
import java.io.IOException

data class ApiEmployeeResult(
    @SerializedName("employees")
    val employees: List<ApiEmployee>
)

/**
 * Retrofit interface for Employee Api
 */
interface EmployeeApi {

    @GET("/sq-mobile-interview/employees.json")
    fun employees(): Call<ApiEmployeeResult>
}

/**
 * EmployeeRemoteDataSource
 *
 * DataSource for fetching employee information from backend api
 */
class EmployeeRemoteDataSource(
    private val api: EmployeeApi
) {

    /**
     * Function for fetching employee info
     */
    fun getEmployee(): List<ApiEmployee> {
        val response = api.employees().execute()

        return if(response.isSuccessful) {
            response.body()!!.employees
        } else {
            throw IOException(response.message())
        }
    }

}