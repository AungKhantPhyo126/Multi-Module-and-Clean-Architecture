package com.critx.shwemiAdmin.screens.transferCheckUpStock.transfer.matchCode

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.critx.shwemiAdmin.R
import com.critx.shwemiAdmin.databinding.ItemMatchCodeBinding
import com.journeyapps.barcodescanner.ScanOptions


class MatchCodeRecyclerAdapter(
    private val viewModel: MatchCodeViewModel,     private val onclick:(position:Int)->Unit

) :
    ListAdapter<String, StockCodeListViewHolder>(
        StockCodeListDiffUtil
    ) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StockCodeListViewHolder {
        return StockCodeListViewHolder(
            ItemMatchCodeBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            ), viewModel, onclick
        )
    }

    override fun onBindViewHolder(holder: StockCodeListViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class StockCodeListViewHolder(
    private val binding: ItemMatchCodeBinding,
    private val viewModel: MatchCodeViewModel,
    private val onclick:(position:Int)->Unit
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(data: String) {

        binding.tvStockCode.text = data
        binding.tilRfidCode.setEndIconOnClickListener {
            onclick(bindingAdapterPosition)
//             scanQrCodeRfid(binding.root.context,barlauncher)
//            viewModel.rfidCodeList[bindingAdapterPosition] = barlauncher.second
        }
        binding.edtRfid.setText(viewModel.rfidCodeList[bindingAdapterPosition])
        binding.edtRfid.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                // TODO Auto-generated method stub
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // TODO Auto-generated method stub
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                viewModel.rfidCodeList[bindingAdapterPosition] = s.toString()
            }
        })
    }
}


object StockCodeListDiffUtil : DiffUtil.ItemCallback<String>() {
    override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }

}