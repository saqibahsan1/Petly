package com.lcpetlylgmg.petly.organization.post.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class PostViewModelFactory(private val postRepository: PostRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PostViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PostViewModel(postRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
