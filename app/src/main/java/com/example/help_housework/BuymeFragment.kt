package com.example.help_housework

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.help_housework.databinding.FragmentBuymeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class BuymeFragment : Fragment() {
    private var mBinding : FragmentBuymeBinding ?= null
    private val binding get() = mBinding!!
    private lateinit var database : DatabaseReference
    private lateinit var auth : FirebaseAuth

    private val buymeWriteList = ArrayList<BuymeWrite>()
    private val buymeAdapter = BuymeAdapter(buymeWriteList)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding = FragmentBuymeBinding.inflate(inflater, container, false)
        val view=binding.root

        database = FirebaseDatabase.getInstance().reference
        auth = FirebaseAuth.getInstance()

        // RecyclerView 설정
        binding.rvBuyme.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvBuyme.setHasFixedSize(true)

        // Adapter 설정
        binding.rvBuyme.adapter = buymeAdapter

        fetchBuymePosts()

        binding.btnFloating.setOnClickListener {
            val intent = Intent(requireContext(),AddBuymeActivity::class.java)
            startActivityForResult(intent, ADD_BUYME_REQUEST_CODE)
        }

        return view
    }

    private fun fetchBuymePosts() {
        // DB에서 초대 코드 가져오기
        val invitationRef = database.child("invitations")
        invitationRef.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    for (invitationSnapshot in snapshot.children){
                        val invitationCode = invitationSnapshot.key
                        invitationCode?.let { code ->
                            // DB 현재 접속한 사람의 초대코드 확인하기
                            val meetupsRef = database.child("meetups").child(code)
                            meetupsRef.child("users").addListenerForSingleValueEvent(object : ValueEventListener{
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    for (userSnapshot in snapshot.children){
                                        val userUid = userSnapshot.key
                                        userUid?.let { uid ->
                                            if(uid == auth.currentUser?.uid){
                                                // 현재 접속한사람의 초대코드가 동일한경우 buyme-write 글 가져오기ㅏ
                                                val buymeRef = meetupsRef.child("buyme_write")
                                                buymeRef.addValueEventListener(object : ValueEventListener{
                                                    override fun onDataChange(snapshot: DataSnapshot) {
                                                        val buymeList = mutableListOf<BuymeWrite>()
                                                        for (postSnapshot in snapshot.children){
                                                            val fromUser = postSnapshot.child("fromUser").value.toString()
                                                            val toUser = postSnapshot.child("toUser").value.toString()
                                                            val content = postSnapshot.child("content").value.toString()
                                                            val hyperlink = postSnapshot.child("hyperlink").value.toString()
                                                            val status = postSnapshot.child("status").value.toString()
                                                            val date = postSnapshot.child("date").value.toString()

                                                            val buymePost = BuymeWrite(fromUser, toUser, content, hyperlink, status, date)
                                                            buymeList.add(buymePost)
                                                        }
                                                        buymeAdapter.setBuymeList(buymeList)
                                                    }

                                                    override fun onCancelled(error: DatabaseError) {

                                                    }
                                                })
                                                return
                                            }
                                        }
                                    }
                                }

                                override fun onCancelled(error: DatabaseError) {
                                }
                            })

                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ADD_BUYME_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val fromUser = data?.getStringExtra("fromUser")
            val toUser = data?.getStringExtra("toUser")
            val content = data?.getStringExtra("content")
            val hyperlink = data?.getStringExtra("hyperlink")
            val status = data?.getStringExtra("status")
            val date = data?.getStringExtra("date")

            // 데이터를 리스트에 추가하고 RecyclerView 갱신
            if (fromUser != null && toUser != null && content != null && hyperlink != null && status != null && date != null) {
                val newItem = BuymeWrite(fromUser, toUser, content, hyperlink, status, date)
                buymeWriteList.add(newItem)
                buymeAdapter.notifyDataSetChanged()
            }
        }
    }


    companion object{
        const val ADD_BUYME_REQUEST_CODE = 100
    }

}