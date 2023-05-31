package com.andre.gitlist

import android.app.UiModeManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import com.andre.gitlist.databinding.ActivityDetailBinding
import com.andre.gitlist.databinding.MainActivityBinding
import com.andre.gitlist.utils.PreferenceManager
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {
    private lateinit var binding: MainActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainActivityBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setBottomNav()


    }

    private fun setBottomNav() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.mainFragmentContainer) as NavHostFragment
        val navHostMainController = navHostFragment.navController
        binding.bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.homeFragment -> {
                    navHostMainController.navigate(R.id.homeFragment)
                    true
                }
                R.id.searchFragment -> {
                    navHostMainController.navigate(R.id.searchFragment)
                    true
                }
                R.id.favoriteFragment -> {
                    navHostMainController.navigate(R.id.favoriteFragment)
                    true
                }
                R.id.settingFragment -> {
                    navHostMainController.navigate(R.id.settingFragment)
                    true
                }
                else -> {
                    false
                }
            }
        }
    }

}