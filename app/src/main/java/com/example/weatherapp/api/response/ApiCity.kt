package com.example.weatherapp.api.response

import android.os.Parcel
import android.os.Parcelable

data class ApiCity(
    var id: Int?,
    var name: String?,
    var coord: ApiCoord?,
    var country: String?,
    var population: Int?,
    var timezone: Int?,
    var sunrise: Int?,
    var sunset: Int?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readParcelable(ApiCoord::class.java.classLoader),
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(id)
        parcel.writeString(name)
        parcel.writeParcelable(coord, flags)
        parcel.writeString(country)
        parcel.writeValue(population)
        parcel.writeValue(timezone)
        parcel.writeValue(sunrise)
        parcel.writeValue(sunset)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ApiCity> {
        override fun createFromParcel(parcel: Parcel): ApiCity {
            return ApiCity(parcel)
        }

        override fun newArray(size: Int): Array<ApiCity?> {
            return arrayOfNulls(size)
        }
    }
}

