package com.example.quikpay.ui.sendmoney.quikpay

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
import com.example.quikpay.ProgressListener
import com.example.quikpay.R
import com.example.quikpay.databinding.FragmentSendQuikpayBinding
import com.example.quikpay.ui.sendmoney.SendMoneyViewModel
import com.example.quikpay.ui.sendmoney.SendMoneyViewModelFactory
import com.example.quikpay.utils.startHomeActivity
import kotlinx.android.synthetic.main.fragment_send_quikpay.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

/**
 * A simple [Fragment] subclass.
 * Use the [SendQuikPayFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SendQuikPayFragment : Fragment(), KodeinAware, ProgressListener {
    private val TAG = SendQuikPayFragment::class.java.simpleName
    override val kodein by kodein()
    private val factory: SendMoneyViewModelFactory by instance()
    private lateinit var sendMoneyViewModel: SendMoneyViewModel
    private lateinit var binding: FragmentSendQuikpayBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG, "fragment created")
        sendMoneyViewModel =
            ViewModelProvider(requireActivity(), factory).get(SendMoneyViewModel::class.java)
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_send_quikpay, container, false)
        binding.sendMoneyViewModel = sendMoneyViewModel
        binding.lifecycleOwner = activity

        sendMoneyViewModel.startSendMoneyQuikpay.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                val accNo = binding.textQuikpayAccNo.text.toString()
                val amount = binding.textQuikpayAmount.text.toString().toDouble()
                val description = binding.textQuikpayDescription.text.toString()
                sendMoneyViewModel.setValues(accNo, amount, description, "quikpay")
                sendMoneyViewModel.sendMoneyQuikpay()
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
         * @return A new instance of fragment SendQuikpayFragment.
         */
        @JvmStatic
        fun newInstance() =
            SendQuikPayFragment()
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