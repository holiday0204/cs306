package com.example.assignment1

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.example.assignment1.QuizActivity
import com.example.assignment1.GridAdapter
import com.example.assignment1.Base
import com.example.assignment1.Constants
import com.example.assignment1.QuizModel
import com.example.assignment1.QuizResponse
import com.example.assignment1.QuestionStatsService
import com.example.assignment1.QuizService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class QuizClass(private val context: Context) {

    fun getQuizList(amount: Int, category: Int?, difficulty: String?, type: String?) {
        val sharedPreferences = context.getSharedPreferences("quiz_prefs", Context.MODE_PRIVATE)
        val timer = sharedPreferences.getInt("timerValue", 20) // Default to 20 if not found

        if (Constants.isNetworkAvailable(context)) {
            val pbDialog = Base.showProgressBar(context)
            val retrofit: Retrofit = Retrofit.Builder()
                .baseUrl("https://opentdb.com/")
                .addConverterFactory(GsonConverterFactory.create()).build()

            val service: QuizService = retrofit.create(QuizService::class.java)
            val dataCall: Call<QuizResponse> = service.getQuiz(
                amount,
                category,
                difficulty,
                type,
            )

            dataCall.enqueue(object : Callback<QuizResponse> {
                override fun onResponse(
                    call: Call<QuizResponse>,
                    response: Response<QuizResponse>
                ) {
                    pbDialog.cancel()
                    if (response.isSuccessful) {
                        val responseData: QuizResponse = response.body()!!
                        val questionList = ArrayList(responseData.results)
                        if (questionList.isNotEmpty()) {
                            // Pass the quiz data and the timer value to QuizActivity
                            val intent = Intent(context, QuizActivity::class.java)
                            intent.putExtra("questionList", questionList)
                            intent.putExtra("timer", timer) // Pass the timer value to the next activity
                            context.startActivity(intent)
                        } else {
                            Base.showToast(
                                context,
                                "We are sorry, but currently we don't have $amount question for this particular selection"
                            )
                            Log.e("debug", responseData.toString())
                        }
                    } else {
                        Base.showToast(context, "Error Code: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<QuizResponse>, t: Throwable) {
                    pbDialog.cancel()
                    Base.showToast(context, "CallBack Failure")
                }
            })
        } else {
            Base.showToast(context, "Network is not Available")
        }
    }

    fun setRecyclerView(recycleView: RecyclerView?) {
        if (Constants.isNetworkAvailable(context)) {
            val pbDialog = Base.showProgressBar(context)
            val retrofit: Retrofit = Retrofit.Builder()
                .baseUrl("https://opentdb.com/")
                .addConverterFactory(GsonConverterFactory.create()).build()

            val service: QuestionStatsService = retrofit.create(QuestionStatsService::class.java)
            val dataCall: Call<QuizModel> = service.getData()
            dataCall.enqueue(object : Callback<QuizModel> {
                override fun onResponse(
                    call: Call<QuizModel>,
                    response: Response<QuizModel>
                ) {
                    pbDialog.cancel()
                    if (response.isSuccessful) {
                        val questionStats: QuizModel = response.body()!!
                        val categoryMap = questionStats.categories
                        val adapter = GridAdapter(Constants.getCategoryItemList(), categoryMap)
                        recycleView?.adapter = adapter
                        adapter.setOnClickListener(object : GridAdapter.OnClickListener {
                            override fun onClick(id: Int) {
                                getQuizList(10, id, null, null)
                            }
                        })
                    } else {
                        Base.showToast(context, "Error Code: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<QuizModel>, t: Throwable) {
                    pbDialog.cancel()
                    Base.showToast(context, "Network is not Available")
                }
            })
        }
    }
}
