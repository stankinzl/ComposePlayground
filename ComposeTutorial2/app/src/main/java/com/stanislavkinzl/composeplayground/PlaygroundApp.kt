package com.stanislavkinzl.composeplayground

import android.app.Application
import com.stanislavkinzl.composeplayground.data.db.sqlite.SQLiteDBHelper
import com.stanislavkinzl.composeplayground.domain.SampleDependency
import com.stanislavkinzl.composeplayground.domain.SampleDependency2
import com.stanislavkinzl.composeplayground.screens.KoinSampleScreenVM
import com.stanislavkinzl.composeplayground.screens.networkanddb.SQLiteSampleScreenVM
import dagger.hilt.android.HiltAndroidApp
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

@HiltAndroidApp
class PlaygroundApp : Application() {

    private val appModule = module {
        singleOf(::SampleDependency2)
        factoryOf(::SampleDependency)
        viewModelOf(::KoinSampleScreenVM)
        factory<SQLiteDBHelper> { SQLiteDBHelper(androidContext(), null) }
        viewModelOf(::SQLiteSampleScreenVM)
    }

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@PlaygroundApp)
            modules(appModule)
        }
    }
}