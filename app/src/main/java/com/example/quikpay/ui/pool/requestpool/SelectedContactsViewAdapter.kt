package com.example.quikpay.ui.pool.requestpool

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.quikpay.data.models.Contact
import com.example.quikpay.databinding.ContactSelectedItemBinding
import com.example.quikpay.utils.SelectableAdapter

class SelectedContactsViewAdapter(private var clickListener: ViewHolder.ClickListener) :
    SelectableAdapter<SelectedContactsViewAdapter.ViewHolder>() {

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
        val binding: ContactSelectedItemBinding,
        listener: ClickListener?
    ) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        private val TAG = ViewHolder::class.java.simpleName
        private var listener: ClickListener?
        private lateinit var contact: Contact

        fun bind(item: Contact) {
            binding.contactName.text = item.name
            binding.contactAmount.text = "$200"
            contact = item
        }

        override fun onClick(v: View?) {
            Log.d(TAG, "Item clicked at position $layoutPosition")
            listener!!.onItemClicked(layoutPosition, contact)
        }

        init {
            binding.contactRemoveImage.setOnClickListener(this)
            this.listener = listener
        }

        companion object {
            fun from(
                parent: ViewGroup,
                clickListener: ClickListener?
            ): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ContactSelectedItemBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(
                    binding,
                    clickListener
                )
            }

        }

        interface ClickListener {
            fun onItemClicked(position: Int, item: Contact)
        }
    }
}