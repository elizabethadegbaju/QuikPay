package com.example.quikpay.ui.reportissue

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.quikpay.ProgressListener
import com.example.quikpay.R
import com.example.quikpay.databinding.ActivityReportIssueBinding
import com.example.quikpay.utils.startHomeActivity
import kotlinx.android.synthetic.main.activity_report_issue.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class ReportIssueActivity : AppCompatActivity(),
    ProgressListener, KodeinAware {
    override val kodein by kodein()
    private val factory: ReportIssueViewModelFactory by instance()
    private lateinit var viewModel: ReportIssueViewModel
    private lateinit var binding: ActivityReportIssueBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_report_issue)
        viewModel = ViewModelProvider(this, factory).get(ReportIssueViewModel::class.java)
        binding.reportIssueViewModel = viewModel
        binding.lifecycleOwner = this
        viewModel.progressListener = this

        viewModel.setValues.observe(this, Observer {
            if (it == true) {
                viewModel.reportIssue(binding.issue.text.toString())
                viewModel.onSubmit()
            }
        })

    }

    override fun onStarted() {
        progressbar.visibility = View.VISIBLE
    }

    override fun onSuccess() {
        progressbar.visibility = View.GONE
        this.startHomeActivity()
    }

    override fun onFailure(message: String) {
        progressbar.visibility = View.GONE
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
