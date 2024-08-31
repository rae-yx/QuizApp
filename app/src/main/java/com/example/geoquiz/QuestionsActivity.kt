package com.example.geoquiz

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.view.View.OnClickListener
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.geoquiz.databinding.ActivityMainBinding
import com.example.geoquiz.databinding.ActivityQuestionsBinding
import java.util.concurrent.TimeUnit

class QuestionsActivity : AppCompatActivity(), OnClickListener {

    private var mCurrentPosition: Int = 1
    private var mQuestionList: ArrayList<Questions>? = null
    private var mSelectedOptionPosition: Int = 0
    private var mCorrectAnswers: Int = 0
    private var mUserName: String? = null
    private var mCheatTokens: Int = 3
    private var mCheatAttempts: Int = 0
    private lateinit var binding: ActivityQuestionsBinding

    private lateinit var countDownTimer: CountDownTimer
    private var timerRunning: Boolean = false
    private var timerMillisecondsRemaining: Long = 0
    private val quizDuration = 1800000L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuestionsBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        mUserName = intent.getStringExtra(Constants.USER_NAME)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        mQuestionList = Constants.getQuestions()

        if (savedInstanceState != null) {
            timerRunning = savedInstanceState.getBoolean("timerRunning", false)
            timerMillisecondsRemaining = savedInstanceState.getLong("timerMillisecondsRemaining", 0)
            if (timerRunning) {
                startTimer(timerMillisecondsRemaining)
            }
        } else {
            startTimer(quizDuration)
        }

        setQuestion()

        binding.optionOne.setOnClickListener(this)
        binding.optionTwo.setOnClickListener(this)
        binding.optionThree.setOnClickListener(this)
        binding.optionFour.setOnClickListener(this)
        binding.submitButton.setOnClickListener(this)
        binding.previousButton.setOnClickListener(this)
        binding.exitButton.setOnClickListener(this)
        binding.resetButton.setOnClickListener { resetQuiz() }
        binding.cheatButton.setOnClickListener { cheat() }

