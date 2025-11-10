package ru.ushell.app.di.retrofit

import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.ushell.app.R
import ru.ushell.app.data.features.attendance.remote.attendance.AttendanceApi
import ru.ushell.app.data.features.messenger.remote.MessengerApi
import ru.ushell.app.data.features.timetable.remote.timetable.TimetableApi
import ru.ushell.app.data.features.user.remote.AuthApi
import ru.ushell.app.domain.service.token.TokenService
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RetrofitModule {

    @Provides
    @Singleton
    fun providesSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences(
            context.getString(R.string.app_name),
            Context.MODE_PRIVATE
        )
    }

    @Provides
    @Singleton
    fun httpLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().apply {
            this.level = HttpLoggingInterceptor.Level.BODY
        }


    @Provides
    @Singleton
    fun httpClient(
        httpLoggingInterceptor: HttpLoggingInterceptor,
        tokenService: TokenService
    ): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor { chain ->
                val token = tokenService.getAccessToken()
                val request = chain.request().newBuilder()
                    .apply {
                        if (!token.isNullOrBlank()) {
                            addHeader("Authorization", "Bearer $token")
                        }
                    }
                    .build()
                return@addInterceptor chain.proceed(request)
            }
            .addInterceptor(httpLoggingInterceptor)
            .build()

    @Provides
    @Singleton
    @RetrofitClient
    fun retrofit(okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    fun providesAuthApi(@RetrofitClient retrofit: Retrofit): AuthApi =
        retrofit.create(AuthApi::class.java)

    @Provides
    @Singleton
    fun providesTimetableApi(@RetrofitClient retrofit: Retrofit): TimetableApi =
        retrofit.create(TimetableApi::class.java)

    @Provides
    @Singleton
    fun providesAttendanceApi(@RetrofitClient retrofit: Retrofit): AttendanceApi =
        retrofit.create(AttendanceApi::class.java)

    @Provides
    @Singleton
    fun providesMessengerApi(@RetrofitClient retrofit: Retrofit): MessengerApi =
        retrofit.create(MessengerApi::class.java)
}
