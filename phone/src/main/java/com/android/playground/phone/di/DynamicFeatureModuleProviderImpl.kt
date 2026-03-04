package com.android.playground.phone.di

import com.android.playground.di.DynamicFeatureModuleProvider
import com.android.playground.phone.finding.FindingViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

class DynamicFeatureModuleProviderImpl : DynamicFeatureModuleProvider {
    override fun getKoinModules(): List<Module> = listOf(
        module {
            viewModel { FindingViewModel() }
        }
    )
}