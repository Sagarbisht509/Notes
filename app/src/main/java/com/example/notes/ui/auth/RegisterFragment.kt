package com.example.notes.ui.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.notes.R
import com.example.notes.databinding.FragmentRegisterBinding
import com.example.notes.models.UserRequest
import com.example.notes.utils.NetworkResult
import com.example.notes.utils.TokenManager
import com.example.notes.viewmodels.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private var _binding : FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private val authViewModel by activityViewModels<AuthViewModel>()

    @Inject
    lateinit var tokenManager: TokenManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentRegisterBinding.inflate(inflater, container, false)

        if(tokenManager.getToken() != null) {
            findNavController().navigate(R.id.action_registerFragment_to_notesFragment)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.registerBtn.setOnClickListener {
            val validationResult = validateInput()
            if(validationResult.first) {
                authViewModel.registerUser(getUserRequest())
            }
            else {
                binding.errorMessage.text = validationResult.second
            }
        }

        binding.login.setOnClickListener {
            it.findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }

        observer()
    }

    private fun observer() {
        authViewModel.userResponseLiveData.observe(viewLifecycleOwner, Observer {
            binding.progressBar.isVisible = false;
            when (it) {
                is NetworkResult.Loading -> {
                    binding.progressBar.isVisible = true;
                }
                is NetworkResult.Success -> {
                    tokenManager.saveToken(it.data!!.token)
                    findNavController().navigate(R.id.action_registerFragment_to_notesFragment)
                }
                is NetworkResult.Error -> {
                    binding.errorMessage.text = it.message
                }
            }
        })
    }

    private fun validateInput() : Pair<Boolean, String> {
        val userRequest = getUserRequest()
        return authViewModel.validateCredentials(userRequest.email, userRequest.password, userRequest.username, true)
    }

    private fun getUserRequest(): UserRequest {

        return binding.run {
            UserRequest(
                email.text.toString(),
                password.text.toString(),
                username.text.toString()
            )
        }

       /* val username = binding.username.text.toString()
        val email = binding.email.text.toString()
        val password = binding.password.text.toString()
        return UserRequest(email, password, username)*/
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}