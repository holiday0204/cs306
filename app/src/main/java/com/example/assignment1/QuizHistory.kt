package com.example.assignment1

import android.os.Parcel
import android.os.Parcelable

data class QuizHistory(
    val userId: String,
    val date: Long = 0L,   // This should be a Long
    val score: Double = 0.0,
    val questionsAnswered: Int = 0,
    val totalQuestions: Int = 0,
    val quizResults: List<ResultModel> = emptyList()  // Use List<ResultModel> directly
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readLong(),  // Ensure we're using readLong() here for Long values
        parcel.readDouble(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.createTypedArrayList(ResultModel) ?: emptyList()  // Deserialize ResultModel list correctly
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(userId)
        parcel.writeLong(date)  // Ensure we write a Long value here
        parcel.writeDouble(score)
        parcel.writeInt(questionsAnswered)
        parcel.writeInt(totalQuestions)
        parcel.writeTypedList(quizResults)  // Serialize List<ResultModel> correctly
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<QuizHistory> {
        override fun createFromParcel(parcel: Parcel): QuizHistory {
            return QuizHistory(parcel)
        }

        override fun newArray(size: Int): Array<QuizHistory?> {
            return arrayOfNulls(size)
        }
    }
}
