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
import com.andre.gitlist.databinding.FollowingFragmentBinding
import com.andre.gitlist.ui.viewmodel.FollowingViewModel
import com.andre.gitlist.ui.viewmodel.ViewModelFactory

class FollowingFragment : Fragment() {
    private var _binding: FollowingFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: FollowingViewModel
    private lateinit var adapter: UserRecyclerViewAdapter
    private lateinit var progressBar: ProgressBar

    companion object {
        fun newInstance(data: String) = FollowingFragment().apply {
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
        _binding = FollowingFragmentBinding.inflate(inflater, container, false)

        val view = binding.root
        adapter = UserRecyclerViewAdapter(requireContext(), emptyList()) { user ->
            val intent = Intent(requireContext(), DetailActivity::class.java)
            intent.putExtra("user", user)
            startActivity(intent)
        }
        val recyclerView = binding.followingRecyclerView
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)
        progressBar = binding.followingProgressBar
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val factory = ViewModelFactory.getInstance(requireContext())
        viewModel = ViewModelProvider(this, factory)[FollowingViewModel::class.java]
        val bundle = arguments
        val userName = bundle?.getString("user")

        viewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }
        viewModel.isError.observe(viewLifecycleOwner) {
            showErrorMessage(requireContext(), viewModel.errorMessage.value.toString(), it)
        }

        viewModel.following.observe(viewLifecycleOwner) {
            adapter.updateData(it)
        }

        if (userName != null) {
            viewModel.getFollowing(userName)
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