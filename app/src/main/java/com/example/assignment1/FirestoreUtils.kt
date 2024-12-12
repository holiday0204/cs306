package com.example.assignment1

fun ResultModel.toMap(): Map<String, Any> {
    return mapOf(
        "question" to this.question,
        "correctAnswer" to this.correctAnswer,
        "selectedAnswer" to this.selectedAnswer,
        "correct" to this.correct,  // Use `correct` instead of `isCorrect`
        "time" to this.time,
        "score" to this.score,
        "timeBonus" to this.timeBonus
    )
}
