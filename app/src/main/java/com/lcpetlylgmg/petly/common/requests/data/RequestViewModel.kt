package com.lcpetlylgmg.petly.common.requests.data

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.lcpetlylgmg.petly.user.data.User
import com.lcpetlylgmg.petly.utils.GlobalKeys

class RequestViewModel(private val requestRepository: RequestRepository) : ViewModel() {

    private val _userRequestLiveData = MutableLiveData<List<Request>>()
    val userRequestLiveData: LiveData<List<Request>> = _userRequestLiveData

    private val _userRequestLiveDataMessage = MutableLiveData<String>()
    val userRequestLiveDataMessage: LiveData<String> = _userRequestLiveDataMessage

    private val firestore = FirebaseFirestore.getInstance()

    fun getRequestsByUserID(userID: String, requestType: String) {
        requestRepository.getRequestsByUserID(userID, requestType) { requests, errorMessage ->
            if (requests != null) {
                _userRequestLiveData.postValue(requests)
            } else {
                _userRequestLiveDataMessage.postValue(errorMessage)
            }
        }
    }


    fun sendRequest(request: Request, onComplete: (Boolean, String?) -> Unit) {
        requestRepository.sendRequest(request, onComplete)
    }

    fun cancelRequest(requestId: String, onComplete: (Boolean, String?) -> Unit) {
        requestRepository.cancelRequest(requestId, onComplete)
    }

    fun hasAlreadyRequest(request: Request, onComplete: (Boolean, String?) -> Unit) {
        requestRepository.hasUserSentRequestForDog(request, onComplete)
    }

    fun updateRequestedList(
        userId: String,
        postId: String,
        user: User?,
    ) {
        val documentRef = firestore.collection(GlobalKeys.USER_TABLE).document(userId)
        documentRef.get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    // Get the current data in the document
                    val data = documentSnapshot.data

                    // Assuming you have an array field called "requestedPostIds"
                    var currentArray = data?.get("requestedPostIds") as? ArrayList<String>

                    // If the currentArray is null, initialize it as an empty ArrayList
                    if (currentArray == null) {
                        currentArray = ArrayList<String>()
                    }

                    // Make the desired modifications to the array
                    currentArray.add(postId) // Add an item to the array, for example

                    // Create a new map with the updated array
                    val updatedData = hashMapOf("requestedPostIds" to currentArray)

                    // Update the document with the updated data
                    documentRef.update(updatedData as Map<String, Any>)
                        .addOnSuccessListener {
                        }
                        .addOnFailureListener { e ->
                            // Handle any errors that occurred during the update
                            Log.e(ContentValues.TAG, "Error updating document: $e")
                        }
                }
            }
            .addOnFailureListener { e ->
                // Handle any errors that occurred while fetching the document
                Log.e(ContentValues.TAG, "Error getting document: $e")
            }
    }


}
