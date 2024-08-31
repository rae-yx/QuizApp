package com.example.geoquiz

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.geoquiz.databinding.ActivityMainBinding
import com.example.geoquiz.databinding.ActivityResultBinding

class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        val username = intent.getStringExtra(Constants.USER_NAME)
        binding.usernameResult.text = username
        val totalQuestions = intent.getIntExtra(Constants.TOTAL_QUESTIONS, 0)
        val correctAnswers = intent.getIntExtra(Constants.CORRECT_ANSWERS, 0)
        val cheatAttempts = intent.getIntExtra(Constants.CHEAT_ATTEMPTS, 0)
        val score = (correctAnswers.toDouble() / totalQuestions.toDouble()) * 100
        val resultMessage = "Quiz completed!\nYour score: ${String.format("%.2f", score)}%"

        binding.scoreResult.text = "Your Score is $correctAnswers / $totalQuestions"
        binding.questionsAnsweredResult.text = "Questions Answered: $totalQuestions"
        binding.cheatAttemptsResult.text = "Cheat Attempts: $cheatAttempts"

        Toast.makeText(this, resultMessage, Toast.LENGTH_LONG).show()

        binding.closeButton.setOnClickListener{
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}