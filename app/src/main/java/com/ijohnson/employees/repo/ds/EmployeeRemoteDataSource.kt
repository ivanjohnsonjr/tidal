package com.ijohnson.employees.repo.ds

import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.http.GET
import java.io.IOException

data class ApiEmployee(
    @SerializedName("uuid")
    val id: String,
    @SerializedName("full_name")
    val fullName: String,
    @SerializedName("phone_number")
    val phoneNumber: String,
    @SerializedName("email_address")
    val email: String,
    val biography: String,
    @SerializedName("photo_url_small")
    val photo: String,
    val team: String,
    @SerializedName("employee_type")
    val employeeType: String
)

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