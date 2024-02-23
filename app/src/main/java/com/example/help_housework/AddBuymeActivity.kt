package com.example.help_housework

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.help_housework.databinding.ActivityAddBuymeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.sql.Date
import java.text.SimpleDateFormat
import java.util.Locale

class AddBuymeActivity : AppCompatActivity() {
    private var mBinding : ActivityAddBuymeBinding ?= null
    private val binding get() = mBinding!!
    private lateinit var database : DatabaseReference
    private lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityAddBuymeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar = binding.toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.title = "사주세요 글쓰기"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        database = FirebaseDatabase.getInstance().reference
        auth = FirebaseAuth.getInstance()

        // 현재 사용자의 UID 가져오기
        val currentUserUid = auth.currentUser?.uid

        currentUserUid?.let {uid ->
            database.child("users").child(uid).addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists()){
                        val user = snapshot.getValue(UserAccount::class.java)
                        user?.let {
                            // tv_from_user_a 현재 사용자의 이름과 관계
                            binding.tvFromUserA.text = "${it.selectedRelation} ${it.name}"
                        }
                        getInvitationCode()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@AddBuymeActivity, "데이터베이스 읽기 오류 : ${error.message}", Toast.LENGTH_SHORT).show()
                }
            })

        }

        binding.btnAddbuymeA.setOnClickListener {
            val fromUser = binding.tvFromUserA.text.toString()
            val toUser = binding.spToUserA.selectedItem.toString()
            val content = binding.etContentA.text.toString()
            val hyperlink = binding.etHyperlinkA.text.toString()
            val date = SimpleDateFormat("yyyy.MM.dd", Locale.getDefault()).format(java.util.Date())

            val intent = Intent().apply {
                putExtra("fromUser", fromUser)
                putExtra("toUser", toUser)
                putExtra("content", content)
                putExtra("hyperlink", hyperlink)
                putExtra("status", "구매중")
                putExtra("date", date)
            }

            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }

    // db에서 초대 코드 가져오기
    private fun getInvitationCode() {
        val invitationRef = database.child("invitations")

        invitationRef.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    for (invitationSnapshot in snapshot.children){
                        val invitationCode = invitationSnapshot.key
                        invitationCode?.let { code ->
                            checkCurrentUserInInvitation(code)
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@AddBuymeActivity, "데이터베이스 읽기 오류 : ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    // 초대코드 확인하기
    private fun checkCurrentUserInInvitation(code: String) {
        val meetupsRef = database.child("meetups").child(code)
        meetupsRef.child("users").addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for (userSnapshot in snapshot.children){
                    val userUid = userSnapshot.key
                    userUid?.let {uid ->
                        if(uid == auth.currentUser?.uid){
                            addUserListToSpinner(snapshot)
                            return
                        }
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@AddBuymeActivity, "데이터베이스 읽기 오류 : ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    // 스피너에 목록 추가하기
    private fun addUserListToSpinner(snapshot: DataSnapshot) {
        val usersList = mutableListOf<String>()
        for (userSnapshot in snapshot.children){
            val userUid = userSnapshot.key
            userUid?.let { uid ->
                database.child("users").child(uid).addListenerForSingleValueEvent(object : ValueEventListener{
                    override fun onDataChange(userSnapshot: DataSnapshot) {
                        val user = userSnapshot.getValue(UserAccount::class.java)
                        user?.let {
                            val relationName = "${it.selectedRelation} ${it.name}"
                            usersList.add(relationName)
                        }
                        val adapter = ArrayAdapter<String>(this@AddBuymeActivity, android.R.layout.simple_spinner_item, usersList)
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        binding.spToUserA.adapter = adapter
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(this@AddBuymeActivity, "데이터베이스 읽기 오류 : ${error.message}", Toast.LENGTH_SHORT).show()
                    }
                })
            }
        }
    }


    // tool bar --------------------------------
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