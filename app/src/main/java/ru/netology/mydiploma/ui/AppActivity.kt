package ru.netology.mydiploma.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.content.res.AppCompatResources
import ru.netology.mydiploma.R
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
            R.id.signIn -> {true}
            R.id.signUp -> {true}
            R.id.signOut -> {true}
            else -> super.onOptionsItemSelected(item)
        }
    }

}