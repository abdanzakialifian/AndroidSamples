package com.android.playground.mockokhttp.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.android.playground.mockokhttp.databinding.ActivityMockResponseRetrofitBinding
import com.android.playground.mockokhttp.utils.Result
import com.android.playground.mockokhttp.utils.Utils.handleResponseError
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class MockOkhttpActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMockResponseRetrofitBinding

    private val viewModel by inject<MockOkhttpViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMockResponseRetrofitBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            btnGetMockApiResponse.setOnClickListener {
                lifecycleScope.launch {
                    viewModel.getUser("abdanzakialifian")
                }
            }

            btnGetRealApiResponse.setOnClickListener {
                lifecycleScope.launch {
                    viewModel.getUsers()
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                launch {
                    viewModel.listUsers.collect { result ->
                        when(result) {
                            is Result.LOADING -> {
                                binding.progressBar.isVisible = true
                            }
                            is Result.SUCCESS -> {
                                binding.progressBar.isVisible = false
                            }
                            is Result.ERROR -> {
                                binding.progressBar.isVisible = false
                                result.exception.handleResponseError()
                            }
                            is Result.INITIAL -> Unit
                        }
                    }
                }

                launch {
                    viewModel.user.collect { result ->
                        when(result) {
                            is Result.LOADING -> {
                                binding.progressBar.isVisible = true
                            }
                            is Result.SUCCESS -> {
                                binding.progressBar.isVisible = false
                            }
                            is Result.ERROR -> {
                                binding.progressBar.isVisible = false
                                result.exception.handleResponseError()
                            }
                            is Result.INITIAL -> Unit
                        }
                    }
                }
            }
        }
    }
}