package com.example.quikpay.ui.pool.pendingpools

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.quikpay.R
import com.example.quikpay.data.models.PoolRequest
import com.example.quikpay.databinding.RequestItemBinding
import com.example.quikpay.utils.Strings

class PendingPoolsViewAdapter(private var clickListener: ViewHolder.ClickListener) :
    ListAdapter<PoolRequest, PendingPoolsViewAdapter.ViewHolder>(
        PendingRequestDiffCallback()
    ) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item!!)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(
            parent,
            clickListener
        )
    }

    class ViewHolder private constructor(
        val binding: RequestItemBinding,
        listener: ClickListener?
    ) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        private val TAG = ViewHolder::class.java.simpleName
        private var listener: ClickListener?
        private lateinit var poolRequest: PoolRequest

        fun bind(item: PoolRequest) {
            binding.textDescription.text = Strings.get(R.string.request_desc, item.from_name)
            poolRequest = item
        }

        override fun onClick(v: View?) {
            Log.d(TAG, "Item clicked at position $layoutPosition")
            listener!!.onItemClicked(layoutPosition, poolRequest)
        }

        init {
            binding.root.setOnClickListener(this)
            this.listener = listener
        }

        companion object {
            fun from(
                parent: ViewGroup,
                clickListener: ClickListener?
            ): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = RequestItemBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(
                    binding,
                    clickListener
                )
            }

        }

        interface ClickListener {
            fun onItemClicked(position: Int, item: PoolRequest)
        }
    }

    class PendingRequestDiffCallback : DiffUtil.ItemCallback<PoolRequest>() {
        override fun areItemsTheSame(
            oldItem: PoolRequest,
            newItem: PoolRequest
        ): Boolean {
            return oldItem.from_acc == newItem.from_acc
        }

        override fun areContentsTheSame(
            oldItem: PoolRequest,
            newItem: PoolRequest
        ): Boolean {
            return oldItem == newItem
        }

    }
}