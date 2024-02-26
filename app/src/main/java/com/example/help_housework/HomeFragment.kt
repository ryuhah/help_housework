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

class HomeFragment : Fragment() {

    private var mBinding : FragmentHomeBinding ?= null

    private val binding get() = mBinding!!

    private val notiWriteList = ArrayList<HomeWrite>()
    private val notiAdapter = HomeAdapter(notiWriteList)


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root

//        val homeWriteList = arrayListOf(
//            HomeWrite("김원영","오늘 대청소날입니다","2024.02.07"),
//            HomeWrite("김빵빵","이번주 주말에 할머니 생신입니다","2024.02.07"),
//            HomeWrite("김다빵","이번달에 생일이신 엄마 생일축하드립니다","2024.02.06"),
//            HomeWrite("김스빵","이번년도 여행은 언제 가나요? 안가나요?","2024.02.06"),
//            HomeWrite("빵빵이","옥지얌~","2024.02.05"),
//            HomeWrite("옥지얌","빵빵아!","2024.02.05")
//
//        )

        binding.rvHome.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
        binding.rvHome.setHasFixedSize(true)

        binding.rvHome.adapter = notiAdapter



        val workWriteList = arrayListOf(
            WorkWrite("거실청소",R.drawable.tv_circle),
            WorkWrite("방청소",R.drawable.tv_circle),
            WorkWrite("설거지",R.drawable.tv_circle),
            WorkWrite("화장실청소",R.drawable.tv_circle)

        )
        binding.rvWork.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL,false)
        binding.rvWork.setHasFixedSize(true)

        binding.rvWork.adapter = WorkAdapter(workWriteList)


        binding.btnNotiH.setOnClickListener {
            val intent = Intent(requireContext(),AddNoticeActivity::class.java)
            startActivityForResult(intent, ADD_HOME_REQUEST_CODE)
        }

        binding.tvAddwork.setOnClickListener {
            val intent = Intent(requireContext(),AddWorkActivity::class.java)
            startActivity(intent)
        }


        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ADD_HOME_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val writer = data?.getStringExtra("writer")
            val content = data?.getStringExtra("contenth")
            val date = data?.getStringExtra("dateh")

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