package com.android.playground.watch

import android.os.Build
import android.util.Log

object Utils {
    fun getDeviceInformation() {
        Log.d("CEK", "ID : ${Build.ID}")
        Log.d("CEK", "SKU : ${Build.SKU}")
        Log.d("CEK", "HOST : ${Build.HOST}")
        Log.d("CEK", "TAGS : ${Build.TAGS}")
        Log.d("CEK", "BOARD : ${Build.BOARD}")
        Log.d("CEK", "BOOTLOADER : ${Build.BOOTLOADER}")
        Log.d("CEK", "BRAND : ${Build.BRAND}")
        Log.d("CEK", "DEVICE : ${Build.DEVICE}")
        Log.d("CEK", "DISPLAY : ${Build.DISPLAY}")
        Log.d("CEK", "FINGERPRINT : ${Build.FINGERPRINT}")
        Log.d("CEK", "HARDWARE : ${Build.HARDWARE}")
        Log.d("CEK", "MANUFACTURER : ${Build.MANUFACTURER}")
        Log.d("CEK", "MODEL : ${Build.MODEL}")
        Log.d("CEK", "ODM SKU : ${Build.ODM_SKU}")
        Log.d("CEK", "PRODUCT : ${Build.PRODUCT}")
        Log.d("CEK", "SOC MANUFACTURER : ${Build.SOC_MANUFACTURER}")
        Log.d("CEK", "SOC MODEL : ${Build.SOC_MODEL}")
        Log.d("CEK", "SUPPORTED 32BIT ABIS : ${Build.SUPPORTED_32_BIT_ABIS}")
        Log.d("CEK", "SUPPORTED 64BIT ABIS : ${Build.SUPPORTED_64_BIT_ABIS}")
        Log.d("CEK", "SUPPORTED ABIS : ${Build.SUPPORTED_ABIS}")
        Log.d("CEK", "TIME : ${Build.TIME}")
        Log.d("CEK", "TYPE : ${Build.TYPE}")
        Log.d("CEK", "UNKNOWN : ${Build.UNKNOWN}")
        Log.d("CEK", "USER : ${Build.USER}")
    }
}