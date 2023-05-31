package com.andre.gitlist.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.andre.gitlist.adapter.UserRecyclerViewAdapter
import com.andre.gitlist.databinding.FollowersFragmentBinding
import com.andre.gitlist.ui.viewmodel.FollowersViewModel
import com.andre.gitlist.ui.viewmodel.ViewModelFactory

class FollowersFragment : Fragment() {
    private var _binding: FollowersFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: FollowersViewModel
    private lateinit var adapter: UserRecyclerViewAdapter
    private lateinit var progressBar: ProgressBar

    companion object {
        fun newInstance(data: String) = FollowersFragment().apply {
            arguments = Bundle().apply {
                putString("user", data)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FollowersFragmentBinding.inflate(inflater, container, false)

        val view = binding.root
        adapter = UserRecyclerViewAdapter(requireContext(), emptyList()) { user ->
            val intent = Intent(requireContext(), DetailActivity::class.java)
            intent.putExtra("user", user)
            startActivity(intent)
        }
        val recyclerView = binding.followersRecyclerView
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)
        progressBar = binding.followersProgressBar
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val factory = ViewModelFactory.getInstance(requireContext())
        viewModel = ViewModelProvider(this, factory)[FollowersViewModel::class.java]
        val bundle = arguments
        val userName = bundle?.getString("user")

        viewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }
        viewModel.isError.observe(viewLifecycleOwner) {
            showErrorMessage(requireContext(), viewModel.errorMessage.value.toString(), it)
        }

        viewModel.followers.observe(viewLifecycleOwner) {
            adapter.updateData(it)
        }

        if (userName != null) {
            viewModel.getFollowers(userName)
        }
    }

    private fun showLoading(status: Boolean) {
        progressBar.visibility = if (status) View.VISIBLE else View.INVISIBLE
    }

    private fun showErrorMessage(context: Context, errorMessage: String, status: Boolean) {
        if (status) {
            Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}