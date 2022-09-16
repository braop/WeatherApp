package com.example.weatherapp.api.response

import android.os.Parcel
import android.os.Parcelable

data class ApiWind(
    var speed: Double?,
    var deg: Int?,
    var gust: Double?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Double::class.java.classLoader) as? Double
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(speed)
        parcel.writeValue(deg)
        parcel.writeValue(gust)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ApiWind> {
        override fun createFromParcel(parcel: Parcel): ApiWind {
            return ApiWind(parcel)
        }

        override fun newArray(size: Int): Array<ApiWind?> {
            return arrayOfNulls(size)
        }
    }
}