package com.example.assignment1

import android.os.Parcel
import android.os.Parcelable

data class ResultModel(
    var question: String,
    var correctAnswer: String,
    var selectedAnswer: String,
    var isCorrect: Boolean,
    var time: Long = 0L,
    var score: Double,
    var timeBonus: Double = 0.0
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readByte() != 0.toByte(),
        parcel.readLong(),
        parcel.readDouble(),
        parcel.readDouble()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(question)
        parcel.writeString(correctAnswer)
        parcel.writeString(selectedAnswer)
        parcel.writeByte(if (isCorrect) 1 else 0)
        parcel.writeLong(time)
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
    fun ResultModel.toMap(): Map<String, Any> {
        return mapOf(
            "question" to this.question,
            "correctAnswer" to this.correctAnswer,
            "selectedAnswer" to this.selectedAnswer,
            "isCorrect" to this.isCorrect,
            "time" to this.time,
            "score" to this.score,
            "timeBonus" to this.timeBonus
        )
    }

    fun fromMap(map: Map<String, Any>): ResultModel {
        return ResultModel(
            question = map["question"] as? String ?: "", // Safe cast to String with default empty value
            correctAnswer = map["correctAnswer"] as? String ?: "",
            selectedAnswer = map["selectedAnswer"] as? String ?: "",
            isCorrect = map["isCorrect"] as? Boolean ?: false,  // Safe cast to Boolean with default value
            time = map["time"] as? Long ?: 0L,  // Safe cast to Long with default value
            score = map["score"] as? Double ?: 0.0, // Safe cast to Double with default value
            timeBonus = map["timeBonus"] as? Double ?: 0.0 // Safe cast to Double with default value
        )
    }



}
