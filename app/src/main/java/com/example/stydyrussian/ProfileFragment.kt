package com.example.stydyrussian

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import com.example.stydyrussian.GreetingsSigning.Greetings
import com.example.stydyrussian.GreetingsSigning.SignIn
import com.example.stydyrussian.databinding.FragmentProfileBinding
import java.io.File

// TODO: Rename parameter arguments, choose names that match
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        binding = FragmentProfileBinding.inflate(inflater)

        val savedImageUri = Personal().getSavedImageUri(requireContext())
        savedImageUri?.let {
            binding.image.setImageURI(it)
        }

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            personalInfo.setOnClickListener { openFragmentWithBackStack(Personal.newInstance(null,null)) }
            changePass.setOnClickListener { openFragmentWithBackStack(Change_password.newInstance(null,null)) }
            progress.setOnClickListener { openFragmentWithBackStack(Progress.newInstance(null,null)) }
            privacy.setOnClickListener { openFragmentWithBackStack(Privacy.newInstance(null,null)) }

            logOut.setOnClickListener {
                logoutUser()
            }
            
        }
    }

    companion object {

        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String?, param2: String?) =
            ProfileFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }


    private fun logoutUser() {
        // Очищаем SharedPreferences, удаляя информацию о входе пользователя
        val sharedPreferences = requireContext().getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE)
        sharedPreferences.edit().remove("isLoggedIn").apply()
        openActivity(Greetings())
    }


}