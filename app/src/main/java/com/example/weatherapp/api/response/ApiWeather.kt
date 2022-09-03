package com.example.weatherapp.api.response

import android.os.Parcel
import android.os.Parcelable

data class ApiWeather(
    var id: Int?,
    var main: String?,
    var description: String?,
    var icon: String?
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(id)
        parcel.writeString(main)
        parcel.writeString(description)
        parcel.writeString(icon)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ApiWeather> {
        override fun createFromParcel(parcel: Parcel): ApiWeather {
            return ApiWeather(parcel)
        }

        override fun newArray(size: Int): Array<ApiWeather?> {
            return arrayOfNulls(size)
        }
    }
}
