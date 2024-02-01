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
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database

class JoinActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var database : DatabaseReference
    private var mBinding : ActivityJoinBinding ?= null
    private val binding get() = mBinding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityJoinBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth
        database = Firebase.database.reference

        val relationshipList = listOf("선택하세요","아빠", "엄마", "아들", "딸")
        val adapter = ArrayAdapter<String>(this, R.layout.spinner_item, relationshipList)
        binding.spinner.adapter = adapter

        binding.btnJoinJ.setOnClickListener{
            val id = binding.etIdJ.text.toString()
            val pw = binding.etPwJ.text.toString()
            val name = binding.etNameJ.text.toString()
            val selectRelationship = binding.spinner.selectedItem.toString()

            val userAccount = UserAccount(id, pw, name, selectRelationship)

            if (id.isBlank() || pw.isBlank() || name.isBlank() || selectRelationship == "선택하세요"){
                Toast.makeText(this, "모든 내용을 입력해주세요", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            auth.createUserWithEmailAndPassword(id, pw)
                .addOnCompleteListener{task ->
                    if(task.isSuccessful){
                        addUserToDatabase(userAccount)
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

    private fun addUserToDatabase (userAccount: UserAccount){
        val usersRef = database.child("users")
        val userId = usersRef.push().key
        userId?.let{
            usersRef.child(it).setValue(userAccount)
        }
    }
}