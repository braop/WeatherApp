package com.example.weatherapp.api.response

import android.os.Parcel
import android.os.Parcelable

data class ApiClouds(
    var all: Int?
) : Parcelable {
    constructor(parcel: Parcel) : this(parcel.readValue(Int::class.java.classLoader) as? Int)

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(all)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ApiClouds> {
        override fun createFromParcel(parcel: Parcel): ApiClouds {
            return ApiClouds(parcel)
        }

        override fun newArray(size: Int): Array<ApiClouds?> {
            return arrayOfNulls(size)
        }
    }
}