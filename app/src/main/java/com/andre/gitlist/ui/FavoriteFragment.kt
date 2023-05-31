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
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.andre.gitlist.adapter.UserRecyclerViewAdapter
import com.andre.gitlist.databinding.FavoriteFragmentBinding
import com.andre.gitlist.ui.viewmodel.FavoriteViewModel
import com.andre.gitlist.ui.viewmodel.ViewModelFactory
import kotlinx.coroutines.launch

class FavoriteFragment : Fragment() {
    private var _binding: FavoriteFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: FavoriteViewModel
    private lateinit var adapter: UserRecyclerViewAdapter
    private lateinit var progressBar: ProgressBar
    private lateinit var recyclerView: RecyclerView


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FavoriteFragmentBinding.inflate(inflater, container, false)

        val view = binding.root
        adapter = UserRecyclerViewAdapter(requireContext(), emptyList()) { user ->
            val intent = Intent(requireContext(), DetailActivity::class.java)
            intent.putExtra("user", user)
            startActivity(intent)
        }
        recyclerView = binding.favoriteRecyclerView
        progressBar = binding.favoriteProgressBar

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)


        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val factory = ViewModelFactory.getInstance(requireContext())
        viewModel = ViewModelProvider(this, factory)[FavoriteViewModel::class.java]

        lifecycleScope.launch {
            viewModel.getFavorite()
        }

        viewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }
        viewModel.isError.observe(viewLifecycleOwner) {
            showErrorMessage(requireContext(), viewModel.errorMessage.value.toString(), it)
        }

        viewModel.favorite.observe(viewLifecycleOwner) {
            adapter.updateData(it)
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

    override fun onResume() {
        super.onResume()
        lifecycleScope.launch {
            viewModel.getFavorite()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}