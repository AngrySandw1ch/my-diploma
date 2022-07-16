package ru.netology.mydiploma.ui.user

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.netology.mydiploma.R
import ru.netology.mydiploma.databinding.FragmentUserDetailsBinding

class UserDetailsFragment : Fragment() {

    lateinit var binding: FragmentUserDetailsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserDetailsBinding.inflate(inflater, container, false)


        return binding.root
    }


}