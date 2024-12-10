package com.example.assignment1

import com.example.assignment1.QuizModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.Locale

interface QuizService {
    @GET("api.php")
    fun getQuiz(
        @Query("amount") amount:Int,
        @Query("category")category:Int?,
        @Query("difficulty")difficulty:String?,
        @Query("type") type:String?
    ):Call<QuizResponse>

    @GET("api_category.php")
    fun getCategories(): Call<CategoryResponse>
}

interface QuestionStatsService{
    @GET("api_count_global.php")
    fun getData():Call<QuizModel>
}