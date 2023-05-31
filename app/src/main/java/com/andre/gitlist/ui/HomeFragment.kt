package com.andre.gitlist.ui

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.andre.gitlist.databinding.HomeFragmentBinding
import com.andre.gitlist.ui.viewmodel.HomeViewModel
import com.andre.gitlist.ui.viewmodel.ViewModelFactory

class HomeFragment : Fragment() {
    private var _binding: HomeFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: HomeViewModel
    private lateinit var progressBar: ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = HomeFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }


    @SuppressLint("SetTextI18n")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val factory = ViewModelFactory.getInstance(requireContext())
        viewModel = ViewModelProvider(this, factory)[HomeViewModel::class.java]
        progressBar = binding.homeProgressBar

        viewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }

        viewModel.activeUser.observe(viewLifecycleOwner) {
            binding.homeActiveUser.text = it.toString()
        }

        viewModel.activeOrg.observe(viewLifecycleOwner) {
            binding.homeActiveOrg.text = it.toString()
        }

        viewModel.topFollowers.observe(viewLifecycleOwner) {
            binding.homeTopFollowers.text =
                "1. ${it[0].login}\n2. ${it[1].login}\n3. ${it[2].login}\n4. ${it[3].login}\n 5. ${it[4].login}\n"
        }

        viewModel.isError.observe(viewLifecycleOwner) {
            viewModel.errorMessage.value?.let { it1 -> showErrorMessage(requireContext(), it1, it) }
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