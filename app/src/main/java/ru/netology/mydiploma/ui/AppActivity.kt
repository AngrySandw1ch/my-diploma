package ru.netology.mydiploma.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.content.res.AppCompatResources
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import ru.netology.mydiploma.R
import ru.netology.mydiploma.auth.AppAuth
import ru.netology.mydiploma.databinding.ActivityAppBinding
import ru.netology.mydiploma.viewmodel.AuthViewModel


class AppActivity : AppCompatActivity() {
    lateinit var binding: ActivityAppBinding
    private val viewModel: AuthViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAppBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setBackgroundDrawable(AppCompatResources.getDrawable(this, R.color.blue))

        viewModel.data.observe(this) {
            invalidateOptionsMenu()
        }
        val navView = binding.bottomNav
        val navController = findNavController(R.id.nav_host_fragment)

        navView.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.page_feed -> {
                    navController.navigate(R.id.feedFragment)
                    true
                }
                R.id.page_users -> {
                    navController.navigate(R.id.usersFragment)
                    true
                }
                R.id.page_events -> {
                    navController.navigate(R.id.eventsFragment)
                    true
                }
                else -> false
            }
        }


    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        menu.let {
            it.setGroupVisible(R.id.authenticated, viewModel.authentificated)
            it.setGroupVisible(R.id.unauthenticated, !viewModel.authentificated)
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.signIn -> {
               findNavController(R.id.nav_host_fragment)
                    .navigate(R.id.signInFragment)
                true
            }
            R.id.signUp -> {
               findNavController(R.id.nav_host_fragment)
                    .navigate(R.id.signUpFragment)
                true
            }
            R.id.signOut -> {
                AppAuth.getInstance().removeAuth()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}