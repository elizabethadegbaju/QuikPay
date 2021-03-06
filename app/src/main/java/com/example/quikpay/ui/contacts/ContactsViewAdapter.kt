package com.example.quikpay.ui.contacts

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.quikpay.data.models.Contact
import com.example.quikpay.databinding.ContactItemBinding
import com.example.quikpay.utils.SelectableAdapter

class ContactsViewAdapter(private var clickListener: ViewHolder.ClickListener) :
    SelectableAdapter<ContactsViewAdapter.ViewHolder>() {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        val checkVisibility = if (isSelected(position)) View.VISIBLE else View.INVISIBLE
        holder.bind(item!!, checkVisibility)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent, clickListener)
    }

    class ViewHolder private constructor(
        val binding: ContactItemBinding,
        listener: ClickListener?
    ) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        private val TAG = ViewHolder::class.java.simpleName
        private var listener: ClickListener?
        private lateinit var contact: Contact

        fun bind(item: Contact, checkVisibility: Int) {
            contact = item
            binding.contactName.text = item.name
            binding.contactCheckImage.visibility = checkVisibility
        }

        override fun onClick(v: View?) {
            Log.d(TAG, "Item clicked at position $layoutPosition")
            listener!!.onItemClicked(layoutPosition, contact)
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
                val binding = ContactItemBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding, clickListener)
            }

        }

        interface ClickListener {
            fun onItemClicked(
                position: Int,
                contact: Contact
            ): Boolean
        }
    }
}