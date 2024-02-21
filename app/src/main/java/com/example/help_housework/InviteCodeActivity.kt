package com.example.help_housework

import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.help_housework.databinding.ActivityInviteCodeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class InviteCodeActivity : AppCompatActivity() {
    private lateinit var auth : FirebaseAuth
    private lateinit var database : DatabaseReference

    private var mBinding : ActivityInviteCodeBinding ?= null
    private val binding get() = mBinding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference

        mBinding = ActivityInviteCodeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getInvitationCode()
    }

    private fun getInvitationCode() {
        val invitationRef = database.child("invitations")

        invitationRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (invitationSnapshot in dataSnapshot.children) {
                        val invitationCode = invitationSnapshot.key
                        if (invitationCode != null) {
                            getUserUIDFromMeetups(invitationCode)
                        }
                    }
                } else {
                    setFrag(0)
                    binding.btnEnterCode.setTextColor(Color.parseColor("#FFE600"))


                    binding.btnEnterCode.setOnClickListener{
                        setFrag(0)
                        binding.btnEnterCode.setTextColor(Color.parseColor("#FFE600"))
                        binding.btnGenerateCode.setTextColor(Color.WHITE)
                    }

                    binding.btnGenerateCode.setOnClickListener{
                        setFrag(1)
                        binding.btnEnterCode.setTextColor(Color.WHITE)
                        binding.btnGenerateCode.setTextColor(Color.parseColor("#FFE600"))
                    }

                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    private fun getUserUIDFromMeetups(invitationCode : String){
        val meetupsRef = database.child("meetups").child(invitationCode)

        meetupsRef.child("users").addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val currentUserUID = auth.currentUser?.uid

                if (currentUserUID != null && dataSnapshot.hasChild(currentUserUID)){

                    val intent = Intent(this@InviteCodeActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()

                } else {
                    setFrag(0)
                    binding.btnEnterCode.setTextColor(Color.parseColor("#FFE600"))


                    binding.btnEnterCode.setOnClickListener{
                        setFrag(0)
                        binding.btnEnterCode.setTextColor(Color.parseColor("#FFE600"))
                        binding.btnGenerateCode.setTextColor(Color.WHITE)
                    }

                    binding.btnGenerateCode.setOnClickListener{
                        setFrag(1)
                        binding.btnEnterCode.setTextColor(Color.WHITE)
                        binding.btnGenerateCode.setTextColor(Color.parseColor("#FFE600"))
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {

            }
        })
    }



    private fun setFrag(fragNum : Int) {
        val ft = supportFragmentManager.beginTransaction()
        when(fragNum){
            0 -> {
                ft.replace(R.id.invite_frame, Fragment_enterCode()).commit()
            }
            1 -> {
                ft.replace(R.id.invite_frame, Fragment_genatate_code()).commit()
            }
        }
    }
}