package com.android.adevinta

import com.android.adevinta.provider.ApiProvider
import com.android.adevinta.repository.UserRepository
import com.squareup.moshi.Moshi
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val appModule = module {
    single {
        ApiProvider()
    }

    /**single<Dispatcher> {
        DefaultDispatcher()
    }**/

    single {
        Moshi.Builder().build()
    }


    factory {
        UserRepository(
            userApi = get<ApiProvider>().userApi
        )
    }

}