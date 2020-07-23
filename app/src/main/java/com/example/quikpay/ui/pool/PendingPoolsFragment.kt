package com.example.quikpay.ui.pool

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.quikpay.ProgressListener
import com.example.quikpay.data.models.PoolRequest
import com.example.quikpay.databinding.FragmentPendingRequestsBinding
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

/**
 * A simple [Fragment] subclass.
 * Use the [PendingPoolsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PendingPoolsFragment : Fragment(), KodeinAware,
    PendingPoolsViewAdapter.ViewHolder.ClickListener,
    ProgressListener {
    private val TAG = PendingPoolsFragment::class.java.simpleName
    override val kodein by kodein()
    private val factory: PoolViewModelFactory by instance()
    private lateinit var poolViewModel: PoolViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var pendingPoolsAdapter: PendingPoolsViewAdapter
    private lateinit var binding: FragmentPendingRequestsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG, "fragment created")
        poolViewModel = ViewModelProvider(requireActivity(), factory).get(PoolViewModel::class.java)
        pendingPoolsAdapter = PendingPoolsViewAdapter(this)
        binding = FragmentPendingRequestsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        poolViewModel.progressListener = this
        recyclerView = binding.list
        poolViewModel.fetchPendingRequests()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment PendingRequestsFragment.
         */
        @JvmStatic
        fun newInstance() = PendingPoolsFragment()
    }

    override fun onItemClicked(position: Int, item: PoolRequest) {
        this.findNavController().navigateUp()
    }

    override fun onStarted() {
        binding.progressbar.visibility = View.VISIBLE
    }

    override fun onSuccess() {
        binding.progressbar.visibility = View.GONE
        Log.d(
            TAG,
            "Successfully fetched pending requests: ${poolViewModel.pendingRequests.value.toString()}"
        )
        val pendingRequests = poolViewModel.pendingRequests.value
        with(recyclerView) {
            adapter = pendingPoolsAdapter
            layoutManager = LinearLayoutManager(context)
            (adapter as PendingPoolsViewAdapter).submitList(pendingRequests)
        }
    }

    override fun onFailure(message: String) {
        binding.progressbar.visibility = View.GONE
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
    }
}