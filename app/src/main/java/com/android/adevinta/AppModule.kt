package com.android.adevinta

import com.android.adevinta.provider.ApiProvider
import com.android.adevinta.repository.UserRepository
import com.android.adevinta.util.DefaultPersistenceStore
import com.android.adevinta.util.DefaultSharedPreferencesStore
import com.android.adevinta.util.PersistenceStore
import com.android.adevinta.util.SharedPreferencesStore
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