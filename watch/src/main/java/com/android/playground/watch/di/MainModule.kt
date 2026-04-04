package com.android.playground.watch.di

import com.android.playground.watch.presentation.discoverable.DiscoverableViewModel
import com.google.android.gms.wearable.Wearable
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val mainModule = module {
    factory { Wearable.getMessageClient(androidContext()) }
    factory { Wearable.getCapabilityClient(androidContext()) }
    viewModel { DiscoverableViewModel(get(), get()) }
}