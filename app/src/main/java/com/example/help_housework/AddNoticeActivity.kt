package com.example.help_housework

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.example.help_housework.databinding.ActivityAddNoticeBinding

class AddNoticeActivity : AppCompatActivity() {

    private var mBinding : ActivityAddNoticeBinding?= null
    private val binding get() = mBinding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityAddNoticeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar = binding.toolbar2
        setSupportActionBar(toolbar)
        supportActionBar?.title = "공지사항 글쓰기"    // 이부분 공지사항 글쓰기로 수정해주세용
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

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