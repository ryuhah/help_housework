package com.example.help_housework

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.help_housework.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private var mBinding : ActivityMainBinding ? = null

    private val binding get() = mBinding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        supportFragmentManager.beginTransaction().replace(
            R.id.fl_id,HomeFragment()
        ).commit()

        binding.bnvId.setOnItemSelectedListener {item->

            when(item.itemId){
                R.id.nav_home->{
                    supportFragmentManager.beginTransaction().replace(
                        R.id.fl_id,
                        HomeFragment()
                    ).commit()
                }
                R.id.nav_buyme->{
                    supportFragmentManager.beginTransaction().replace(
                        R.id.fl_id,
                        BuymeFragment()
                    ).commit()
                }
                R.id.nav_mypage->{
                    supportFragmentManager.beginTransaction().replace(
                        R.id.fl_id,
                        MypageFragment()
                    ).commit()
                }
            }
            true
        }


    }
}
