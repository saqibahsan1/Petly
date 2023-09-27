package com.lcpetlylgmg.petly.common.chat.data

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.lcpetlylgmg.petly.utils.GlobalKeys

class ChatRepository {

    private val chatRef = FirebaseFirestore.getInstance().collection(GlobalKeys.CHAT_TABLE)


    fun createOrUpdateDocument(chatThread: ChatThread, message: Message) {
        val query = chatRef.whereEqualTo("userId", chatThread.userId)
            .whereEqualTo("shelterId", chatThread.shelterId)
            .whereEqualTo("postId", chatThread.postId)

        query.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val documents = task.result?.documents
                if (documents.isNullOrEmpty()) {
                    // Create a new chat document with initial data

                    chatRef.document(chatThread.threadId).set(chatThread)
                        .addOnSuccessListener {
                            chatRef.document(chatThread.threadId).collection("msgs")
                                .document(message.messageId).set(message)
                        }
                        .addOnFailureListener { e ->
                            // Handle the error
                        }
                } else {
                    // Chat document already exists, you can update it
                    val chatDocument =
                        documents[0] // Assuming there's only one matching document
                    val chatDocumentId = chatDocument.id


                    // Update specific keys in the chat document
                    val updates = hashMapOf(
                        "lastMsg" to chatThread.lastMsg,
                        "lastMsgTime" to chatThread.lastMsgTime,
                        "isShelterNewMessage" to chatThread.isShelterNewMessage,
                        "isUserNewMessage" to chatThread.isUserNewMessage,
                        "lastMsgUserId" to chatThread.lastMsgUserId,
                        "lastMsgUserName" to chatThread.lastMsgUserName,
                        "modifiedDate" to chatThread.modifiedDate,
                    )

                    chatRef.document(chatDocumentId)
                        .update(updates as Map<String, Any>)
                        .addOnSuccessListener {
                            chatRef.document(chatThread.threadId).collection("msgs")
                                .document(message.messageId).set(message)
                        }
                        .addOnFailureListener { e ->
                            Log.d("TAGGG", e.message.toString())
                        }
                }
            } else {
                // Handle the error
            }
        }
    }

    fun getChatsByUserId(userId: String, onComplete: (List<ChatThread>?, Exception?) -> Unit) {
        chatRef.get()
            .addOnSuccessListener { documents ->
                val chatThreads = mutableListOf<ChatThread>()

                for (document in documents) {
                    val chatThread = document.toObject(ChatThread::class.java)
                    // Check if the user's userId or shelterId matches the given ID
                    if (chatThread.userId == userId || chatThread.shelterId == userId) {
                        chatThreads.add(chatThread)
                    }
                }

                onComplete(chatThreads, null)
            }
            .addOnFailureListener { exception ->
                onComplete(null, exception)
            }
    }


}