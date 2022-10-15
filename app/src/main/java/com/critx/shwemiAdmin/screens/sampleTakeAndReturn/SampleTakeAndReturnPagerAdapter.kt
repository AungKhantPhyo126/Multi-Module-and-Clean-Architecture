package com.critx.shwemiAdmin.screens.sampleTakeAndReturn

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.critx.shwemiAdmin.screens.sampleTakeAndReturn.tabs.inventory.InventoryFragment
import com.critx.shwemiAdmin.screens.sampleTakeAndReturn.tabs.outside.OutSideFragment
import com.critx.shwemiAdmin.screens.sampleTakeAndReturn.tabs.voucher.VoucherFragment


const val VOUCHER="voucher"
const val INVENTORY="inventory"
const val OUTSIDE="outside"


class SampleTakeAndReturnPagerAdapter(fragment:Fragment):FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        val type = when(position){
            0-> VOUCHER
            1-> INVENTORY
            2-> OUTSIDE
            else-> VOUCHER
        }
        val fragment = when(type){
            VOUCHER -> {
                VoucherFragment()
            }
            INVENTORY->{
                InventoryFragment()
            }
            OUTSIDE->{
                OutSideFragment()
            }
            else->VoucherFragment()
        }
        return  fragment
    }

}