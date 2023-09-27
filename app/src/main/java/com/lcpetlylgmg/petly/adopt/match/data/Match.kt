package com.lcpetlylgmg.petly.adopt.match.data

import android.os.Parcel
import android.os.Parcelable

data class Match(
    var ageRange: String? = null,
    var compatibleWithCats: String? = null,
    var compatibleWithKids: String? = null,
    var country: String? = null,
    var dogExperience: String? = null,
    var dogGender: String? = null,
    var dogSize: String? = null,
    var handicapDog: String? = null,
    var state: List<String>? = null
) : Parcelable {

    // Write data to parcel
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(ageRange)
        parcel.writeString(compatibleWithCats)
        parcel.writeString(compatibleWithKids)
        parcel.writeString(country)
        parcel.writeString(dogExperience)
        parcel.writeString(dogGender)
        parcel.writeString(dogSize)
        parcel.writeString(handicapDog)
        parcel.writeStringList(state)
    }

    // Create an instance from a parcel
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.createStringArrayList()
    )

    // Required for Parcelable
    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Match> {
        override fun createFromParcel(parcel: Parcel): Match {
            return Match(parcel)
        }

        override fun newArray(size: Int): Array<Match?> {
            return arrayOfNulls(size)
        }
    }
}

