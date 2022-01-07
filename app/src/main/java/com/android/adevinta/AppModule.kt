package com.android.adevinta

import com.android.adevinta.provider.ApiProvider
import com.android.adevinta.repository.UserRepository
import com.squareup.moshi.Moshi
import org.koin.dsl.module

val appModule = module {
    single {
        ApiProvider()
    }

    single {
        Moshi.Builder().build()
    }


    factory {
        UserRepository(
            userApi = get<ApiProvider>().userApi
        )
    }

}