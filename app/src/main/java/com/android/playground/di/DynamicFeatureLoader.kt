package com.android.playground.di

import org.koin.core.context.loadKoinModules

object DynamicFeatureLoader {
    fun load(providerClassName: String) {
        val clazz = Class.forName(providerClassName)
        val provider = clazz.getDeclaredConstructor().newInstance() as? DynamicFeatureModuleProvider
        loadKoinModules(provider?.getKoinModules().orEmpty())
    }
}