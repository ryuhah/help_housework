package com.example.help_housework

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.example.help_housework.databinding.ActivityAddWorkBinding

class AddWorkActivity : AppCompatActivity() {

    private var mBinding : ActivityAddWorkBinding?= null
    private val binding get() = mBinding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityAddWorkBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar = binding.toolbar3
        setSupportActionBar(toolbar)
        supportActionBar?.title = "할 일 추가하기"
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