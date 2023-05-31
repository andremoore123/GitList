package com.andre.gitlist.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.andre.gitlist.R
import com.andre.gitlist.adapter.UserRecyclerViewAdapter
import com.andre.gitlist.databinding.SearchFragmentBinding
import com.andre.gitlist.ui.viewmodel.SearchViewModel
import com.andre.gitlist.ui.viewmodel.ViewModelFactory

class SearchFragment : Fragment() {
    private var _binding: SearchFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: SearchViewModel
    private lateinit var progressBar: ProgressBar
    private lateinit var adapter: UserRecyclerViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = SearchFragmentBinding.inflate(inflater, container, false)

        val view = binding.root
        adapter = UserRecyclerViewAdapter(requireContext(), emptyList()) { user ->
            val intent = Intent(requireContext(), DetailActivity::class.java)
            intent.putExtra("user", user)
            startActivity(intent)
        }
        val recyclerView = view.findViewById<RecyclerView>(R.id.searchRecyclerView)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val factory = ViewModelFactory.getInstance(requireContext())
        viewModel = ViewModelProvider(this, factory)[SearchViewModel::class.java]
        progressBar = binding.searchProgresBar

        viewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }
        viewModel.userName.observe(viewLifecycleOwner) {
            viewModel.getSearchedUser()
        }
        viewModel.listSearchedUser.observe(viewLifecycleOwner, { users ->
            adapter.updateData(users)
        })

        viewModel.isError.observe(viewLifecycleOwner) {
            showError(requireContext(), it, viewModel.errorMessage.value.toString())
        }

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrBlank()) {
                    viewModel.setUserName(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (!newText.isNullOrBlank()) {
                    viewModel.setUserName(newText)
                }
                return true
            }
        })
    }

    private fun showLoading(status: Boolean) {
        Log.d("Loading", status.toString())
        progressBar.visibility = if (status) View.VISIBLE else View.INVISIBLE
    }

    private fun showError(context: Context, status: Boolean, errorMessage: String) {
        if (status) {
            Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}