        updateCheatTokens()
    }

    private fun startTimer(duration: Long) {
        countDownTimer = object : CountDownTimer(duration, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timerMillisecondsRemaining = millisUntilFinished
                val timeLeft = String.format(
                    "%02d:%02d",
                    TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) % 60,
                    TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % 60
                )
                binding.timer.text = timeLeft
            }

            override fun onFinish() {
                endQuiz()
            }
        }.start()

        timerRunning = true
    }

    override fun onDestroy() {
        super.onDestroy()
        countDownTimer.cancel()
        timerRunning = false
    }

    private fun endQuiz() {
        val intent = Intent(this, ResultActivity::class.java)
        intent.putExtra(Constants.USER_NAME, mUserName)
        intent.putExtra(Constants.CORRECT_ANSWERS, mCorrectAnswers)
        intent.putExtra(Constants.TOTAL_QUESTIONS, mQuestionList!!.size)
        intent.putExtra(Constants.CHEAT_ATTEMPTS, mCheatAttempts)
        startActivity(intent)
        finish()
    }

    private fun setQuestion() {
        val question = mQuestionList!![mCurrentPosition - 1]

        defaultOptionsBg()

        if (mCurrentPosition == mQuestionList!!.size) {
            binding.submitButton.text = "Result Summary"
        } else {
            binding.submitButton.text = "Submit"
        }

        binding.progressBar.progress = mCurrentPosition
        binding.quizProgress.text = "$mCurrentPosition" + "/" + binding.progressBar.max

        binding.questionId.text = question!!.question

        binding.optionOne.text = question.optionOne
        binding.optionTwo.text = question.optionTwo
        binding.optionThree.text = question.optionThree
        binding.optionFour.text = question.optionFour

        enableOptions()
    }

    private fun defaultOptionsBg() {
        val options = ArrayList<TextView>()
        options.add(0, binding.optionOne)
        options.add(1, binding.optionTwo)
        options.add(2, binding.optionThree)
        options.add(3, binding.optionFour)

        for (option in options) {
            option.setTextColor(Color.parseColor("#FF000000"))
            option.typeface = Typeface.DEFAULT
            option.background = ContextCompat.getDrawable(this, R.drawable.option_background)
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.optionOne -> {
                selectedOptionsBg(binding.optionOne, selectedOption = 1)
            }

            R.id.optionTwo -> {
                selectedOptionsBg(binding.optionTwo, selectedOption = 2)
            }

            R.id.optionThree -> {
                selectedOptionsBg(binding.optionThree, selectedOption = 3)
            }

            R.id.optionFour -> {
                selectedOptionsBg(binding.optionFour, selectedOption = 4)
            }

            R.id.submitButton -> {
                if (mSelectedOptionPosition == 0) {
                    mCurrentPosition++
                    when {
                        mCurrentPosition <= mQuestionList!!.size -> {
                            setQuestion()
                        }

                        else -> {
                            endQuiz()
                        }
                    }
                } else {
                    val question = mQuestionList?.get(mCurrentPosition - 1)
                    if (question!!.correctAnswer != mSelectedOptionPosition) {
                        answerBg(mSelectedOptionPosition, R.drawable.incorrect_option)
                    } else {
                        mCorrectAnswers++
                    }
                    answerBg(question.correctAnswer, R.drawable.correct_option)

                    if (mCurrentPosition == mQuestionList!!.size) {
                        binding.submitButton.text = "Result Summary"
                    } else {
                        binding.submitButton.text = "Next Question"
                    }
                    mSelectedOptionPosition = 0

                    disableOptions()
                }
            }

            R.id.previousButton -> {
                if (mCurrentPosition > 1) {
                    mCurrentPosition--
                    setQuestion()
                } else {
                    Toast.makeText(this, "You are at the first question", Toast.LENGTH_SHORT).show()
                }
            }

            R.id.exitButton -> {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Exit Quiz")
                builder.setMessage("Are you sure you want to exit the quiz?")
                builder.setPositiveButton("Exit") { dialog, which ->
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
                builder.setNegativeButton("Cancel") { dialog, which ->
                    dialog.dismiss()
                }
                val dialog = builder.create()
                dialog.show()
            }
        }
    }

    private fun selectedOptionsBg(textView: TextView, selectedOption: Int) {
        defaultOptionsBg()
        mSelectedOptionPosition = selectedOption

        textView.setTextColor(Color.parseColor("#FF000000"))
        textView.setTypeface(textView.typeface, Typeface.BOLD)
        textView.background =
            ContextCompat.getDrawable(this, R.drawable.option_selected_background)
    }

    private fun answerBg(answer: Int, drawableView: Int) {
        when (answer) {
            1 -> {
                binding.optionOne.background = ContextCompat.getDrawable(this, drawableView)
            }

            2 -> {
                binding.optionTwo.background = ContextCompat.getDrawable(this, drawableView)
            }

            3 -> {
                binding.optionThree.background = ContextCompat.getDrawable(this, drawableView)
            }

            4 -> {
                binding.optionFour.background = ContextCompat.getDrawable(this, drawableView)
            }
        }
    }

    private fun resetQuiz() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Reset Quiz")
        builder.setMessage("Are you sure you want to reset the quiz?")
        builder.setPositiveButton("Reset") { dialog, which ->
            mCurrentPosition = 1
            mCorrectAnswers = 0
            mSelectedOptionPosition = 0
            mCheatTokens = 3
            mCheatAttempts = 0
            setQuestion()
            updateCheatTokens()
            binding.cheatButton.isEnabled = true
            countDownTimer.cancel()
            startTimer(quizDuration)
            Toast.makeText(this, "Quiz reset!", Toast.LENGTH_SHORT).show()
        }
        builder.setNegativeButton("Cancel") { dialog, which ->
            dialog.dismiss()
        }
        val dialog = builder.create()
        dialog.show()
    }

    private fun cheat() {
        if (mCheatTokens > 0) {
            val question = mQuestionList?.get(mCurrentPosition - 1)
            answerBg(question!!.correctAnswer, R.drawable.correct_option)
            mCheatTokens--
            updateCheatTokens()
            mCheatAttempts++
            if (mCheatTokens == 0) {
                binding.cheatButton.isEnabled = false
            }
        }
    }

    private fun updateCheatTokens() {
        binding.cheatTokens.text = "Cheat tokens remaining: $mCheatTokens"
    }

    private fun disableOptions() {
        binding.optionOne.isEnabled = false
        binding.optionTwo.isEnabled = false
        binding.optionThree.isEnabled = false
        binding.optionFour.isEnabled = false
    }

    private fun enableOptions() {
        binding.optionOne.isEnabled = true
        binding.optionTwo.isEnabled = true
        binding.optionThree.isEnabled = true
        binding.optionFour.isEnabled = true
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putInt("currentPosition", mCurrentPosition)
        outState.putInt("correctAnswers", mCorrectAnswers)
        outState.putInt("selectedOptionPosition", mSelectedOptionPosition)
        outState.putString("userName", mUserName)
        outState.putInt("cheatTokens", mCheatTokens)
        outState.putInt("cheatAttempts" , mCheatAttempts)

        outState.putBoolean("timerRunning", timerRunning)
        outState.putLong("timerMillisecondsRemaining", timerMillisecondsRemaining)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        mCurrentPosition = savedInstanceState.getInt("currentPosition")
        mCorrectAnswers = savedInstanceState.getInt("correctAnswers")
        mSelectedOptionPosition = savedInstanceState.getInt("selectedOptionPosition")
        mUserName = savedInstanceState.getString("userName")
        mCheatTokens = savedInstanceState.getInt("cheatTokens")
        mCheatAttempts = savedInstanceState.getInt("cheatAttempts")

        timerRunning = savedInstanceState.getBoolean("timerRunning")
        timerMillisecondsRemaining = savedInstanceState.getLong("timerMillisecondsRemaining")
        setQuestion()
        updateCheatTokens()
    }
}
