package com.example.weatherapp.api.response

import android.os.Parcel
import android.os.Parcelable

data class ApiSys(
    var pod: String?
): Parcelable {
    constructor(parcel: Parcel) : this(parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(pod)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ApiSys> {
        override fun createFromParcel(parcel: Parcel): ApiSys {
            return ApiSys(parcel)
        }

        override fun newArray(size: Int): Array<ApiSys?> {
            return arrayOfNulls(size)
        }
    }
}