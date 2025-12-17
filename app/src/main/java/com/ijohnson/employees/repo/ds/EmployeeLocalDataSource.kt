package com.ijohnson.employees.repo.ds

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Insert
import androidx.room.Query
import androidx.room.RoomDatabase
import androidx.room.Transaction
import com.ijohnson.employees.repo.data.ApiEmployee

@Database(entities = [ApiEmployee::class], version = 1)
abstract class EmployeeDatabase : RoomDatabase() {
    abstract fun employeeDao(): EmployeeDao
}

/**
 * Employee Dao interface
 */
@Dao
interface EmployeeDao {

    @Query("SELECT * FROM Employee")
    suspend fun getAll(): List<ApiEmployee>

    @Transaction
    @Insert
    suspend fun clearAndInsertAll(vararg employees: ApiEmployee) {
        deleteAll()
        insert(*employees)
    }

    @Insert
    suspend fun insert(vararg employees: ApiEmployee)

    @Query("DELETE from Employee")
    suspend fun deleteAll()
}

class EmployeeLocalDataSource(
    private val employeeDao: EmployeeDao
) {

    suspend fun insert(vararg employees: ApiEmployee): Int {
        employeeDao.clearAndInsertAll(*employees)
        return employees.size
    }

    suspend fun getAll(): List<ApiEmployee> {
        return employeeDao.getAll()
    }
}