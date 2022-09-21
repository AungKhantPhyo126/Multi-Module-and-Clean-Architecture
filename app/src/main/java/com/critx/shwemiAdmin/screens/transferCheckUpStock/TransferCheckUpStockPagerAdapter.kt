package com.critx.shwemiAdmin.screens.transferCheckUpStock

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.critx.shwemiAdmin.screens.sampleTakeAndReturn.tabs.new.NewSampleFragment
import com.critx.shwemiAdmin.screens.sampleTakeAndReturn.tabs.outside.OutSideFragment
import com.critx.shwemiAdmin.screens.sampleTakeAndReturn.tabs.voucher.VoucherFragment

const val CHECKUP="checkUp"
const val TRANSFER="transfer"


class TransferCheckUpStockPagerAdapter(fragment: Fragment): FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        val type = when(position){
            0-> CHECKUP
            1-> TRANSFER
            else-> CHECKUP
        }
        val fragment = when(type){
            CHECKUP -> {
                VoucherFragment()
            }
            TRANSFER->{
                NewSampleFragment()
            }
            else-> VoucherFragment()
        }
        return  fragment
    }

}