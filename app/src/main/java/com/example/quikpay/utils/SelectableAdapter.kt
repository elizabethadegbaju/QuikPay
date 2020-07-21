package com.example.quikpay.utils

import android.util.SparseBooleanArray
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.quikpay.data.models.Contact


abstract class SelectableAdapter<VH : RecyclerView.ViewHolder?> :
    ListAdapter<Contact, VH>(ContactDiffCallback()) {
    private var selectedItems: SparseBooleanArray = SparseBooleanArray()
    var selectedContactList = mutableListOf<Contact>()

    /**
     * Indicates if the item at position position is selected
     * @param position Position of the item to check
     * @return true if the item is selected, false otherwise
     */
    fun isSelected(position: Int): Boolean {
        return getSelectedItems().contains(position)
    }

    /**
     * Toggle the selection status of the item at a given position
     * @param position Position of the item to toggle the selection status for
     */
    fun toggleSelection(position: Int, contact: Contact) {
        if (selectedItems[position, false]) {
            selectedItems.delete(position)
            selectedContactList.remove(contact)
        } else {
            selectedItems.put(position, true)
            selectedContactList.add(contact)
        }
        notifyItemChanged(position)
    }

    /**
     * Clear the selection status for all items
     */
    fun clearSelection() {
        val selection = getSelectedItems()
        selectedItems.clear()
        for (i in selection) {
            notifyItemChanged(i)
        }
    }

    /**
     * Count the selected items
     * @return Selected items count
     */
    val selectedItemCount: Int
        get() = selectedItems.size()

    /**
     * Indicates the list of selected items
     * @return List of selected items ids
     */
    fun getSelectedItems(): List<Int> {
        val items: MutableList<Int> = ArrayList(selectedItems.size())
        for (i in 0 until selectedItems.size()) {
            items.add(selectedItems.keyAt(i))
        }
        return items
    }

    companion object {
        private val TAG = SelectableAdapter::class.java.simpleName
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