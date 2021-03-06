package ru.netology.mydiploma.ui.signup

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.mydiploma.R
import ru.netology.mydiploma.auth.AppAuth
import ru.netology.mydiploma.databinding.FragmentSignUpBinding
import ru.netology.mydiploma.ui.AppActivity
import ru.netology.mydiploma.util.AndroidUtils
import ru.netology.mydiploma.util.showToast
import ru.netology.mydiploma.viewmodel.SignUpViewModel

class SignUpFragment : Fragment() {
    lateinit var binding: FragmentSignUpBinding
    private val viewModel: SignUpViewModel by viewModels()
    private var appActivity: AppActivity? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        appActivity = context as? AppActivity
        appActivity?.supportActionBar?.hide()
    }

    override fun onDetach() {
        super.onDetach()
        appActivity?.supportActionBar?.show()
        appActivity = null
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
                AppAuth.getInstance().setAuth(it.id, it.token)
                appActivity?.supportActionBar?.show()
                findNavController().navigate(R.id.action_signUpFragment_to_feedFragment)
                AndroidUtils.hideKeyboard(requireView())
            }
        }

        return binding.root
    }
}