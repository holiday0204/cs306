package com.example.assignment1

import android.os.Parcel
import android.os.Parcelable

data class ResultModel(
    var time: Int,  // Time taken to answer this question (in seconds)
    var type: String?,  // Type of question (e.g., multiple choice, true/false)
    var difficulty: String?,  // Difficulty level of the question (e.g., easy, medium, hard)
    var score: Double,  // Score for this question (1.0 for correct, 0.0 for incorrect)
    var timeBonus: Double = 0.0 // Default value for timeBonus if not passed
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readDouble(),
        parcel.readDouble() // Ensure you read all required data from the parcel
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(time)
        parcel.writeString(type)
        parcel.writeString(difficulty)
        parcel.writeDouble(score)
        parcel.writeDouble(timeBonus) // Write the timeBonus to parcel
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<ResultModel> {
        override fun createFromParcel(parcel: Parcel): ResultModel {
            return ResultModel(parcel)
        }

        override fun newArray(size: Int): Array<ResultModel?> {
            return arrayOfNulls(size)
        }
    }
}
