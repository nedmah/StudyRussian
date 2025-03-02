package com.example.stydyrussian

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Rect
import android.icu.util.Calendar
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.stydyrussian.RoomData.MainDb
import com.example.stydyrussian.databinding.FragmentPersonalBinding
import com.example.stydyrussian.databinding.FragmentProfileBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.internal.ViewUtils.showKeyboard
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class Personal : Fragment() {

    // TODO: Rename and change types of parameters
    private var login: String? = null
    private var param2: String? = null
    private lateinit var binding: FragmentPersonalBinding
    private lateinit var imagePickerLauncher: ActivityResultLauncher<Intent>
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>
    private var name: String? = null
    private var surname: String? = null
    private var email: String? = null
    private var date: String? = null
    private val personalViewModel : PersonalViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            login = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

        imagePickerLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val data: Intent? = result.data
                    val imageUri = data?.data
                    if (imageUri != null) {
                        saveImage(requireContext(), imageUri)
                    }
                    binding.image.setImageURI(imageUri)
                }
            }

        requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                pickImageFromGallery()
            } else {
                Toast.makeText(
                    requireContext(),
                    "Разрешение на доступ к хранилищу не предоставлено",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }


//        val file = File(requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES),"profile_image.jpg")
//        val imageUri = Uri.fromFile(file)
//        binding.image.setImageURI(imageUri)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPersonalBinding.inflate(inflater)

        binding.apply {

            loginTV.text = "@$login"


            try {
                personalViewModel.getUserInfo(login!!)


            } catch (e: Exception) {
                e.printStackTrace()
            }

            personalViewModel.userInfo.observe(viewLifecycleOwner){userInfo ->
                nameEditText.setText(userInfo?.name)
                surnameEditText.setText(userInfo?.surname)
                emailEditText.setText(userInfo?.email)
                dateEditText.setText(userInfo?.date)
            }

            val savedImageUri = getSavedImageUri(requireContext())
            savedImageUri?.let {
                image.setImageURI(it)
            }


        }
        return binding.root
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {


            image.setOnClickListener {
                checkStoragePermissionAndPickImage()
            }


            dateEditText.setOnTouchListener { _, event ->
                if (event.action == MotionEvent.ACTION_UP) {
                    // Рассчитываем координату x относительно правого края EditText
                    val drawableEndX = dateEditText.width - dateEditText.compoundPaddingEnd
                    // Проверяем, было ли нажатие на Drawable
                    if (event.rawX >= drawableEndX) {
                        showDatePickerDialog(dateEditText)

                        return@setOnTouchListener true
                    }
                }
                false
            }

            // Устанавливаем слушатель для форматирования вводимого текста
            dateEditText.addTextChangedListener(object : TextWatcher {
                private val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
                private var isFormatting = false

                override fun afterTextChanged(s: Editable?) {
                    if (!isFormatting) {
                        isFormatting = true
                        if (s != null && s.length == 10) {
                            // Если введено полное значение, удаляем фокус, чтобы предотвратить дальнейший ввод
                            dateEditText.clearFocus()
                        }
                        isFormatting = false
                    }
                }

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (!s.isNullOrBlank() && count == 1 && (start == 1 || start == 4)) {
                        dateEditText.append(".")
                    }
                }
            })


            confirmBtn.setOnClickListener {
                binding.apply {
                    val name = nameEditText.textOrNull()
                    val surname = surnameEditText.textOrNull()
                    val email = emailEditText.textOrNull()
                    val date = dateEditText.textOrNull()

                    personalViewModel.updateUser(login!!,name,surname,email,date)


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
            Personal().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    fun EditText.textOrNull(): String? = if (text.isBlank()) null else text.toString()

    fun showDatePickerDialog(view: View) {
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, year, monthOfYear, dayOfMonth ->
                // Здесь вы можете обработать выбранную дату
                val selectedDate = "$dayOfMonth.${monthOfYear + 1}.$year"
                binding.dateEditText.setText(selectedDate)
                (view as EditText).setText(selectedDate)
            },
            // Установите текущую дату как дату по умолчанию в DatePickerDialog
            Calendar.getInstance().get(Calendar.YEAR),
            Calendar.getInstance().get(Calendar.MONTH),
            Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        )


        datePickerDialog.show()
    }

    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        imagePickerLauncher.launch(intent)
    }


    private fun checkStoragePermissionAndPickImage() {
        when {
            ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED -> {
                pickImageFromGallery()
            }
            else -> {
                requestPermissionLauncher.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }
    }

    private fun saveImage(context: Context, imageUri: Uri) {
        val inputStream = context.contentResolver.openInputStream(imageUri)
        val filename = "profile_image.jpg"
        val fileOutputStream = context.openFileOutput(filename, Context.MODE_PRIVATE)
        inputStream?.use { input ->
            fileOutputStream.use { output ->
                input.copyTo(output)
            }
        }
    }

    fun getSavedImageUri(context: Context): Uri? {
        val filename = "profile_image.jpg"
        val file = File(context.filesDir, filename)
        return if (file.exists()) {
            Uri.fromFile(file)
        } else {
            null
        }
    }

}