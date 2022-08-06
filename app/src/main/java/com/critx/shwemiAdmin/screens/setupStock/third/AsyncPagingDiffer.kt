package com.critx.shwemiAdmin.screens.setupStock.third

import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListUpdateCallback
import kotlinx.coroutines.Dispatchers

class AsyncPagingDiffer {
    val differ: AsyncPagingDataDiffer<String> by lazy {
        AsyncPagingDataDiffer(
            diffCallback = diffCallback,
            updateCallback = updateCallback,
            mainDispatcher = Dispatchers.Unconfined,
            workerDispatcher = Dispatchers.Unconfined,
        )
    }

    val diffCallback = object : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String) = oldItem.first() == newItem.first()
        override fun areContentsTheSame(oldItem: String, newItem: String) = oldItem == newItem
    }

    val updateCallback = object : ListUpdateCallback {
        override fun onInserted(position: Int, count: Int) {
            println("onInserted: ${differ.snapshot()}")
        }

        override fun onRemoved(position: Int, count: Int) {
            println("onRemoved: ${differ.snapshot()}")
        }

        override fun onMoved(fromPosition: Int, toPosition: Int) {
            println("onMoved: ${differ.snapshot()}")
        }

        override fun onChanged(position: Int, count: Int, payload: Any?) {
            println("onChanged: ${differ.snapshot()}")
        }
    }


}