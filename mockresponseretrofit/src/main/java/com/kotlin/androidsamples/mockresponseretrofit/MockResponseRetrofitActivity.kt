package com.kotlin.androidsamples.mockresponseretrofit

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.kotlin.androidsamples.mockresponseretrofit.databinding.ActivityMockResponseRetrofitBinding
import kotlinx.coroutines.launch

class MockResponseRetrofitActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMockResponseRetrofitBinding

    private val viewModel by viewModels<MockResponseRetrofitViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMockResponseRetrofitBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnGetMockApiResponse.setOnClickListener {
            viewModel.getUsers(this)
            viewModel.getUser(this, "abdanzakialifian")
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                launch {
                    viewModel.listUsers.collect {
                        Log.d("CEK", "RESPONSE LIST USERS : $it")
                    }
                }

                launch {
                    viewModel.user.collect {
                        Log.d("CEK", "RESPONSE USER : $it")
                    }
                }
            }
        }
    }
}