package com.example.help_housework

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.help_housework.databinding.ActivityJoinBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

private lateinit var auth: FirebaseAuth
class JoinActivity : AppCompatActivity() {
    private var mBinding : ActivityJoinBinding ?= null
    private val binding get() = mBinding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityJoinBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth

        binding.btnJoinJ.setOnClickListener{
            val id = binding.etIdJ.text.toString()
            val pw = binding.etPwJ.text.toString()

            auth.createUserWithEmailAndPassword(id, pw)
                .addOnCompleteListener{task ->
                    if(task.isSuccessful){
                        Toast.makeText(this,"회원가입에 성공했습니다!", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, LoginActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this, "회원가입에 실패했습니다.${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }
}