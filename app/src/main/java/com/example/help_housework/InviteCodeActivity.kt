package com.example.help_housework

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.help_housework.databinding.ActivityInviteCodeBinding

class InviteCodeActivity : AppCompatActivity() {
    private var mBinding : ActivityInviteCodeBinding ?= null
    private val binding get() = mBinding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityInviteCodeBinding.inflate(layoutInflater)
        setContentView(binding.root)

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

        binding.btnStart.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
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