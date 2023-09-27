package com.lcpetlylgmg.petly.common.requests.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class RequestViewModelFactory(private val requestRepository: RequestRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RequestViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RequestViewModel(requestRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
