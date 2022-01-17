package com.android.users

import com.android.users.provider.ApiProvider
import com.android.users.repository.UserRepository
import com.android.users.util.DefaultPersistenceStore
import com.android.users.util.DefaultSharedPreferencesStore
import com.android.users.util.PersistenceStore
import com.android.users.util.SharedPreferencesStore
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