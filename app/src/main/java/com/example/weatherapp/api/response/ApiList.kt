package com.example.weatherapp.api.response

import android.os.Parcel
import android.os.Parcelable

data class ApiList(
    var dt: Int?,
    var main: ApiMain?,
    var weather: List<ApiWeather>?,
    var clouds: ApiClouds?,
    var wind: ApiWind?,
    var visibility: Int?,
    var pop: Int?,
    var sys: ApiSys?,
    var dtTxt: String?
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readValue(Int::class.java.classLoader) as? Int,
        TODO("main"),
        TODO("weather"),
        parcel.readParcelable(ApiClouds::class.java.classLoader),
        TODO("wind"),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        TODO("sys"),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(dt)
        parcel.writeParcelable(clouds, flags)
        parcel.writeValue(visibility)
        parcel.writeValue(pop)
        parcel.writeString(dtTxt)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ApiList> {
        override fun createFromParcel(parcel: Parcel): ApiList {
            return ApiList(parcel)
        }

        override fun newArray(size: Int): Array<ApiList?> {
            return arrayOfNulls(size)
        }
    }
}