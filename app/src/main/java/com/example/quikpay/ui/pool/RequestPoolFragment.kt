package com.example.quikpay.ui.pool

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.quikpay.ProgressListener
import com.example.quikpay.R
import com.example.quikpay.data.models.Contact
import com.example.quikpay.databinding.FragmentRequestPoolBinding
import com.example.quikpay.utils.startHomeActivity
import kotlinx.android.synthetic.main.fragment_request_pool.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

/**
 * A simple [Fragment] subclass.
 * Use the [RequestPoolFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RequestPoolFragment : Fragment(), KodeinAware, ProgressListener,
    SelectedContactsViewAdapter.ViewHolder.ClickListener {
    private val TAG = RequestPoolFragment::class.java.simpleName
    override val kodein by kodein()
    private val factory: PoolViewModelFactory by instance()
    private lateinit var poolViewModel: PoolViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var selectedContactAdapter: SelectedContactsViewAdapter
    private lateinit var binding: FragmentRequestPoolBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG, "fragment created")
        poolViewModel = ViewModelProvider(requireActivity(), factory).get(PoolViewModel::class.java)
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_request_pool, container, false)
        binding.poolViewModel = poolViewModel
        binding.lifecycleOwner = activity
        selectedContactAdapter = SelectedContactsViewAdapter(this)

        poolViewModel.startCreatePool.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                poolViewModel.setValues(
                    binding.textDescription.text.toString(),
                    binding.textTarget.text.toString()
                )
                poolViewModel.createPool()
            }
        })

        poolViewModel.navigateToContacts.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                Navigation.findNavController(binding.root)
                    .navigate(PoolFragmentDirections.actionNavPoolToContactsFragment())
                poolViewModel.onNavigateToContacts()
            }
        })

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        recyclerView = binding.list
        poolViewModel.progressListener = this
        with(recyclerView) {
            layoutManager = LinearLayoutManager(context)
            adapter = selectedContactAdapter
            (adapter as SelectedContactsViewAdapter).submitList(poolViewModel.selectedContacts.value)
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment RequestPoolFragment.
         */
        @JvmStatic
        fun newInstance() = RequestPoolFragment()
    }


    override fun onStarted() {
        progressbar.visibility = View.VISIBLE
    }

    override fun onSuccess() {
        progressbar.visibility = View.GONE
        requireActivity().startHomeActivity()
    }

    override fun onFailure(message: String) {
        progressbar.visibility = View.GONE
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
    }

    override fun onItemClicked(position: Int, item: Contact) {
        poolViewModel.removeSelectedContact(item)
        selectedContactAdapter.notifyItemRemoved(position)
    }
}