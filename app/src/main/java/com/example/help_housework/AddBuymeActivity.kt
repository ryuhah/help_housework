package com.example.help_housework

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
                            // tv_to_user_a 현재 사용자의 이름과 관계
                            binding.tvToUserA.text = "${it.selectedRelation} ${it.name}"
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@AddBuymeActivity, "데이터베이스 읽기 오류 : ${error.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

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