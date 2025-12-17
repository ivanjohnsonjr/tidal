package com.ijohnson.employees.repo.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

data class Employee(
    val id: String,
    val fullName: String,
    val phoneNumber: String,
    val email: String,
    val biography: String,
    val photo: String
)

//@Entity("employee")
//data class DaoEmployee(
//    @PrimaryKey val id: String,
//    @ColumnInfo(name = "full_name") val fullName: String,
//    @ColumnInfo(name = "phone_number") val phoneNumber: String,
//    @ColumnInfo(name = "email") val email: String,
//    @ColumnInfo(name = "biography") val biography: String,
//    @ColumnInfo(name = "photo") val photo: String
//)

@Entity("employee")
data class ApiEmployee(
    @SerializedName("uuid")
    @PrimaryKey
    val id: String,

    @SerializedName("full_name")
    @ColumnInfo(name = "full_name")
    val fullName: String,

    @SerializedName("phone_number")
    @ColumnInfo(name = "phone_number")
    val phoneNumber: String,

    @SerializedName("email_address")
    @ColumnInfo(name = "email")
    val email: String,

    @ColumnInfo(name = "biography")
    val biography: String,

    @SerializedName("photo_url_small")
    @ColumnInfo(name = "photo")
    val photo: String,

    val team: String,

    @SerializedName("employee_type")
    val employeeType: String
)