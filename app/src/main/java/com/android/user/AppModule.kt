package com.android.user

import com.android.user.provider.ApiProvider
import com.android.user.repository.UserRepository
import com.android.user.util.DefaultPersistenceStore
import com.android.user.util.DefaultSharedPreferencesStore
import com.android.user.util.PersistenceStore
import com.android.user.util.SharedPreferencesStore
import com.squareup.moshi.Moshi
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val appModule = module {
    single {
        ApiProvider()
    }

    single {
        Moshi.Builder().build()
    }

    single<PersistenceStore> {
        DefaultPersistenceStore(get())
    }

    single<SharedPreferencesStore> {
        DefaultSharedPreferencesStore(androidApplication())
    }


    factory {
        UserRepository(
            userApi = get<ApiProvider>().userApi
        )
    }

}