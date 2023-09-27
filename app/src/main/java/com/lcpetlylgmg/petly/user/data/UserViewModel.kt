package com.lcpetlylgmg.petly.user.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.lcpetlylgmg.petly.organization.post.data.Post
import com.lcpetlylgmg.petly.utils.GlobalKeys

class UserViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _registrationResult = MutableLiveData<Pair<Boolean, String?>>()
    val registrationResult: LiveData<Pair<Boolean, String?>> = _registrationResult

    private val _loginResult = MutableLiveData<Pair<Boolean, String?>>()
    val loginResult: LiveData<Pair<Boolean, String?>> = _loginResult

    private val firestore = FirebaseFirestore.getInstance()
    private val usersLiveData = MutableLiveData<List<User>>()

    fun registerUser(
        email: String,
        password: String,
        user: User
    ) {
        userRepository.registerUser(email, password, user) { success, errorMessage ->
            _registrationResult.value = Pair(success, errorMessage)
        }
    }


    fun loginUser(
        email: String,
        password: String
    ) {
        userRepository.loginUser(email, password) { success, errorMessage ->
            _loginResult.value = Pair(success, errorMessage)
        }
    }

    fun getUserData(userId: String, onComplete: (User?, String?) -> Unit) {
        userRepository.getUserData(userId, onComplete)
    }
    suspend fun getProductsData(onComplete: (List<Post>?, String?) -> Unit) {
        userRepository.getProducts(onComplete)
    }

    fun saveFCMTokenForUser(
        userId: String,
        fcmToken: String,
        onComplete: (Boolean, String?) -> Unit
    ) {
        userRepository.saveFCMTokenForUser(userId, fcmToken, onComplete)
    }


    fun getUsers(): LiveData<List<User>> {
        // Fetch users from Firestore
        firestore.collection(GlobalKeys.USER_TABLE)
            .get()
            .addOnSuccessListener { querySnapshot ->
                val usersList = mutableListOf<User>()
                for (document in querySnapshot.documents) {
                    val user = document.toObject(User::class.java)
                    if (user != null) {
                        if (!user.accountType.equals(GlobalKeys.ACCOUNT_TYPE_ADOPT))
                            user.let { usersList.add(it) }
                    }
                }
                usersLiveData.value = usersList // Update LiveData with fetched data
            }
            .addOnFailureListener { exception ->
                // Handle the error
                // You can add error handling logic here
            }
        return usersLiveData
    }
}
