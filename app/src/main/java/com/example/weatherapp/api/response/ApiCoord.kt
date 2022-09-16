package com.example.weatherapp.api.response

import android.os.Parcel
import android.os.Parcelable

data class ApiCoord(
    var lat: Double?,
    var lon: Double?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readValue(Double::class.java.classLoader) as? Double
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(lat)
        parcel.writeValue(lon)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ApiCoord> {
        override fun createFromParcel(parcel: Parcel): ApiCoord {
            return ApiCoord(parcel)
        }

        override fun newArray(size: Int): Array<ApiCoord?> {
            return arrayOfNulls(size)
        }
    }
}