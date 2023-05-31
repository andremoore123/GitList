package com.andre.gitlist.ui

import android.app.UiModeManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.andre.gitlist.databinding.SettingFragmentBinding
import com.andre.gitlist.ui.viewmodel.ProfileViewModel
import com.andre.gitlist.utils.PreferenceManager
import kotlinx.coroutines.launch

class ProfileFragment : Fragment() {
    private lateinit var binding: SettingFragmentBinding
    private lateinit var viewModel: ProfileViewModel
    private lateinit var preferenceManager: PreferenceManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = SettingFragmentBinding.inflate(inflater, container, false)
        val view = binding.root
        preferenceManager = PreferenceManager.getInstance(requireContext())

        lifecycleScope.launch {
            preferenceManager.uiMode.collect { mode ->
                when (mode) {
                    UiModeManager.MODE_NIGHT_YES -> {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                        binding.settingSwitch.isChecked = true
                    }
                    UiModeManager.MODE_NIGHT_NO -> {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                        binding.settingSwitch.isChecked = false
                    }
                }
            }
        }

        binding.settingSwitch.setOnCheckedChangeListener { _, isChecked ->
            lifecycleScope.launch {
                preferenceManager.setUiMode(
                    if (isChecked) UiModeManager.MODE_NIGHT_YES else UiModeManager.MODE_NIGHT_NO
                )
            }
        }

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this)[ProfileViewModel::class.java]
        // TODO: Use the ViewModel
    }

}