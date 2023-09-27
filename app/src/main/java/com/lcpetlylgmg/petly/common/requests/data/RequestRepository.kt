package com.lcpetlylgmg.petly.common.requests.data

import com.google.firebase.firestore.FirebaseFirestore
import com.lcpetlylgmg.petly.utils.GlobalKeys

class RequestRepository {

    private val firestore = FirebaseFirestore.getInstance()


    fun getRequestsByUserID(
        userID: String,
        userType: String,
        onComplete: (List<Request>?, String?) -> Unit
    ) {
        val collectionRef = firestore.collection(GlobalKeys.REQUEST_TABLE)
        collectionRef.whereEqualTo(userType, userID)
            .get()
            .addOnSuccessListener { querySnapshot ->
                val requests = querySnapshot.toObjects(Request::class.java)
                onComplete(requests, null)
            }
            .addOnFailureListener { exception ->
                onComplete(null, exception.message ?: "Error fetching posts.")
            }
    }


    fun sendRequest(request: Request, onComplete: (Boolean, String?) -> Unit) {
        val collectionRef = firestore.collection(GlobalKeys.REQUEST_TABLE)
        collectionRef.document(request.requestId.toString()).set(request)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onComplete(true, null)
                } else {
                    onComplete(false, task.exception?.message ?: "Error saving post.")
                }
            }
    }



    fun cancelRequest(requestId: String, onComplete: (Boolean, String?) -> Unit) {
        val collectionRef = firestore.collection(GlobalKeys.REQUEST_TABLE)
        collectionRef.document(requestId).delete()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onComplete(true, null)
                } else {
                    onComplete(false, task.exception?.message ?: "Error saving post.")
                }
            }
    }

    fun hasUserSentRequestForDog(request: Request, onComplete: (Boolean, String?) -> Unit) {
        val collectionRef = firestore.collection(GlobalKeys.REQUEST_TABLE)

        // Create a query to check if a request with the same postId and userId exists
        collectionRef
            .whereEqualTo("postId", request.postId)
            .whereEqualTo("requestFrom", request.requestFrom)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val documents = task.result?.documents
                    val userSentRequest = !documents.isNullOrEmpty()
                    if (userSentRequest) {
                        onComplete(userSentRequest, "Already Requested")
                    } else {
                        onComplete(userSentRequest, "Not Requested")
                    }
                } else {
                    // Handle the error and print the error message
                    val errorMessage = task.exception?.message ?: "Unknown error"
                    println("Firestore query error: $errorMessage")
                    onComplete(false, errorMessage)
                }
            }
    }


}
