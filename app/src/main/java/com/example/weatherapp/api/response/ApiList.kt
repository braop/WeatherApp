package com.example.weatherapp.api.response

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class ApiList(
    var dt: Int?,
    var main: ApiMain?,
    var weather: List<ApiWeather>?,
    var clouds: ApiClouds?,
    var wind: ApiWind?,
    var visibility: Int?,
    var pop: Double?,
    var sys: ApiSys?,
    @SerializedName("dt_txt")
    var dtTxt: String?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readParcelable(ApiMain::class.java.classLoader),
        parcel.createTypedArrayList(ApiWeather),
        parcel.readParcelable(ApiClouds::class.java.classLoader),
        parcel.readParcelable(ApiWind::class.java.classLoader),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readParcelable(ApiSys::class.java.classLoader),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(dt)
        parcel.writeParcelable(main, flags)
        parcel.writeTypedList(weather)
        parcel.writeParcelable(clouds, flags)
        parcel.writeParcelable(wind, flags)
        parcel.writeValue(visibility)
        parcel.writeValue(pop)
        parcel.writeParcelable(sys, flags)
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