package com.lcpetlylgmg.petly.organization.post.data

import android.os.Parcel
import android.os.Parcelable

data class Post(
    var category: String? = null,
    var productId: String? = null,
    var link: String? = null,
    var isBlog: String? = null,
    var price: String? = null,
    var productName: String? = null,
    var age: String? = null,
    var ageRange: String? = null,
    var breed: String? = null,
    var city: String? = null,
    var compatibleWithCats: String? = null,
    var compatibleWithKids: String? = null,
    var country: String? = null,
    var dogExperience: String? = null,
    var dogGender: String? = null,
    var dogName: String? = null,
    var dogSize: String? = null,
    var handicapDog: String? = null,
    var imageUrl: String? = null,
    var postId: String? = null,
    var postedBy: String? = null,
    var postedByName: String? = null,
    var aboutDog: String? = null,
    var postedDate: Long? = null,
    var state: List<String>? = null
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
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readLong(),
        parcel.createStringArrayList()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(category)
        parcel.writeString(productId)
        parcel.writeString(link)
        parcel.writeString(isBlog)
        parcel.writeString(price)
        parcel.writeString(productName)
        parcel.writeString(age)
        parcel.writeString(ageRange)
        parcel.writeString(breed)
        parcel.writeString(city)
        parcel.writeString(compatibleWithCats)
        parcel.writeString(compatibleWithKids)
        parcel.writeString(country)
        parcel.writeString(dogExperience)
        parcel.writeString(dogGender)
        parcel.writeString(dogName)
        parcel.writeString(dogSize)
        parcel.writeString(handicapDog)
        parcel.writeString(imageUrl)
        parcel.writeString(postId)
        parcel.writeString(postedBy)
        parcel.writeString(postedByName)
        parcel.writeString(aboutDog)
        parcel.writeLong(postedDate ?: 0)
        parcel.writeStringList(state)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Post> {
        override fun createFromParcel(parcel: Parcel): Post {
            return Post(parcel)
        }

        override fun newArray(size: Int): Array<Post?> {
            return arrayOfNulls(size)
        }
    }
}
