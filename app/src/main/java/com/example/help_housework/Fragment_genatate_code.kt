package com.example.help_housework

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.help_housework.databinding.FragmentGenerateCodeBinding
import androidx.core.content.getSystemService
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class Fragment_genatate_code : Fragment() {
    private var mBinding : FragmentGenerateCodeBinding ?= null
    private val binding get() = mBinding!!

    private var invitationCode : String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?, ): View? {
        mBinding = FragmentGenerateCodeBinding.inflate(inflater,container,false)
        val view = binding.root

        // 랜덤 코드 생성
        binding.btnGenerateG.setOnClickListener {
            invitationCode  = generateInvitationCode()
            binding.tvCodeG.text = invitationCode
        }ㅅㄷㄴㅅ

        // 코드 클립보드 복사
        binding.tvCodeG.setOnClickListener {
            if(binding.tvCodeG.text != "초대코드"){
                val clipboard = requireContext().getSystemService<ClipboardManager>()
                clipboard?.let {
                    it.setPrimaryClip(ClipData.newPlainText("Code", binding.tvCodeG.text))
                    Toast.makeText(requireContext(),"코드가 복사되었습니다", Toast.LENGTH_SHORT).show()
                }
            }

        }

        binding.btnStartG.setOnClickListener {
            // 초대 코드가 생성되었는지 확인 후 사용
            if(!invitationCode.isNullOrBlank()){
                // 현재 사용자 Uid 가져오기
                val currentUserUid = FirebaseAuth.getInstance().currentUser?.uid

                // firebase에 초대코드 저장
                val invitationsRef = FirebaseDatabase.getInstance().reference.child("invitations")
                invitationsRef.child(invitationCode!!).setValue(invitationCode)

                // 방에 사용자 추가
                if(currentUserUid !=null){
                    val userRef = FirebaseDatabase.getInstance().reference.child("meetups").child(invitationCode!!).child("users")
                    userRef.child(currentUserUid).setValue(true)
                }

                Toast.makeText(requireContext(),"방이 생성되었습니다",Toast.LENGTH_SHORT).show()

                val intent = Intent(requireContext(), MainActivity::class.java)
                startActivity(intent)
                requireActivity().finish()

            }else {
                Toast.makeText(requireContext(),"초대 코드가 생성되지 않았습니다",Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }
    fun generateInvitationCode(): String{
        val charPool : List<Char> = ('a'..'z')+('A'..'Z')+('0'..'9')
        return (1..6)
            .map{charPool.random()}
            .joinToString("")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mBinding = null
    }
}