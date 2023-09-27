package com.lcpetlylgmg.petly.common.chat.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.lcpetlylgmg.petly.utils.GlobalKeys

class ChatViewModel : ViewModel() {

    private val repository = ChatRepository()
    private val firestore = FirebaseFirestore.getInstance()
    private val chatRef = FirebaseFirestore.getInstance().collection(GlobalKeys.CHAT_TABLE)
    private val _messageList = MutableLiveData<List<Message>>()
    val messageList: LiveData<List<Message>> get() = _messageList

    private val _chatThreads = MutableLiveData<List<ChatThread>>()
    val chatThreads: LiveData<List<ChatThread>> get() = _chatThreads

    fun setupRealTimeMessageListener(id: String) {
        val messageCollection =
            firestore.collection(GlobalKeys.CHAT_TABLE).document(id).collection("msgs")

        messageCollection.orderBy("time", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot, exception ->
                if (exception != null) {
                    // Handle the error
                    return@addSnapshotListener
                }

                val messages = mutableListOf<Message>()
                for (document in snapshot!!) {
                    val message = document.toObject(Message::class.java)
                    messages.add(message)
                }

                _messageList.postValue(messages)
            }
    }


    fun getChatsByUserId(userId: String) {
        repository.getChatsByUserId(userId) { chatThreads, exception ->
            if (exception == null) {
                _chatThreads.postValue(chatThreads)
            } else {
                // Handle the exception
            }
        }
    }

    fun createOrUpdateDocument(chatThread: ChatThread, message: Message) {
        repository.createOrUpdateDocument(chatThread, message)
    }



    fun checkChatThreadExists(
        userId: String,
        shelterId: String,
        postId: String, onComplete: (Boolean, ChatThread?, Exception?) -> Unit
    ) {
        val query = chatRef.whereEqualTo("userId", userId)
            .whereEqualTo("shelterId", shelterId)
            .whereEqualTo("postId", postId)

        query.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val documents = task.result?.documents
                val chatThreadExists = !documents.isNullOrEmpty()
                val existingChatThread = if (chatThreadExists) {
                    // Assuming there's only one matching document
                    documents!![0].toObject(ChatThread::class.java)
                } else {
                    null
                }
                onComplete(chatThreadExists, existingChatThread, null)
            } else {
                onComplete(false, null, task.exception)
            }
        }
    }


}
