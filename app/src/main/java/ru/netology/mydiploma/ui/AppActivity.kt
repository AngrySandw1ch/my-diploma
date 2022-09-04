package ru.netology.mydiploma.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.content.res.AppCompatResources
import androidx.navigation.findNavController
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint
import ru.netology.mydiploma.R
import ru.netology.mydiploma.auth.AppAuth
import ru.netology.mydiploma.databinding.ActivityAppBinding
import ru.netology.mydiploma.service.FCMService
import ru.netology.mydiploma.viewmodel.AuthViewModel
import javax.inject.Inject

@AndroidEntryPoint
class AppActivity : AppCompatActivity(), AppBarController{
    @Inject
    lateinit var auth: AppAuth
    @Inject
    lateinit var firebaseMessaging: FirebaseMessaging
    @Inject
    lateinit var googleApiAvailability: GoogleApiAvailability

    lateinit var binding: ActivityAppBinding
    private val viewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAppBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseMessaging.token.addOnSuccessListener {
            println(it)
        }

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

        checkGoogleApiAvailability()
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
                auth.removeAuth()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun checkGoogleApiAvailability() {
        with(googleApiAvailability) {
            val code = isGooglePlayServicesAvailable(this@AppActivity)
            if (code == ConnectionResult.SUCCESS) {
                return@with
            }
            if (isUserResolvableError(code)) {
                getErrorDialog(this@AppActivity, code, 9000)?.show()
                return
            }
            Toast.makeText(this@AppActivity, "google play unavailable", Toast.LENGTH_LONG)
                .show()
        }
    }

    override fun hide() {
        supportActionBar?.hide()
    }

    override fun show() {
        supportActionBar?.show()
    }

}