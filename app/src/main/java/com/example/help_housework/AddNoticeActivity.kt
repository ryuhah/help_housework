package com.example.help_housework

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.example.help_housework.databinding.ActivityAddNoticeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.Locale

class AddNoticeActivity : AppCompatActivity() {

    private var mBinding : ActivityAddNoticeBinding?= null
    private val binding get() = mBinding!!

    private lateinit var database : DatabaseReference
    private lateinit var auth : FirebaseAuth
    private var writer: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityAddNoticeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar = binding.toolbar2
        setSupportActionBar(toolbar)
        supportActionBar?.title = "공지사항 글쓰기"    // 이부분 공지사항 글쓰기로 수정해주세용
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        database = FirebaseDatabase.getInstance().reference
        auth = FirebaseAuth.getInstance()

        // 공지사항 작성하기 버튼
        binding.btnAddnoti.setOnClickListener{
            val currentUserUid = auth.currentUser?.uid
            currentUserUid?. let { uid ->
                database.child("users").child(uid).addListenerForSingleValueEvent(object : ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if(snapshot.exists()){
                            val user = snapshot.getValue(UserAccount::class.java)
                            user?.let {
                                val writer = it.name
                                createIntent(writer)
                            }
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {

                    }
                })
            }

        }

    }

    private fun createIntent(writer: String?) {
        val content = binding.etContentNoti.text.toString()
        val date = SimpleDateFormat("yyyy.MM.dd", Locale.getDefault()).format(java.util.Date())

        val intent = Intent().apply{
            putExtra("writer", writer)
            putExtra("contenth", content)
            putExtra("dateh",date)
        }

        setResult(Activity.RESULT_OK, intent)
        getInvitationCodetoDB(writer)
        finish()

    }

    // db에 글 정보 추가하기 ----------------------
    // db에 글 정보 추가하기-1 db에서 초대 코드 가져오기
    private fun getInvitationCodetoDB(writer: String?){
        val invitationRef = database.child("invitations")
        invitationRef.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    for (invitationSnapshot in snapshot.children){
                        val invitationCode = invitationSnapshot.key
                        invitationCode?.let { code ->
                            checkCurrentUserInInvitationToDB(code,writer)
                        }
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    // db에 글 정보 추가하기-2 현재 접속한 사람의 초대코드 확인하기
    private fun checkCurrentUserInInvitationToDB(code: String, writer: String?) {
        val meetupsRef = database.child("meetups").child(code)
        meetupsRef.child("users").addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for (userSnapshot in snapshot.children){
                    val userId = userSnapshot.key
                    userId?.let { uid ->
                        if(uid == auth.currentUser?.uid){
                            addNoticeWriteToDB(code,writer)
                        }
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })

    }

    // db에 글 정보 추가하기-3 db에 경로 및 내용 추가하기
    private fun addNoticeWriteToDB(code: String, writer: String?) {
        val writerUID = auth.currentUser?.uid
        val content = binding.etContentNoti.text.toString()
        val date = SimpleDateFormat("yyyy.MM.dd", Locale.getDefault()).format(java.util.Date())

        val meetupsRef = database.child("meetups").child(code)
        meetupsRef.child("notice_write").addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val count = snapshot.childrenCount
                val nextNumber = String.format("%03d", count+1)
                val newPostKey = "notice-$nextNumber"

                val noticeWriteMap = mapOf(
                    "writerUID" to writerUID,
                    "writerName" to writer,
                    "content" to content,
                    "date" to date
                )

                val newPostRef = meetupsRef.child("notice_write").child(newPostKey)
                newPostRef.setValue(noticeWriteMap)

            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    // toolbar ------------------------
    // 툴바 메뉴 버튼을 설정
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.add_buyme_menu,menu)
        return true
    }

    // 툴바 메뉴 버튼이 클릭 됐을 때 콜백
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item!!.itemId){
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}