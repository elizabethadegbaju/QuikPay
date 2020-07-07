package com.example.quikpay.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.quikpay.R
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

/**
 * A fragment representing a list of Items.
 */
class TransactionFragment : Fragment(), KodeinAware {

    override val kodein by kodein()
    private val factory: HomeViewModelFactory by instance()
    private lateinit var homeViewModel: HomeViewModel
    private var columnCount = 1
    private var transactionType = "all"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
            transactionType = it.getString(ARG_TRANSACTION_TYPE).toString()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_transaction_list, container, false)
        homeViewModel = ViewModelProvider(this, factory).get(HomeViewModel::class.java)
        homeViewModel.fetchUserDetails()

        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
                layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
                }
                adapter = TransactionsViewAdapter(homeViewModel.userDetails.value!!.email)

                when (transactionType) {
                    "all" -> {
                        homeViewModel.updateAllTransactions()
                    }
                    "received" -> {
                        homeViewModel.updateReceivedTransactions()
                    }
                    "sent" -> {
                        homeViewModel.updateSentTransactions()
                    }
                }
                homeViewModel.sentTransactions.observe(viewLifecycleOwner, Observer {
                    if (it.isNotEmpty()) {
                        if (transactionType == "sent")
                            (adapter as TransactionsViewAdapter).submitList(homeViewModel.sentTransactions.value)
                    }
                })
                homeViewModel.allTransactions.observe(viewLifecycleOwner, Observer {
                    if (it.isNotEmpty()) {
                        if (transactionType == "all")
                            (adapter as TransactionsViewAdapter).submitList(homeViewModel.allTransactions.value)
                    }
                })
                homeViewModel.receivedTransactions.observe(viewLifecycleOwner, Observer {
                    if (it.isNotEmpty()) {
                        if (transactionType == "received")
                            (adapter as TransactionsViewAdapter).submitList(homeViewModel.receivedTransactions.value)
                    }
                })
            }
        }

        return view
    }

    companion object {

        const val ARG_COLUMN_COUNT = "column-count"
        const val ARG_TRANSACTION_TYPE = "transaction-type"

        @JvmStatic
        fun newInstance(columnCount: Int, type: String) =
            TransactionFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                    putString(ARG_TRANSACTION_TYPE, type)
                }
            }
    }
}