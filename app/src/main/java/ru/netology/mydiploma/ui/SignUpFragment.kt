package ru.netology.mydiploma.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import ru.netology.mydiploma.R
import ru.netology.mydiploma.auth.AppAuth
import ru.netology.mydiploma.databinding.FragmentSignUpBinding
import ru.netology.mydiploma.util.AndroidUtils
import ru.netology.mydiploma.viewmodel.SignUpViewModel

class SignUpFragment : Fragment() {
    lateinit var binding: FragmentSignUpBinding
    private val viewModel: SignUpViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignUpBinding.inflate(inflater, container, false)
        binding.userLogin.requestFocus()

        binding.SignUpButton.setOnClickListener {
            with(binding) {
                if (
                    userLogin.toString().isBlank() ||
                    userPassword.toString().isBlank()
                ) {
                    Toast.makeText(requireContext(), R.string.warning, Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                viewModel.signUp(userLogin.toString(), userPassword.toString(), requireContext())
            }

        }

        viewModel.data.observe(viewLifecycleOwner) {
            if (it.id != 0L && it.token != null) {
                AppAuth.getInstance().setAuth(it.id, it.token)
                //todo navigate to previous position
                AndroidUtils.hideKeyboard(requireView())
            }
        }

        return binding.root
    }
}