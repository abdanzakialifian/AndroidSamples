package com.android.playground.phone.di

import com.android.playground.di.DynamicFeatureModuleProvider
import com.android.playground.phone.dashboard.DashboardViewModel
import com.android.playground.phone.detail.DetailViewModel
import com.android.playground.phone.finding.FindingViewModel
import com.google.android.gms.wearable.Wearable
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

class DynamicFeatureModuleProviderImpl : DynamicFeatureModuleProvider {
    override fun getKoinModules(): List<Module> = listOf(
        module {
            factory { Wearable.getCapabilityClient(androidContext()) }
            factory { Wearable.getMessageClient(androidContext()) }
            viewModel { FindingViewModel(get()) }
            viewModel { DetailViewModel(get()) }
            viewModel { DashboardViewModel() }
        }
    )
}