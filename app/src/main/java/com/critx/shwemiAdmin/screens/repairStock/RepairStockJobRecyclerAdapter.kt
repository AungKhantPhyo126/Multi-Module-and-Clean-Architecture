package com.critx.shwemiAdmin.screens.repairStock

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.critx.domain.model.repairStock.JobDomain
import com.critx.shwemiAdmin.databinding.ItemJobBinding

class RepairStockJobRecyclerAdapter : ListAdapter<JobDomain, CollectStockViewHolder>(
    CollectStockDiffUtil
)  {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CollectStockViewHolder {
        return CollectStockViewHolder(
            ItemJobBinding.inflate(
                LayoutInflater.from(parent.context),parent,false
            ))
    }

    override fun onBindViewHolder(holder: CollectStockViewHolder, position: Int) {
        holder.bind(getItem(position))
    }


}

class CollectStockViewHolder(private val binding: ItemJobBinding): RecyclerView.ViewHolder(binding.root){
    fun bind(data: JobDomain){
       binding.tvJob.text = data.repair_job_id.name
       binding.tvItem.text = data.jewellery_type_id.name
       binding.tvQty.text = data.quantity
       binding.tvWeight.text = data.weight_gm
    }
}

object CollectStockDiffUtil : DiffUtil.ItemCallback<JobDomain>() {
    override fun areItemsTheSame(oldItem: JobDomain, newItem: JobDomain): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: JobDomain, newItem: JobDomain): Boolean {
        return oldItem == newItem
    }

}