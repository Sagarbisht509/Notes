package com.example.notes

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.notes.databinding.FragmentLoginBinding
import com.example.notes.models.UserRequest
import com.example.notes.utils.NetworkResult
import com.example.notes.utils.TokenManager
import com.example.notes.viewmodels.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding : FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val authViewModel by activityViewModels<AuthViewModel>()

    @Inject
    lateinit var tokenManager: TokenManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.loginBtn.setOnClickListener{
            val validationResult = validateInput()
            if(validationResult.first) {
                authViewModel.loginUser(getUserRequest())
            }
            else {
                binding.errorMessage.text = validationResult.second
            }
        }

        binding.register.setOnClickListener {
            findNavController().popBackStack()
        }

        observer()
    }

    private fun observer() {
        authViewModel.userResponseLiveData.observe(viewLifecycleOwner, Observer {
            // hide progress bar
            when (it) {
                is NetworkResult.Loading -> {
                    // show progress bar
                }
                is NetworkResult.Success -> {
                    tokenManager.saveToken(it.data!!.token)
                    findNavController().navigate(R.id.action_loginFragment_to_notesFragment)
                }
                is NetworkResult.Error -> {
                    binding.errorMessage.text = it.message
                }
                else -> {}
            }
        })
    }

    private fun validateInput() : Pair<Boolean, String> {
        val userRequest = getUserRequest()
        return authViewModel.validateCredentials(userRequest.email, userRequest.password, "", false)
    }

    private fun getUserRequest() : UserRequest {
        val email = binding.email.text.toString()
        val password = binding.password.text.toString()
        return UserRequest(email, password, "")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}