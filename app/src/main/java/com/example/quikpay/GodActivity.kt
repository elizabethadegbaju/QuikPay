package com.example.quikpay

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.quikpay.databinding.ActivityGodBinding
import com.example.quikpay.databinding.NavHeaderGodBinding
import com.example.quikpay.ui.reportissue.ReportIssueActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_god.*
import kotlinx.android.synthetic.main.app_bar_god.view.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class GodActivity : AppCompatActivity(), ProgressListener, KodeinAware {
    override val kodein by kodein()
    private val factory: GodViewModelFactory by instance()
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var viewModel: GodViewModel
    private lateinit var binding: ActivityGodBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_god)
        viewModel = ViewModelProvider(this, factory).get(GodViewModel::class.java)
        viewModel.progressListener = this
        binding.godViewModel = viewModel
        binding.lifecycleOwner = this
        val header = binding.navView.getHeaderView(0)
        val headerBinding: NavHeaderGodBinding = NavHeaderGodBinding.bind(header)
        viewModel.fetchUserDetails()

        //Update navigation drawer header details when userDetails is updated
        viewModel.userDetails.observe(this, Observer {
            if (it != null) {
                Glide.with(this)
                    .load(viewModel.userDetails.value?.photoURL)
                    .apply(RequestOptions().placeholder(R.drawable.ic_round_person_24))
                    .into(headerBinding.profilePicture)
                headerBinding.profilePicture.clipToOutline = true
                headerBinding.displayName.text = viewModel.userDetails.value!!.name
                headerBinding.profilePicture.setBackgroundResource(R.drawable.round_outline)
            }
        })
        viewModel.navigateToReportIssue.observe(this, Observer {
            if (it == true) {
                startActivity(Intent(this, ReportIssueActivity::class.java))
                viewModel.onNavigateToReportIssue()
            }
        })

        val toolbar = binding.root.toolbar
        setSupportActionBar(toolbar)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment)

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_edit_profile
            ), drawerLayout
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
//        bottomNavView.setupWithNavController(navController)
//        bottomNavView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.god, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private val onNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            item.isChecked = true
            when (item.itemId) {
                R.id.nav_home -> {
                    TODO()
                }
                R.id.nav_pool -> {
                    TODO()
                }
                R.id.nav_send -> {
                    TODO()
                }
                R.id.nav_contacts -> {
                    TODO()
                }
            }
            false
        }

    override fun onStarted() {
        progressbar.visibility = View.VISIBLE
    }

    override fun onSuccess() {
        progressbar.visibility = View.GONE
    }

    override fun onFailure(message: String) {
        progressbar.visibility = View.GONE
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
