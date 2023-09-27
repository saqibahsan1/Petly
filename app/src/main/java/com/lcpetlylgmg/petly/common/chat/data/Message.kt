package com.lcpetlylgmg.petly.common.chat.data

data class Message(
    var messageId: String = "",
    var messageText: String = "",
    var modified: Long = 0,
    var senderId: String = "",
    var time: Long = 0
)
