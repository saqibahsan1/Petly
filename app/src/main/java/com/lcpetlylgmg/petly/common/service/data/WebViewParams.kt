package com.lcpetlylgmg.petly.common.service.data

import android.os.Parcel
import android.os.Parcelable

data class WebViewParams(
    val name : String? = null,
    val link : String? = null
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString()
    ) {}

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(link)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<WebViewParams> {
        override fun createFromParcel(parcel: Parcel): WebViewParams {
            return WebViewParams(parcel)
        }

        override fun newArray(size: Int): Array<WebViewParams?> {
            return arrayOfNulls(size)
        }
    }
}
