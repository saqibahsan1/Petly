package com.lcpetlylgmg.petly.user.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.lcpetlylgmg.petly.organization.post.data.Post
import com.lcpetlylgmg.petly.utils.GlobalKeys
import kotlinx.coroutines.tasks.await

class UserRepository {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    fun registerUser(
        email: String,
        password: String,
        user: User,
        onComplete: (Boolean, String?) -> Unit
    ) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val currentUser = auth.currentUser
                    currentUser?.uid?.let { userId ->
                        user.userId = userId
                        firestore.collection(GlobalKeys.USER_TABLE).document(userId)
                            .set(user)
                            .addOnCompleteListener { firestoreTask ->
                                if (firestoreTask.isSuccessful) {
                                    onComplete(true, null)
                                } else {
                                    onComplete(false, firestoreTask.exception?.message)
                                }
                            }
                    }
                } else {
                    val exception = task.exception as? FirebaseAuthException
                    onComplete(false, exception?.message ?: "Registration failed.")
                }
            }
    }

    fun loginUser(email: String, password: String, onComplete: (Boolean, String?) -> Unit) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onComplete(true, null)
                } else {
                    val exception = task.exception as? FirebaseAuthException
                    onComplete(false, exception?.message ?: "Login failed.")
                }
            }
    }


    fun getUserData(userId: String, onComplete: (User?, String?) -> Unit) {
        firestore.collection(GlobalKeys.USER_TABLE).document(userId)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val document = task.result
                    if (document != null && document.exists()) {
                        val userData = document.toObject(User::class.java)
                        onComplete(userData, null)
                    } else {
                        onComplete(null, "User data not found.")
                    }
                } else {
                    onComplete(null, task.exception?.message ?: "Error fetching user data.")
                }
            }
    }

    suspend fun getProducts(onComplete: (List<Post>?, String?) -> Unit) {
        val postList = mutableListOf<Post>()
        val collectionRef = firestore.collection(GlobalKeys.PRODUCT_TABLE)
        val query: Query = collectionRef
        val querySnapshot = query.get().await()
        for (document in querySnapshot.documents) {
            val post = document.toObject(Post::class.java)
            if (post != null) {
                postList.add(post)
            }
        }
        onComplete(postList, "Data Found")
    }

    fun saveFCMTokenForUser(
        userId: String,
        fcmToken: String,
        onComplete: (Boolean, String?) -> Unit
    ) {
        firestore.collection(GlobalKeys.USER_TABLE).document(userId)
            .update("fcmToken", fcmToken)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onComplete(true, null)
                } else {
                    onComplete(false, task.exception?.message ?: "Error saving FCM token.")
                }
            }
    }

}
