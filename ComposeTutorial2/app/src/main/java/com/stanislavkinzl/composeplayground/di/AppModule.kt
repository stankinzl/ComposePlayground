package com.stanislavkinzl.composeplayground.di

import android.content.Context
import android.net.ConnectivityManager
import androidx.room.Room
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.stanislavkinzl.composeplayground.BuildConfig
import com.stanislavkinzl.composeplayground.data.db.room.AppDatabase
import com.stanislavkinzl.composeplayground.data.db.room.NetworkCatsDao
import com.stanislavkinzl.composeplayground.network.CatsApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    companion object {
        const val OKHTTP_NETWORK_CONNECT_TIMEOUT = 20L
        const val OKHTTP_NETWORK_READ_TIMEOUT = 20L
        const val APP_DATABASE_NAME = "APP_DATABASE"
    }

    @Provides
    fun provideApiService(retrofit: Retrofit): CatsApiService = retrofit.create(CatsApiService::class.java)

    @Provides
    @Singleton
    fun provideRetrofit(
        moshi: Moshi,
        okHttpClient: OkHttpClient
    ): Retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.API_BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .client(okHttpClient)
        .build()

    @Provides
    @Singleton
    fun provideMoshi(): Moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient = OkHttpClient.Builder()
        .apply {
            if (BuildConfig.DEBUG) {
                addNetworkInterceptor(
                    HttpLoggingInterceptor()
                        .setLevel(HttpLoggingInterceptor.Level.BODY)
                )
            }
        }
        .connectTimeout(OKHTTP_NETWORK_CONNECT_TIMEOUT, TimeUnit.SECONDS)
        .readTimeout(OKHTTP_NETWORK_READ_TIMEOUT, TimeUnit.SECONDS)
        .build()

    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): AppDatabase = Room.databaseBuilder(context, AppDatabase::class.java, APP_DATABASE_NAME)
        .fallbackToDestructiveMigration()
        .build()

    @Provides
    @Singleton
    fun provideNetworkCatsDao(appDatabase: AppDatabase): NetworkCatsDao = appDatabase.catsDao()

    @Provides
    fun provideConnectivityManager(@ApplicationContext appContext: Context) =
        appContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
}