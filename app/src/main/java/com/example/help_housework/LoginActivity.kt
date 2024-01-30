package com.example.help_housework

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.help_housework.databinding.ActivityLoginBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class LoginActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private var mBinding : ActivityLoginBinding ? = null
    private val binding get() = mBinding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth

        binding.btnLogin.setOnClickListener{
            val id = binding.etIdL.text.toString()
            val pw = binding.etPwL.text.toString()

            auth.signInWithEmailAndPassword(id, pw)
                .addOnCompleteListener {task ->
                    if(task.isSuccessful){
                        Toast.makeText(this, "로그인에 성공했습니다!", Toast.LENGTH_SHORT).show()
                        val mainintent = Intent(this, MainActivity::class.java)
                        startActivity(mainintent)
                        finish()
                    } else {
                        Toast.makeText(this, "아이디와 비밀번호를 확인해주세요.", Toast.LENGTH_SHORT).show()
                    }
                }
        }

        binding.tvJoinL.setOnClickListener{
            val joinintent = Intent(this, JoinActivity::class.java)
            startActivity(joinintent)
        }
    }
}