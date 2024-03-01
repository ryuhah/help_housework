package com.example.help_housework

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.help_housework.databinding.ActivityMainBinding
import com.example.help_housework.databinding.FragmentHomeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class HomeFragment : Fragment() {
    private var mBinding : FragmentHomeBinding ?= null

    private val binding get() = mBinding!!
    private lateinit var database : DatabaseReference
    private lateinit var auth : FirebaseAuth

    private val notiWriteList = ArrayList<HomeWrite>()
    private val notiAdapter = HomeAdapter(notiWriteList)


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root

        database = FirebaseDatabase.getInstance().reference
        auth = FirebaseAuth.getInstance()

        // 공지사항 RecyclerView설정
        binding.rvHome.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
        binding.rvHome.setHasFixedSize(true)

        // 공지사항 adapter설정
        binding.rvHome.adapter = notiAdapter

        fetchNoticePosts()

        // 공지사항 추가하기버튼
        binding.btnNotiH.setOnClickListener {
            val intent = Intent(requireContext(),AddNoticeActivity::class.java)
            startActivityForResult(intent, ADD_HOME_REQUEST_CODE)
        }


        val workWriteList = arrayListOf(
            WorkWrite("거실청소",R.drawable.tv_circle),
            WorkWrite("방청소",R.drawable.tv_circle),
            WorkWrite("설거지",R.drawable.tv_circle),
            WorkWrite("화장실청소",R.drawable.tv_circle)

        )
        // 할일추가하기 RecyclerView설정
        binding.rvWork.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL,false)
        binding.rvWork.setHasFixedSize(true)

        // 할일추가하기 adapter설정
        binding.rvWork.adapter = WorkAdapter(workWriteList)

        // 할일추가하기버튼
        binding.tvAddwork.setOnClickListener {
            val intent = Intent(requireContext(),AddWorkActivity::class.java)
            startActivity(intent)
        }
        return view
    }

    // DB에서 초대 코드 가져오기
    private fun fetchNoticePosts(){
        val invitationRef = database.child("invitations")
        invitationRef.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    for (invitationSnapshot in snapshot.children){
                        val invitationCode = invitationSnapshot.key
                        invitationCode?.let { code ->
                            checkCurrentUserInInvitationToDB(code)
                        }
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    // DB 현재 접속한 사람의 초대코드 확인하기
    private fun checkCurrentUserInInvitationToDB(code: String){
        val meetupsRef = database.child("meetups").child(code)
        meetupsRef.child("users").addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for (userSnapshot in snapshot.children){
                    val userId = userSnapshot.key
                    userId?.let { uid ->
                        if (uid == auth.currentUser?.uid){
                            ImportNoticeWriteToDB(meetupsRef)
                            return
                        }
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    // 현재 접속한 사람의 초대코드가 동일한경우 notice_write 글 가져오기
    private fun ImportNoticeWriteToDB(meetupsRef: DatabaseReference){
        val noticeRef = meetupsRef.child("notice_write")
        noticeRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val noticeList = arrayListOf<HomeWrite>()
                for (postSnapshot in snapshot.children){
                    val writer = postSnapshot.child("writerName").value.toString()
                    val content = postSnapshot.child("content").value.toString()
                    val date = postSnapshot.child("date").value.toString()

                    val noticePost = HomeWrite(writer, content, date)
                    noticeList.add(noticePost)
                }
                notiAdapter.setNoticeList(noticeList)
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })

    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ADD_HOME_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val writer = data?.getStringExtra("writer")
            val content = data?.getStringExtra("contenth")
            val date = data?.getStringExtra("dateh")

            // 데이터를 리스트에 추가하고 RecyclerView 갱신
            if (writer != null && content != null && date != null) {
                val newItem = HomeWrite(writer, content, date)
                notiWriteList.add(newItem)
                notiAdapter.notifyDataSetChanged()
            }
        }
    }


    companion object{
        const val ADD_HOME_REQUEST_CODE = 101
    }



}