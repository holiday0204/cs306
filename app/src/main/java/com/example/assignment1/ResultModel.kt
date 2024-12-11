package com.example.assignment1

import android.os.Parcel
import android.os.Parcelable

data class ResultModel(
    var question: String,
    var correctAnswer: String,
    var selectedAnswer: String,
    var isCorrect: Boolean,
    var time: Int,
    var score: Double,
    var timeBonus: Double = 0.0
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readByte() != 0.toByte(),
        parcel.readInt(),
        parcel.readDouble(),
        parcel.readDouble()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(question)
        parcel.writeString(correctAnswer)
        parcel.writeString(selectedAnswer)
        parcel.writeByte(if (isCorrect) 1 else 0)
        parcel.writeInt(time)
        parcel.writeDouble(score)
        parcel.writeDouble(timeBonus)
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
