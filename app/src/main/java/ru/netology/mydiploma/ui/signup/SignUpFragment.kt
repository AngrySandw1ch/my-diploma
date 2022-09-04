package ru.netology.mydiploma.ui.signup

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import ru.netology.mydiploma.R
import ru.netology.mydiploma.auth.AppAuth
import ru.netology.mydiploma.databinding.FragmentSignUpBinding
import ru.netology.mydiploma.ui.AppBarController
import ru.netology.mydiploma.util.AndroidUtils
import ru.netology.mydiploma.util.hideAppBar
import ru.netology.mydiploma.util.showAppBar
import ru.netology.mydiploma.util.showToast
import ru.netology.mydiploma.viewmodel.SignUpViewModel
import javax.inject.Inject

@AndroidEntryPoint
class SignUpFragment : Fragment() {
    lateinit var binding: FragmentSignUpBinding
    @Inject
    lateinit var auth: AppAuth
    private val viewModel: SignUpViewModel by viewModels()

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
        binding = FragmentSignUpBinding.inflate(inflater, container, false)
        binding.userLogin.requestFocus()

        binding.SignUpButton.setOnClickListener {
            with(binding) {
                if (
                    userLogin.text.toString().isBlank() ||
                    userPassword.text.toString().isBlank()
                ) {
                    showToast(getString(R.string.warning))
                    return@setOnClickListener
                }
                viewModel.signUp(userLogin.text.toString(), userPassword.text.toString(), requireContext())
            }

        }

        viewModel.data.observe(viewLifecycleOwner) {
            if (it.id != 0L && it.token != null) {
                auth.setAuth(it.id, it.token)
                showAppBar()
                findNavController().navigate(R.id.action_signUpFragment_to_feedFragment)
                AndroidUtils.hideKeyboard(requireView())
            }
        }

        return binding.root
    }
}