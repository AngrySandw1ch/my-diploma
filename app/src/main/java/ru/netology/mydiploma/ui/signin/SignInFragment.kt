package ru.netology.mydiploma.ui.signin

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_sign_in.*
import ru.netology.mydiploma.R
import ru.netology.mydiploma.auth.AppAuth
import ru.netology.mydiploma.databinding.FragmentSignInBinding
import ru.netology.mydiploma.ui.AppActivity
import ru.netology.mydiploma.ui.AppBarController
import ru.netology.mydiploma.util.AndroidUtils
import ru.netology.mydiploma.util.hideAppBar
import ru.netology.mydiploma.util.showAppBar
import ru.netology.mydiploma.util.showToast
import ru.netology.mydiploma.viewmodel.SignInViewModel
import javax.inject.Inject

@AndroidEntryPoint
class SignInFragment : Fragment() {
    lateinit var binding: FragmentSignInBinding
    @Inject
    lateinit var auth: AppAuth
    private val viewModel: SignInViewModel by viewModels()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        hideAppBar()
    }

    override fun onDetach() {
        super.onDetach()
        showAppBar()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignInBinding.inflate(inflater, container, false)

        binding.newUserName.requestFocus()

        binding.signInButton.setOnClickListener {
            with(binding) {
                if (
                    newUserLogin.text.toString().isBlank() ||
                    newUserPassword.text.toString().isBlank() ||
                    newUserName.text.toString().isBlank()
                ) {
                    showToast(getString(R.string.warning))
                    return@setOnClickListener
                }
                viewModel.signIn(
                    newUserLogin.text.toString(),
                    newUserPassword.text.toString(),
                    newUserName.text.toString()
                )
            }
        }

        viewModel.data.observe(viewLifecycleOwner) {
            if (it.id != 0L && it.token != null) {
                auth.setAuth(it.id, it.token)
                showAppBar()
                findNavController().navigate(R.id.action_signInFragment_to_feedFragment)
                AndroidUtils.hideKeyboard(requireView())
            }
        }

        return binding.root
    }

}