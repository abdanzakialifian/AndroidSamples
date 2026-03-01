package com.android.playground.di

import org.koin.core.module.Module

interface DynamicFeatureModuleProvider {
    fun getKoinModules(): List<Module>
}