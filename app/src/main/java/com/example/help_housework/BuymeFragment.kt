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
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class BuymeFragment : Fragment() {
    private var mBinding : FragmentBuymeBinding ?= null
    private val binding get() = mBinding!!

    private val buymeWriteList = ArrayList<BuymeWrite>()
    private val buymeAdapter = BuymeAdapter(buymeWriteList)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding = FragmentBuymeBinding.inflate(inflater, container, false)
        val view=binding.root

        // RecyclerView 설정
        binding.rvBuyme.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvBuyme.setHasFixedSize(true)

        // Adapter 설정
        binding.rvBuyme.adapter = buymeAdapter

        binding.btnFloating.setOnClickListener {
            val intent = Intent(requireContext(),AddBuymeActivity::class.java)
            startActivityForResult(intent, ADD_BUYME_REQUEST_CODE)
        }

        return view
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