package com.critx.shwemiAdmin.screens.sampleTakeAndReturn

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.critx.shwemiAdmin.screens.sampleTakeAndReturn.tabs.inventory.InventoryFragment
import com.critx.shwemiAdmin.screens.sampleTakeAndReturn.tabs.outside.OutSideFragment


const val INVENTORY="inventory"
const val OUTSIDE="outside"


class SampleTakeAndReturnPagerAdapter(fragment:Fragment):FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        val type = when(position){
            0-> INVENTORY
            1-> OUTSIDE
            else-> INVENTORY
        }
        val fragment = when(type){

            INVENTORY->{
                InventoryFragment()
            }
            OUTSIDE->{
                OutSideFragment()
            }
            else->InventoryFragment()
        }
        return  fragment
    }

}