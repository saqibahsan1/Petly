package com.lcpetlylgmg.petly.common.chat.data
import android.os.Parcel
import android.os.Parcelable

data class ChatThread(
    var createdDate: Long = 0,
    @field:JvmField
    var isActive: Boolean = false,
    @field:JvmField
    var isShelterNewMessage: Boolean = false,
    @field:JvmField
    var isUserNewMessage: Boolean = false,
    var lastMsg: String = "",
    var lastMsgTime: Long = 0,
    var lastMsgUserId: String = "",
    var lastMsgUserName: String = "",
    var modifiedDate: Long = 0,
    var postId: String = "",
    var postImg: String = "",
    var shelterId: String = "",
    var shelterName: String = "",
    var threadId: String = "",
    var userId: String = "",
    var userName: String = ""
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readByte() != 0.toByte(),
        parcel.readByte() != 0.toByte(),
        parcel.readByte() != 0.toByte(),
        parcel.readString() ?: "",
        parcel.readLong(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readLong(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(createdDate)
        parcel.writeByte(if (isActive) 1 else 0)
        parcel.writeByte(if (isShelterNewMessage) 1 else 0)
        parcel.writeByte(if (isUserNewMessage) 1 else 0)
        parcel.writeString(lastMsg)
        parcel.writeLong(lastMsgTime)
        parcel.writeString(lastMsgUserId)
        parcel.writeString(lastMsgUserName)
        parcel.writeLong(modifiedDate)
        parcel.writeString(postId)
        parcel.writeString(postImg)
        parcel.writeString(shelterId)
        parcel.writeString(shelterName)
        parcel.writeString(threadId)
        parcel.writeString(userId)
        parcel.writeString(userName)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ChatThread> {
        override fun createFromParcel(parcel: Parcel): ChatThread {
            return ChatThread(parcel)
        }

        override fun newArray(size: Int): Array<ChatThread?> {
            return arrayOfNulls(size)
        }
    }
}
