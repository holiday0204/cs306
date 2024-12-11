package com.example.assignment1

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.view.View.OnClickListener
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.assignment1.databinding.ActivityQuizBinding

class QuizActivity : AppCompatActivity() {
    private var binding:ActivityQuizBinding? = null
    private lateinit var questionList:ArrayList<QuizResult>
    private var position = 0
    private var allowPlaying = true
    private var timer:CountDownTimer? = null
    private val resultList = ArrayList<ResultModel>()
    private var timeLeft = 0
    private var score = 0.0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuizBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        questionList = intent.getSerializableExtra("questionList") as ArrayList<QuizResult>
        binding?.pbProgress?.max = questionList.size
        setQuestion()
        setOptions()
        startTimer()
        binding?.tvProgress?.text = "1/${questionList.size}"
        binding?.btnNext?.setOnClickListener {
            onNext()
        }
        val redBg = ContextCompat.getDrawable(this,R.drawable.red_button_bg)
        val optionClickListener = OnClickListener { view ->
            if (allowPlaying)
            {
                timer?.cancel()
                view.background = redBg
                showCorrectAnswer()
                setScore(view as Button?)
                allowPlaying = false
            }
        }

        binding?.option1?.setOnClickListener(optionClickListener)
        binding?.option2?.setOnClickListener(optionClickListener)
        binding?.option3?.setOnClickListener(optionClickListener)
        binding?.option4?.setOnClickListener(optionClickListener)

    }

    private fun onNext() {
        // Store the result for the current question
        val resultModel = ResultModel(
            20 - timeLeft,  // Time remaining as a score for this question
            questionList[position].type,
            questionList[position].difficulty,
            score
        )
        resultList.add(resultModel) // Add the result to the list
        score = 0.0  // Reset score for the next question

        if (position < questionList.size - 1) {
            // Move to the next question
            timer?.cancel()
            position++
            setQuestion()
            setOptions()
            binding?.pbProgress?.progress = position + 1
            binding?.tvProgress?.text = "${position + 1}/${questionList.size}"
            resetButtonBackground()
            allowPlaying = true
            startTimer()
        } else {
            // End the game and go to the result screen
            endGame()
        }
    }

    private fun setQuestion()
    {
        val decodedQuestion = Constants.decodeHtmlString(questionList[position].question)
        binding?.tvQuestion?.text = decodedQuestion
    }
    private lateinit var correctAnswer:String
    private lateinit var optionList:List<String>
    private fun setOptions()
    {
        val question = questionList[position]
        val temp = Constants.getRandomOptions(question.correct_answer, question.incorrect_answers)
        optionList = temp.second
        correctAnswer = temp.first
        binding?.option1?.text = optionList[0]
        binding?.option2?.text = optionList[1]
        if (question.type == "multiple") {
            binding?.option3?.visibility = View.VISIBLE
            binding?.option4?.visibility = View.VISIBLE
            binding?.option3?.text = optionList[2]
            binding?.option4?.text = optionList[3]
        }
        else
        {
            binding?.option3?.visibility = View.GONE
            binding?.option4?.visibility = View.GONE
        }
    }

    private fun setScore(button: Button?) {
        // Check if the answer is correct
        if (correctAnswer == button?.text) {
            score = getScore() // Only calculate the score if the answer is correct
        } else {
            score = 0.0 // If the answer is incorrect, set the score to 0
        }
    }

    private fun getScore(): Double {
        // Base score: 1.0 for correct answer, 0.0 for incorrect
        val baseScore = if (correctAnswer == optionList.first { it == correctAnswer }) 1.0 else 0.0

        // Time bonus: If finished on time, give a 5% bonus (0.05 of the score)
        val timeBonus = if (timeLeft > 0) 0.05 else 0.0  // Bonus is 5% if there's still time left

        // Calculate the final score as a percentage (base score + time bonus)
        return (baseScore + timeBonus) * 100  // 100 to express as percentage
    }



//    private fun setScore(button: Button?)
//    {
//        if (correctAnswer==button?.text)
//            score = getScore()
//    }

//    private fun setScore(button: Button?) {
//        if (correctAnswer == button?.text) {
//            score = getScore() // Only calculate the score if the answer is correct
//        } else {
//            score = 0.0 // If the answer is incorrect, set the score to 0
//        }
//    }
//
//
//    private fun getScore(): Double {
//        // Base score for correct answer (1.0) or incorrect answer (0.0)
//        val baseScore = if (correctAnswer == optionList.first { it == correctAnswer }) 1.0 else 0.0
//
//        // Time bonus (calculate the time-based bonus, but it should not be the only factor for correctness)
//        val timeBonus = timeLeft.toDouble() / 20.0  // Maximum time bonus is 1.0 if answered quickly.
//
//        // Difficulty multiplier (difficulty affects the score of the question)
//        val difficultyMultiplier = when (questionList[position].difficulty) {
//            "easy" -> 1.0
//            "medium" -> 2.0
//            else -> 3.0
//        }
//
//        // Final score (Base score + time bonus + difficulty multiplier)
//        return (baseScore + timeBonus) * difficultyMultiplier
//    }


//    private fun getScore():Double
//    {
//        val score1 = when(questionList[position].type){
//            "boolean"-> 0.5
//            else -> 1.0
//        }
//
//        val score2:Double = (timeLeft.toDouble())/(20).toDouble()
//        val score3 = when(questionList[position].difficulty)
//        {
//            "easy"-> 1.0
//            "medium"-> 2.0
//            else-> 3.0
//        }
//
//        return score1+score2+score3
//    }

    private fun showCorrectAnswer()
    {
        val blueBg = ContextCompat.getDrawable(this,R.drawable.blue_button_bg)
        when(true)
        {
            (correctAnswer == optionList[0]) -> binding?.option1?.background = blueBg
            (correctAnswer == optionList[1]) -> binding?.option2?.background = blueBg
            (correctAnswer == optionList[2]) -> binding?.option3?.background = blueBg
            else -> binding?.option4?.background = blueBg
        }
    }

    private fun resetButtonBackground()
    {
        val grayBg = ContextCompat.getDrawable(this, R.drawable.gray_button_bg)
        binding?.option1?.background = grayBg
        binding?.option2?.background = grayBg
        binding?.option3?.background = grayBg
        binding?.option4?.background = grayBg
    }

    private fun startTimer() {
        binding?.circularProgressBar?.max = 20
        binding?.circularProgressBar?.progress = 20
        timer = object : CountDownTimer(20000, 1000) {
            override fun onTick(remaining: Long) {
                // Update progress bar and timer text
                binding?.circularProgressBar?.incrementProgressBy(-1)
                binding?.tvTimer?.text = (remaining / 1000).toString()
                timeLeft = (remaining / 1000).toInt()
            }

            override fun onFinish() {
                showCorrectAnswer()
                allowPlaying = false
            }
        }.start()
    }


    private fun endGame() {
        // Calculate the total score at the end of the quiz
        val finalScore = resultList.sumByDouble { it.score }  // Sum the scores for all questions

        // Pass the result list and final score to the ResultActivity
        val intent = Intent(this, ResultActivity::class.java)
        intent.putParcelableArrayListExtra("resultList", resultList)
        intent.putExtra("finalScore", finalScore)
        startActivity(intent)
        finish() // Close the quiz activity
    }

}