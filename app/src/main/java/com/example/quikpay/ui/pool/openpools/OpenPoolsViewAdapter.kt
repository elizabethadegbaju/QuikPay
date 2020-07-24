package com.example.quikpay.ui.pool.openpools

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.quikpay.R
import com.example.quikpay.data.models.Pool
import com.example.quikpay.databinding.PoolItemBinding
import com.example.quikpay.utils.Strings

class OpenPoolsViewAdapter(private var clickListener: ViewHolder.ClickListener) :
    ListAdapter<Pool, OpenPoolsViewAdapter.ViewHolder>(
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
        val binding: PoolItemBinding,
        listener: ClickListener?
    ) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        private val TAG = ViewHolder::class.java.simpleName
        private var listener: ClickListener?
        private lateinit var pool: Pool

        fun bind(item: Pool) {
            binding.textDescription.text = item.description
            binding.textDate.text =
                Strings.get(R.string.request_sent_date, item.created_at!!.toDate())
            pool = item
        }

        override fun onClick(v: View?) {
            Log.d(TAG, "Item clicked at position $layoutPosition")
            listener!!.onItemClicked(layoutPosition, pool)
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
                val binding = PoolItemBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(
                    binding,
                    clickListener
                )
            }

        }

        interface ClickListener {
            fun onItemClicked(position: Int, item: Pool)
        }
    }

    class PendingRequestDiffCallback : DiffUtil.ItemCallback<Pool>() {
        override fun areItemsTheSame(
            oldItem: Pool,
            newItem: Pool
        ): Boolean {
            return oldItem.created_at == newItem.created_at
        }

        override fun areContentsTheSame(
            oldItem: Pool,
            newItem: Pool
        ): Boolean {
            return oldItem == newItem
        }

    }
}