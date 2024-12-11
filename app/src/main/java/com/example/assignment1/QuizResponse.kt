package com.example.assignment1

import java.io.Serializable

data class QuizResponse(
    val response_code: Int,
    val results: List<QuizResult>
)

data class QuizResult(
    val category: String,
    val type: String,
    val difficulty: String,
    val question: String,  // The question text
    val correct_answer: String,  // The correct answer for the question
    val incorrect_answers: List<String> // The incorrect answers
) : Serializable