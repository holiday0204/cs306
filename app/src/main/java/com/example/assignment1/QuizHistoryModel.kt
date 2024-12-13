package com.example.assignment1

import android.os.Parcel
import android.os.Parcelable

data class QuizHistoryModel(
    val date: Long = 0L,
    val score: Double = 0.0,
    val questionsAnswered: Int = 0,
    val totalQuestions: Int = 0,
    val quizResults: List<ResultModel> = listOf()
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readDouble(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.createTypedArrayList(ResultModel.CREATOR) ?: listOf()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(date)
        parcel.writeDouble(score)
        parcel.writeInt(questionsAnswered)
        parcel.writeInt(totalQuestions)
        parcel.writeTypedList(quizResults)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<QuizHistoryModel> {
        override fun createFromParcel(parcel: Parcel) = QuizHistoryModel(parcel)
        override fun newArray(size: Int) = arrayOfNulls<QuizHistoryModel>(size)
    }
}