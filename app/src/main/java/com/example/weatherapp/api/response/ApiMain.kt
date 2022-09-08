package com.example.weatherapp.api.response

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class ApiMain(
    var temp: Double?,
    @SerializedName("feels_like")
    var feelsLike: Double?,
    @SerializedName("temp_min")
    var tempMin: Double?,
    @SerializedName("temp_max")
    var tempMax: Double?,
    var pressure: Int?,
    @SerializedName("sea_level")
    var seaLevel: Int?,
    @SerializedName("grnd_level")
    var grndLevel: Int?,
    var humidity: Int?,
    @SerializedName("temp_kf")
    var tempKf: Double?
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Double::class.java.classLoader) as? Double
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(temp)
        parcel.writeValue(feelsLike)
        parcel.writeValue(tempMin)
        parcel.writeValue(tempMax)
        parcel.writeValue(pressure)
        parcel.writeValue(seaLevel)
        parcel.writeValue(grndLevel)
        parcel.writeValue(humidity)
        parcel.writeValue(tempKf)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ApiMain> {
        override fun createFromParcel(parcel: Parcel): ApiMain {
            return ApiMain(parcel)
        }

        override fun newArray(size: Int): Array<ApiMain?> {
            return arrayOfNulls(size)
        }
    }
}