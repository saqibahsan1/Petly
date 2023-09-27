package com.lcpetlylgmg.petly.common.requests.data

import android.os.Parcel
import android.os.Parcelable

data class Request(
    var about: String? = null,
    var ageRange: String? = null,
    var breed: String? = null,
    var city: String? = null,
    var compatibleWithCats: String? = null,
    var compatibleWithKids: String? = null,
    var country: String? = null,
    var dogAge: String? = null,
    var dogExperience: String? = null,
    var dogGender: String? = null,
    var dogName: String? = null,
    var dogSize: String? = null,
    var handicapDog: String? = null,
    var postId: String? = null,
    var postImgUrl: String? = null,
    var requestFrom: String? = null,
    var requestFromName: String? = null,
    var requestFromType: String? = null,
    var requestId: String? = null,
    var requestTime: Long? = null,
    var requestTo: String? = null,
    var requestToName: String? = null,
    var state: List<String>? = null,
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readLong(),
        parcel.readString(),
        parcel.readString(),
        parcel.createStringArrayList()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(about)
        parcel.writeString(ageRange)
        parcel.writeString(breed)
        parcel.writeString(city)
        parcel.writeString(compatibleWithCats)
        parcel.writeString(compatibleWithKids)
        parcel.writeString(country)
        parcel.writeString(dogAge)
        parcel.writeString(dogExperience)
        parcel.writeString(dogGender)
        parcel.writeString(dogName)
        parcel.writeString(dogSize)
        parcel.writeString(handicapDog)
        parcel.writeString(postId)
        parcel.writeString(postImgUrl)
        parcel.writeString(requestFrom)
        parcel.writeString(requestFromName)
        parcel.writeString(requestFromType)
        parcel.writeString(requestId)
        parcel.writeLong(requestTime ?: 0)
        parcel.writeString(requestTo)
        parcel.writeString(requestToName)
        parcel.writeStringList(state)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Request> {
        override fun createFromParcel(parcel: Parcel): Request {
            return Request(parcel)
        }

        override fun newArray(size: Int): Array<Request?> {
            return arrayOfNulls(size)
        }
    }
}
