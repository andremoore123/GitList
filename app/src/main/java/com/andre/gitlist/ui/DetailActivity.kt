package com.andre.gitlist.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.andre.gitlist.R
import com.andre.gitlist.adapter.ViewPagerAdapter
import com.andre.gitlist.databinding.ActivityDetailBinding
import com.andre.gitlist.models.User
import com.andre.gitlist.ui.viewmodel.DetailViewModel
import com.andre.gitlist.ui.viewmodel.ViewModelFactory
import com.bumptech.glide.Glide
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayoutMediator

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private lateinit var viewModel: DetailViewModel
    private lateinit var progressBar: ProgressBar
    private lateinit var viewPagerAdapter: ViewPagerAdapter
    private lateinit var fabButton: FloatingActionButton


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        val factory = ViewModelFactory.getInstance(applicationContext)
        viewModel = ViewModelProvider(this, factory)[DetailViewModel::class.java]
        val userName = intent.getParcelableExtra<User>("user")
        if (userName != null) {
            viewModel.setUsername(userName.login)
            sendDataToFragment(userName.login)
        }

        progressBar = binding.detailProgressBar
        fabButton = binding.detailFAB

        viewModel.isLoading.observe(this){
            showLoading(it)
        }

        viewModel.isError.observe(this){
            showError(this, it, viewModel.errorMessage.value.toString())
        }

        viewModel.userName.observe(this){
            viewModel.getUserData()
        }

        viewModel.userData.observe(this){
            Glide.with(this).load(it.avatar).into(binding.detailImage)
            binding.detailTopAppBar.title = it.login
            binding.detailName.text = it.name
            binding.detailLocation.text =it.location
            binding.detailFollowers.text = it.followers.toString()
            binding.detailFollowings.text = it.following.toString()
            binding.detailGist.text = it.gist.toString()
            binding.detailRepo.text = it.repository.toString()
        }

        binding.detailTopAppBar.setNavigationOnClickListener {
            onBackPressed()
        }

        TabLayoutMediator(binding.detailTabLayout, binding.detailViewPager){tab, position ->
            when (position){
                0 -> tab.text = "Following"
                1 -> tab.text = "Followers"
            }
        }.attach()

        viewModel.isExist.observe(this){
            setFavoriteButton(it)
        }

    }
    private fun sendDataToFragment(userName: String){
        viewPagerAdapter = ViewPagerAdapter(this, userName)
        binding.detailViewPager.adapter = viewPagerAdapter
    }

    private fun showLoading(status: Boolean){
        progressBar.visibility = if (status) View.VISIBLE else View.INVISIBLE
    }

    private fun showError(context: Context, status: Boolean, errorMessage: String){
        if (status){
            Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
        }
    }

    private fun setFavoriteButton(status: Boolean){
        if (status){
            fabButton.setImageResource(R.drawable.ic_baseline_favorite_border_24)
        } else {
            fabButton.setImageResource(R.drawable.ic_baseline_favorite_24)
        }
        fabButton.setOnClickListener {
            viewModel.favButton()
        }
    }

}