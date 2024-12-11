package com.example.assignment1

data class QuizHistory(
    val userId: String = "",
    val date: Long = 0L,  // Timestamp of the quiz attempt
    val score: Double = 0.0,
    val questionsAnswered: Int = 0,
    val totalQuestions: Int = 0,
    val quizResults: List<ResultModel> = listOf()  // This holds the individual question results
)

