package com.example.quikpay.ui.contacts

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.quikpay.data.models.Contact
import com.example.quikpay.databinding.ContactItemBinding

class ContactsViewAdapter() :
    ListAdapter<Contact, ContactsViewAdapter.ViewHolder>(
        ContactDiffCallback()
    ) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item!!)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(val binding: ContactItemBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        fun bind(item: Contact) {
            binding.contactName.text = item.name
            binding.contactCheckImage.visibility = View.GONE
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ContactItemBinding.inflate(layoutInflater, parent, false)
                val viewHolder = ViewHolder(binding)
                binding.root.setOnClickListener(viewHolder)
                return viewHolder
            }

            private val TAG = ViewHolder::class.java.simpleName
        }

        override fun onClick(v: View?) {
            Log.d(TAG, "Item clicked at position $position");
        }
    }

    class ContactDiffCallback : DiffUtil.ItemCallback<Contact>() {
        override fun areItemsTheSame(
            oldItem: Contact,
            newItem: Contact
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: Contact,
            newItem: Contact
        ): Boolean {
            return oldItem == newItem
        }

    }
}