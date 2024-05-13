package com.example.stydyrussian

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.example.stydyrussian.GreetingsSigning.Validator
import com.example.stydyrussian.RoomData.MainDb
import com.example.stydyrussian.databinding.FragmentChangePasswordBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class Change_password : Fragment() {
    // TODO: Rename and change types of parameters
    private var login: String? = null
    private var param2: String? = null
    private lateinit var binding: FragmentChangePasswordBinding
    private lateinit var db: MainDb
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            login = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChangePasswordBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {

            db = MainDb.getDb(requireContext())
            val validator = Validator()

            button.setOnClickListener {
                val password = passEditText.text.toString()
                val passwordConfirm = passEditText1.text.toString()

                if (!validator.arePasswordsMatching(password, passwordConfirm)) {
                    passEditText1.error = "Пароли не совпадают"
                    return@setOnClickListener
                }
                if (!validator.isPasswordValid(password)) {
                    passEditText.error = "Пароль должен быть не менее 6 символов"
                    return@setOnClickListener
                }
                if (validator.containsInvalidCharacters(password)) {
                    passEditText.error = "Недопустимые символы в пароле"
                    return@setOnClickListener
                }

                viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
                    try {
                        db.getUsersDao().updatePassword(login!!, password)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

            }

            backButton.setOnClickListener {
                requireActivity().supportFragmentManager.popBackStack()
            }
        }
    }


    companion object {

        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String?, param2: String?) =
            Change_password().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}