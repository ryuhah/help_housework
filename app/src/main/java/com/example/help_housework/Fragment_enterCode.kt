package com.example.help_housework

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.help_housework.databinding.FragmentEnterCodeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Fragment_enterCode : Fragment() {
    private var mBinding : FragmentEnterCodeBinding ?= null
    private val binding get() = mBinding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?, ): View? {
        mBinding = FragmentEnterCodeBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.btnStartE.setOnClickListener {
            val invitationCode = binding.etCodeE.text.toString()

            if(invitationCode.isBlank()){
                Toast.makeText(requireContext(),"초대코드를 입력해주세요",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            checkInvitationCode(invitationCode)

        }

        return view
    }

    private fun checkInvitationCode(enteredCode: String){
        val invitationsRef = FirebaseDatabase.getInstance().reference.child("invitations")
        invitationsRef.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.hasChild(enteredCode)){
                    // 데이터베이스에서 코드를 찾았을 경우
                    val meetupId = snapshot.child(enteredCode).getValue(String::class.java)
                    if(!meetupId.isNullOrBlank()){
                        addUserToDatabase(meetupId)
                    } else {
                        // meetupId가 null이거나 비어있을 경우 처리
                        Toast.makeText(requireContext(),"유효하지않은 초대코드입니다",Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(requireContext(),"유효하지않은 초대코드입니다",Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // 에러처리
            }
        })
    }

    private fun addUserToDatabase(meetupId: String){
        val currentUserUid = FirebaseAuth.getInstance().currentUser?.uid

        if(!currentUserUid.isNullOrBlank()){
            val userRef = FirebaseDatabase.getInstance().reference.child("meetups").child(meetupId).child("users")
            userRef.child(currentUserUid).setValue(true)

            val intent = Intent(requireContext(), MainActivity::class.java)
            startActivity(intent)
            requireActivity().finish()

        } else {
            Toast.makeText(requireContext(),"로그인 상태를 확인할 수 없습니다",Toast.LENGTH_SHORT).show()
        }
    }
}