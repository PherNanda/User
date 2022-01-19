package com.android.user


import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val mainActivityModule = module {
    viewModel {
        MainActivityViewModel(
            userRepository = get(),
            persistenceStore = get()
        )
    }
}