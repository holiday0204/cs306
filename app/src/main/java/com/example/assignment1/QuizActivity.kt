package com.example.assignment1

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.view.View.OnClickListener
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.assignment1.databinding.ActivityQuizBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class QuizActivity : AppCompatActivity() {
    private var binding: ActivityQuizBinding? = null
    private lateinit var questionList: ArrayList<QuizResult>
    private var position = 0
    private var allowPlaying = true
    private var timer: CountDownTimer? = null
    private val resultList = ArrayList<ResultModel>()
    private var timeLeft = 0
    private var score = 0.0

    // Declare a variable to hold the selected answer
    private lateinit var selectedAnswer: String

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

        val redBg = ContextCompat.getDrawable(this, R.drawable.red_button_bg)
        val optionClickListener = View.OnClickListener { view ->
            if (allowPlaying) {
                timer?.cancel()
                view.background = redBg
                showCorrectAnswer()
                setScore(view as Button?)
                allowPlaying = false

                // Capture the selected answer
                selectedAnswer = (view as Button).text.toString()  // Store the text of the clicked button as selectedAnswer
            }
        }

        // Set the click listener for all options
        binding?.option1?.setOnClickListener(optionClickListener)
        binding?.option2?.setOnClickListener(optionClickListener)
        binding?.option3?.setOnClickListener(optionClickListener)
        binding?.option4?.setOnClickListener(optionClickListener)
    }

    private fun onNext() {
        // Get the current question and its options
        val currentQuestion = questionList[position]

        // Create the result model for the current question
        val resultModel = ResultModel(
            question = currentQuestion.question,  // Pass the question text
            correctAnswer = currentQuestion.correct_answer,  // Pass the correct answer
            selectedAnswer = selectedAnswer,  // Pass the user's selected answer
            isCorrect = selectedAnswer == currentQuestion.correct_answer,  // Check if the user's selected answer is correct
            time = 20 - timeLeft,  // Time remaining as a score for this question
            score = score,  // Score for this question
            timeBonus = 0.0  // Assuming no timeBonus for now, adjust if needed
        )

        // Add the result to the result list
        resultList.add(resultModel)

        // Reset score for the next question
        score = 0.0

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

    private fun setQuestion() {
        val decodedQuestion = Constants.decodeHtmlString(questionList[position].question)
        binding?.tvQuestion?.text = decodedQuestion
    }

    private lateinit var correctAnswer: String
    private lateinit var optionList: List<String>

    private fun setOptions() {
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
        } else {
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

    private fun showCorrectAnswer() {
        val blueBg = ContextCompat.getDrawable(this, R.drawable.blue_button_bg)
        when (true) {
            (correctAnswer == optionList[0]) -> binding?.option1?.background = blueBg
            (correctAnswer == optionList[1]) -> binding?.option2?.background = blueBg
            (correctAnswer == optionList[2]) -> binding?.option3?.background = blueBg
            else -> binding?.option4?.background = blueBg
        }
    }

    private fun resetButtonBackground() {
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

        // Create the QuizHistory object
        val quizHistory = QuizHistory(
            userId = FirebaseAuth.getInstance().currentUser?.uid ?: "guest",  // Use user ID if logged in, else "guest"
            date = System.currentTimeMillis(),  // Timestamp of the quiz completion
            score = finalScore,  // The total score
            questionsAnswered = resultList.size,  // The number of questions answered
            totalQuestions = questionList.size,  // Total number of questions in the quiz
            quizResults = resultList  // List of detailed results for each question
        )

        // Saving to Firestore
        val db = FirebaseFirestore.getInstance()
        db.collection("quiz_history")
            .add(quizHistory)
            .addOnSuccessListener {
                // Handle success (e.g., show a confirmation toast or transition to the result screen)
                Toast.makeText(this, "Quiz history saved!", Toast.LENGTH_SHORT).show()

                // Transition to ResultActivity to display the results
                val intent = Intent(this, ResultActivity::class.java)
                intent.putParcelableArrayListExtra("resultList", resultList)
                intent.putExtra("finalScore", finalScore)
                startActivity(intent)
                finish()  // Close the quiz activity
            }
            .addOnFailureListener { e ->
                // Handle failure (e.g., show an error toast)
                Toast.makeText(this, "Error saving quiz history: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

}
