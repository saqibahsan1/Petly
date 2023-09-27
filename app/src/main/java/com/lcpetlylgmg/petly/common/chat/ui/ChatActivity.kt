package com.lcpetlylgmg.petly.common.chat.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.lcpetlylgmg.petly.R
import com.lcpetlylgmg.petly.common.chat.data.ChatAdapter
import com.lcpetlylgmg.petly.common.chat.data.ChatThread
import com.lcpetlylgmg.petly.common.chat.data.ChatViewModel
import com.lcpetlylgmg.petly.common.chat.data.Message
import com.lcpetlylgmg.petly.common.requests.data.Request
import com.lcpetlylgmg.petly.databinding.ActivityChatBinding
import com.lcpetlylgmg.petly.prefs.PreferenceHelper
import com.lcpetlylgmg.petly.utils.Global
import com.lcpetlylgmg.petly.utils.GlobalKeys

class ChatActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChatBinding
    private lateinit var request: Request
    private lateinit var viewModel: ChatViewModel
    private lateinit var chatThreadAlready: ChatThread
    private var threadId = ""
    private var from = ""
    private var isUserNewMessage: Boolean = false
    private var isShelterNewMessage: Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        from = PreferenceHelper.getPref(this).getStringValue("chat").toString()

        val intent = intent
        if (intent != null) {
            if (from == "y") {
                chatThreadAlready = intent.getParcelableExtra<ChatThread>(GlobalKeys.CHAT)!!
                threadId = chatThreadAlready.threadId
            } else if (from == "n") {
                threadId = intent.getStringExtra(GlobalKeys.THREAD_ID).toString()
                request = intent.getParcelableExtra<Request>(GlobalKeys.REQUEST)!!
            }
        }
        viewModel = ViewModelProvider(this)[ChatViewModel::class.java]

        binding.sendMessageButton.setOnClickListener {
            val message = binding.messageEditText.text.toString()
            if (message.isEmpty()) {
                binding.messageEditText.error = getString(R.string.write_message)
            } else {
                if (from == "y") {
                    sendMessageInBox(message)
                } else if (from == "n") {
                    sendMessageRequest(message)
                }

            }
        }

        binding.buttonBack.setOnClickListener {
            finish()
        }
        viewModel.setupRealTimeMessageListener(threadId)
        initObserver()

    }

    override fun onResume() {
        super.onResume()
    }

    private fun initObserver() {
        viewModel.messageList.observe(this) {
            val adapter = ChatAdapter(
                messages = it,
                PreferenceHelper.getPref(this).getCurrentUser()?.userId.toString()
            )
            binding.messageRecyclerView.adapter = adapter

        }
    }

    private fun sendMessageRequest(message: String) {
        val createDate = Global.getCurrentUnixTimestamp()
        val isActive = true



        if (PreferenceHelper.getPref(this)
                .getCurrentUser()?.accountType == GlobalKeys.ACCOUNT_TYPE_ADOPT || PreferenceHelper.getPref(
                this
            ).getCurrentUser()?.accountType == GlobalKeys.ACCOUNT_TYPE_AGENT
        ) {
            isUserNewMessage = true
            isShelterNewMessage = false
        } else {
            isUserNewMessage = false
            isShelterNewMessage = true
        }
        val lastMsg = message
        val lastMsgUserId = PreferenceHelper.getPref(this).getCurrentUser()?.userId
        val lastMsgUserName = PreferenceHelper.getPref(this).getCurrentUser()?.name
        val postId = request.postId
        val postImage = request.postImgUrl
        val shelterId = request.requestTo
        val userId = request.requestFrom
        val shelterName = request.requestToName
        val userName = request.requestFromName

        val message = Message(
            Global.generateRandomUUID(),
            message,
            createDate,
            lastMsgUserId!!,
            createDate
        )

        val chatThread =
            ChatThread(
                createDate,
                isActive,
                isShelterNewMessage,
                isUserNewMessage,
                lastMsg,
                createDate,
                lastMsgUserId,
                lastMsgUserName!!,
                createDate,
                postId!!,
                postImage!!,
                shelterId!!,
                shelterName!!,
                threadId,
                userId!!,
                userName!!
            )

        viewModel.createOrUpdateDocument(chatThread, message)

        binding.messageEditText.setText("")

    }


    private fun sendMessageInBox(message: String) {
        val createDate = Global.getCurrentUnixTimestamp()
        val isActive = true

        if (PreferenceHelper.getPref(this)
                .getCurrentUser()?.accountType == GlobalKeys.ACCOUNT_TYPE_ADOPT || PreferenceHelper.getPref(
                this
            ).getCurrentUser()?.accountType == GlobalKeys.ACCOUNT_TYPE_AGENT
        ) {
            isUserNewMessage = true
            isShelterNewMessage = false
        } else {
            isUserNewMessage = false
            isShelterNewMessage = true
        }
        val lastMsg = message
        val lastMsgUserId = PreferenceHelper.getPref(this).getCurrentUser()?.userId
        val lastMsgUserName = PreferenceHelper.getPref(this).getCurrentUser()?.name
        val postId = chatThreadAlready.postId
        val postImage = chatThreadAlready.postImg
        val shelterId = chatThreadAlready.shelterId
        val userId = chatThreadAlready.userId
        val shelterName = chatThreadAlready.shelterName
        val userName = chatThreadAlready.userName
        val threadId = chatThreadAlready.threadId

        val chatThread =
            ChatThread(
                createDate,
                isActive,
                isShelterNewMessage,
                isUserNewMessage,
                lastMsg,
                createDate,
                lastMsgUserId!!,
                lastMsgUserName!!,
                createDate,
                postId,
                postImage,
                shelterId,
                shelterName,
                threadId,
                userId,
                userName
            )
        val message = Message(
            Global.generateRandomUUID(),
            message,
            createDate,
            lastMsgUserId,
            createDate
        )

        viewModel.createOrUpdateDocument(chatThread, message)
        viewModel.setupRealTimeMessageListener(chatThread.threadId)
        binding.messageEditText.setText("")
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }


}