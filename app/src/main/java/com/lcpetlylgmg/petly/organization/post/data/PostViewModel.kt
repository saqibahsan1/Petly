package com.lcpetlylgmg.petly.organization.post.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.lcpetlylgmg.petly.utils.GlobalKeys

class PostViewModel(private val postRepository: PostRepository) : ViewModel() {

    private val _userPostsLiveData = MutableLiveData<List<Post>>()
    val userPostsLiveData: LiveData<List<Post>> = _userPostsLiveData

    private val _userPostsLiveDataMessage = MutableLiveData<String>()
    val userPostsLiveDataMessage: LiveData<String> = _userPostsLiveDataMessage

    fun savePost(post: Post, onComplete: (Boolean, String?) -> Unit) {
        postRepository.savePost(post, onComplete)
    }

    fun updatePost(post: Post, onComplete: (Boolean, String?) -> Unit) {
        postRepository.updatePost(post, onComplete)
    }


    fun getPostsByUserID(userID: String) {
        postRepository.getPostsByUserID(userID) { posts, errorMessage ->
            if (posts != null) {
                _userPostsLiveData.postValue(posts)
            } else {
                _userPostsLiveDataMessage.postValue(errorMessage)
            }
        }
    }


    fun deletePost(
        documentId: String,
        onComplete: (Boolean) -> Unit
    ) {
        val firestore = FirebaseFirestore.getInstance()
        val storage = FirebaseStorage.getInstance()

        val collectionRef = FirebaseFirestore.getInstance().collection(GlobalKeys.POST_TABLE)

        // Step 2: If there's associated image data, delete it first
        collectionRef.document(documentId).get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val imageUrl = documentSnapshot.getString("imageUrl")
                    if (imageUrl != null) {
                        // Delete the image from Firebase Storage
                        val storageRef = storage.getReferenceFromUrl(imageUrl)
                        storageRef.delete()
                            .addOnSuccessListener {
                                collectionRef.document(documentId).delete()
                                    .addOnSuccessListener {
                                        onComplete(true)
                                    }
                                    .addOnFailureListener { e ->
                                        // Handle errors while deleting the Firestore document
                                        Log.e("TAG", "Error deleting document: $e")
                                    }
                            }
                            .addOnFailureListener { e ->
                                // Handle errors while deleting the image
                                Log.e("TAG", "Error deleting image: $e")
                            }
                    } else {
                        // No associated image, proceed to delete the Firestore document
                        collectionRef.document(documentId).delete()
                            .addOnSuccessListener {
                                onComplete(true)
                            }
                            .addOnFailureListener { e ->
                                // Handle errors while deleting the Firestore document
                                Log.e("TAG", "Error deleting document: $e")
                            }
                    }
                }
            }
            .addOnFailureListener { e ->
                // Handle errors while fetching the Firestore document
                Log.e("TAG", "Error getting document: $e")
            }
    }




}
