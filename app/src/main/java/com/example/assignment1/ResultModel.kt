
package com.example.assignment1

import android.os.Parcel
import android.os.Parcelable

data class ResultModel(
    val question: String = "",
    val selectedAnswer: String = "",
    val correctAnswer: String = "",
    val correct: Boolean = false,
    val score: Double = 0.0,  // Ensure score is a Double
    val time: Int = 0,
    val stability: Int = 0,
    val timeBonus: Int = 0
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readByte() != 0.toByte(),
        parcel.readDouble(),  // Read score as Double
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(question)
        parcel.writeString(selectedAnswer)
        parcel.writeString(correctAnswer)
        parcel.writeByte(if (correct) 1 else 0)
        parcel.writeDouble(score)  // Write score as Double
        parcel.writeInt(time)
        parcel.writeInt(stability)
        parcel.writeInt(timeBonus)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<ResultModel> {
        override fun createFromParcel(parcel: Parcel) = ResultModel(parcel)
        override fun newArray(size: Int) = arrayOfNulls<ResultModel>(size)
    }
}
