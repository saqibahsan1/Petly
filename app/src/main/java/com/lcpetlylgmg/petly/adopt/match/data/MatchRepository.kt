package com.lcpetlylgmg.petly.adopt.match.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.lcpetlylgmg.petly.organization.post.data.Post
import com.lcpetlylgmg.petly.user.data.User
import com.lcpetlylgmg.petly.utils.GlobalKeys
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext


class MatchRepository {
    private val firestore = FirebaseFirestore.getInstance()

    suspend fun searchSingleDog(match: Match, isMatter: String): LiveData<List<Post>> {
        return withContext(Dispatchers.IO) {
            val postList: MutableList<Post> = mutableListOf()
            val collectionRef = firestore.collection(GlobalKeys.POST_TABLE)
            var query: Query = collectionRef

            // Apply filtering conditions based on match and isMatter
            if (match.state?.isNotEmpty() == true) {
                query = query.whereArrayContainsAny("state", match.state!!)
            }
            if (match.compatibleWithCats != isMatter) {
                query = query.whereEqualTo("compatibleWithCats", match.compatibleWithCats)
            }
            if (match.compatibleWithKids != isMatter) {
                query = query.whereEqualTo("compatibleWithKids", match.compatibleWithKids)
            }
            if (match.dogExperience != isMatter) {
                query = query.whereEqualTo("dogExperience", match.dogExperience)
            }
            if (match.dogSize != isMatter) {
                query = query.whereEqualTo("dogSize", match.dogSize)
            }
            if (match.compatibleWithCats != isMatter) {
                query = query.whereEqualTo("compatibleWithCats", match.compatibleWithCats)
            }
            if (match.handicapDog != isMatter) {
                query = query.whereEqualTo("handicapDog", match.handicapDog)
            }

            // Execute the query
            val querySnapshot = query.get().await()
            for (document in querySnapshot.documents) {
                val post = document.toObject(Post::class.java)
                if (post != null) {
                    postList.add(post)
                }
            }

            MutableLiveData(postList)
        }
    }

}