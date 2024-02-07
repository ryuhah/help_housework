package com.example.help_housework

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.help_housework.databinding.FragmentBuymeBinding


class BuymeFragment : Fragment() {
    private var mBinding : FragmentBuymeBinding ?= null
    private val binding get() = mBinding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding = FragmentBuymeBinding.inflate(inflater, container, false)
        val view=binding.root

        val buymeWriteList = arrayListOf(
            BuymeWrite("엄마 은영", "딸 하경", "아이스아메리카노 부탁해~", "링크.com", "구매중", "2024.02.07"),
            BuymeWrite("아빠 양민", "딸 하경", "카라멜마끼아또 부탁해~", "링크.com", "구매완료", "2024.02.06"),
            BuymeWrite("엄마 은영", "딸 하경", "아이스아메리카노 부탁해~", "링크.com", "구매완료", "2024.02.05"),
            BuymeWrite("엄마 은영", "딸 하경", "아이스아메리카노 부탁해~", "링크.com",  "구매완료", "2024.02.04")

        )

        binding.rvBuyme.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvBuyme.setHasFixedSize(true)

        binding.rvBuyme.adapter = BuymeAdapter(buymeWriteList)

        binding.cbStatus.setOnCheckedChangeListener {buttonView, isChecked ->
            if(isChecked){
                (binding.rvBuyme.adapter as BuymeAdapter).filterByStatus("구매중")
            } else {
                (binding.rvBuyme.adapter as BuymeAdapter).filterByStatus("")
            }
        }


        return view
    }



}