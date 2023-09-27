package com.lcpetlylgmg.petly.organization.post.data

import com.google.firebase.firestore.FirebaseFirestore
import com.lcpetlylgmg.petly.organization.data.Feedback
import com.lcpetlylgmg.petly.utils.GlobalKeys

class PostRepository {

    private val firestore = FirebaseFirestore.getInstance()

    fun savePost(post: Post, onComplete: (Boolean, String?) -> Unit) {
        val collectionRef = firestore.collection(GlobalKeys.POST_TABLE)

        collectionRef.document(post.postId.toString()).set(post)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onComplete(true, null)
                } else {
                    onComplete(false, task.exception?.message ?: "Error saving post.")
                }
            }
    }
    fun sendFeedBack(feedback: Feedback, onComplete: (Boolean, String?) -> Unit) {
        val collectionRef = firestore.collection(GlobalKeys.FEEDBACK_TABLE)

        collectionRef.document(feedback.feedbackId.toString()).set(feedback)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onComplete(true, null)
                } else {
                    onComplete(false, task.exception?.message ?: "Error saving feednack.")
                }
            }
    }

    fun updatePost(post: Post, onComplete: (Boolean, String?) -> Unit) {
        val collectionRef = firestore.collection(GlobalKeys.POST_TABLE)

        // Assuming that post.postId is the document ID of the post you want to update
        val documentRef = collectionRef.document(post.postId.toString())

        documentRef.update(
            "age", post.age,
            "ageRange", post.ageRange,
            "breed", post.breed,
            "city", post.city,
            "compatibleWithCats", post.compatibleWithCats,
            "compatibleWithKids", post.compatibleWithKids,
            "country", post.country,
            "dogAge", post.age,
            "dogExperience", post.dogExperience,
            "dogGender", post.dogGender,
            "dogName", post.dogName,
            "dogSize", post.dogSize,
            "handicapDog", post.handicapDog,
            "imageUrl", post.imageUrl,
            "aboutDog", post.aboutDog,
            "state", post.state

        )
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onComplete(true, null)
                } else {
                    onComplete(false, task.exception?.message ?: "Error updating post.")
                }
            }
    }


    fun getPostsByUserID(userID: String, onComplete: (List<Post>?, String?) -> Unit) {
        val collectionRef = firestore.collection(GlobalKeys.POST_TABLE)
        collectionRef.whereEqualTo("postedBy", userID)
            .get()
            .addOnSuccessListener { querySnapshot ->
                val posts = querySnapshot.toObjects(Post::class.java)
                onComplete(posts, null)
            }
            .addOnFailureListener { exception ->
                onComplete(null, exception.message ?: "Error fetching posts.")
            }
    }

}
