package com.example.geoquiz

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.geoquiz.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.buttonStart.setOnClickListener{
            if(binding.editName.text.toString().isEmpty()){
                Toast.makeText(this,"Enter your name",Toast.LENGTH_SHORT).show()
            }else{
                val intent = Intent(this, QuestionsActivity::class.java)
                intent.putExtra(Constants.USER_NAME, binding.editName.text.toString())
                startActivity(intent)
                finish()
            }
        }
    }
}