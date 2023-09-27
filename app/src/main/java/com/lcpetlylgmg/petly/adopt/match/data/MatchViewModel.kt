package com.lcpetlylgmg.petly.adopt.match.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lcpetlylgmg.petly.organization.post.data.Post
import com.lcpetlylgmg.petly.user.data.User
import kotlinx.coroutines.launch

class MatchViewModel : ViewModel() {
    private val repository = MatchRepository()

    private val postLiveData: MutableLiveData<List<Post>> = MutableLiveData()

    fun searchSingleDog(match: Match, isMatter: String) {
        viewModelScope.launch {
            val resultLiveData = repository.searchSingleDog(match, isMatter)
            resultLiveData.observeForever { postList ->
                postLiveData.postValue(postList)
            }
        }
    }

    // Expose the postLiveData to the UI
    fun getPostLiveData(): MutableLiveData<List<Post>> {
        return postLiveData
    }
}
