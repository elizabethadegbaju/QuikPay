package com.example.quikpay.ui.sendmoney.bank

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.quikpay.ProgressListener
import com.example.quikpay.R
import com.example.quikpay.databinding.FragmentSendBankBinding
import com.example.quikpay.ui.sendmoney.SendMoneyViewModel
import com.example.quikpay.ui.sendmoney.SendMoneyViewModelFactory
import com.example.quikpay.utils.startHomeActivity
import kotlinx.android.synthetic.main.fragment_send_bank.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

/**
 * A simple [Fragment] subclass.
 * Use the [SendBankFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SendBankFragment : Fragment(), KodeinAware, ProgressListener {
    private val TAG = SendBankFragment::class.java.simpleName
    override val kodein by kodein()
    private val factory: SendMoneyViewModelFactory by instance()
    private lateinit var sendMoneyViewModel: SendMoneyViewModel
    private lateinit var binding: FragmentSendBankBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG, "fragment created")
        sendMoneyViewModel =
            ViewModelProvider(requireActivity(), factory).get(SendMoneyViewModel::class.java)
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_send_bank, container, false)
        binding.sendMoneyViewModel = sendMoneyViewModel
        binding.lifecycleOwner = activity
        val spinner = binding.bankSpinner
        ArrayAdapter.createFromResource(
            requireActivity(),
            R.array.banks,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }
        sendMoneyViewModel.startSendMoneyBank.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                val accNo = binding.textBankAccNo.text.toString()
                val amount = binding.textBankAmount.text.toString().toDouble()
                val description = binding.textBankDescription.text.toString()
                val bank = binding.bankSpinner.selectedItem.toString()
                sendMoneyViewModel.setValues(accNo, amount, description, "bank", bank)
                sendMoneyViewModel.sendMoneyBank()
            }
        })
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        sendMoneyViewModel.progressListener = this
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment SendBankFragment.
         */
        @JvmStatic
        fun newInstance() =
            SendBankFragment()
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
}