package com.critx.shwemiAdmin.screens.transferCheckUpStock

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.critx.shwemiAdmin.screens.transferCheckUpStock.checkup.CheckUpStockFragment
import com.critx.shwemiAdmin.screens.transferCheckUpStock.transfer.TransferStockFragment

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
                CheckUpStockFragment()
            }
            TRANSFER->{
                TransferStockFragment()
            }
            else-> CheckUpStockFragment()
        }
        return  fragment
    }

}