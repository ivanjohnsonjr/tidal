package com.ijohnson.employees.repo

import com.google.gson.GsonBuilder
import com.ijohnson.employees.repo.ds.EmployeeApi
import com.ijohnson.employees.repo.ds.EmployeeRemoteDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

@Module
@InstallIn(SingletonComponent::class)
object RepoDi {
    private val BASE_URL = "https://s3.amazonaws.com"

    private val retroFitBuilder: Retrofit
        get() {
            val interceptor = HttpLoggingInterceptor().apply {
                this.level = HttpLoggingInterceptor.Level.BODY
            }

            val client = OkHttpClient.Builder().apply {
                this.addInterceptor(interceptor)
                    // time out setting
                    .connectTimeout(3, TimeUnit.SECONDS)
                    .readTimeout(20,TimeUnit.SECONDS)
                    .writeTimeout(25,TimeUnit.SECONDS)

            }.build()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
                .build()
        }

    @Provides
    fun provideRemoteEmployeeDataSource(): EmployeeApi {
        return retroFitBuilder.create(EmployeeApi::class.java)
    }

    @Provides
    fun provideEmployeeRepository(
        api: EmployeeApi
    ): EmployeeRepository {
        val ds = EmployeeRemoteDataSource(api)

        return EmployeeRepositoryImpl(ds)
    }
}