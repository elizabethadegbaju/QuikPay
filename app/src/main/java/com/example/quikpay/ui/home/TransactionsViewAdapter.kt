package com.example.quikpay.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.quikpay.R
import com.example.quikpay.data.models.Transaction
import com.example.quikpay.databinding.TransactionItemBinding
import com.example.quikpay.utils.Strings

class TransactionsViewAdapter(val email: String) :
    ListAdapter<Transaction, TransactionsViewAdapter.ViewHolder>(
        TransactionDiffCallback()
    ) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item!!, email)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(val binding: TransactionItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Transaction, email: String) {
            binding.transactionDate.text = item.date
            val amount = item.amount.toString().split('.')
            val amountWhole = String.format("%,d", amount[0].toInt())
            when (item.type) {
                "from" -> {
                    binding.transactionType.setImageResource(R.drawable.ic_call_made_24)
                    binding.transactionUser.text = item.other_user
                    binding.transactionAmount.text = Strings.get(
                        R.string.var_amount_whole, amountWhole
                    )
                }
                "to" -> {
                    binding.transactionType.setImageResource(R.drawable.ic_call_received_24)
                    binding.transactionUser.text = item.other_user
                    binding.transactionAmount.text = Strings.get(
                        R.string.var_amount_whole, amountWhole
                    )
//                    binding.transactionAmount.text = Strings.get(
//                        R.string.var_amount_whole,
//                        String.format("%,d", amount[0].toInt())
//                    )
                }
            }
            binding.transactionRef.text = Strings.get(R.string.var_reference_no, item.ref_number)
            binding.transactionAmountDecimal.text = amount[1]
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = TransactionItemBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }

    class TransactionDiffCallback : DiffUtil.ItemCallback<Transaction>() {
        override fun areItemsTheSame(
            oldItem: Transaction,
            newItem: Transaction
        ): Boolean {
            return oldItem.ref_number == newItem.ref_number
        }

        override fun areContentsTheSame(
            oldItem: Transaction,
            newItem: Transaction
        ): Boolean {
            return oldItem == newItem
        }

    }
}