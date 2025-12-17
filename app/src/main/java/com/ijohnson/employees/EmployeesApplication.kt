package com.ijohnson.employees

import android.app.Application
import com.ijohnson.employees.repo.RepoDi
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class EmployeesApplication: Application() {
    init {
        RepoDi.applicationContext = this
    }
}