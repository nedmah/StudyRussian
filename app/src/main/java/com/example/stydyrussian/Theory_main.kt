package com.example.stydyrussian

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.stydyrussian.databinding.FragmentTheoryMainBinding
import dagger.hilt.android.AndroidEntryPoint

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private const val ARG_PARAM3 = "param3"

@AndroidEntryPoint
class Theory_main : Fragment() {

    private lateinit var binding: FragmentTheoryMainBinding
    private var param1: String? = null
    private var param2: String? = null
    private var param3: String? = null
    var isCodeExecuted = false

    private val theoryViewModel : TheoryViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
            param3 = it.getString(ARG_PARAM3)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTheoryMainBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {

            themeText.text = arguments?.getString(ARG_PARAM1)?.replace("\n", " ")
            val fileName = arguments?.getString(ARG_PARAM2)
            val login = arguments?.getString(ARG_PARAM3)
            val resourceId = resources.getIdentifier(fileName, "raw", requireContext().packageName)
            val inputStream = resources.openRawResource(resourceId)
            val text = inputStream.bufferedReader().use { it.readText() }
            mainText.text = text
            val digit = fileName?.filter { it.isDigit() }

            theoryScroll.viewTreeObserver.addOnScrollChangedListener {
                if (!isCodeExecuted && theoryScroll.getChildAt(0).bottom <= (theoryScroll.height + theoryScroll.scrollY)) {

                    try {
                        theoryViewModel.theoryFullScroll(login!!,digit!!)
                    } catch (e: Exception) {
                        e.printStackTrace()
                        Log.d("IT'S OK DON'T PANIC", "не получился метод после скролла: ")
                    }

                    isCodeExecuted = true
                }
            }

            backButton.setOnClickListener {
                requireActivity().supportFragmentManager.popBackStack()
            }

        }
    }

    companion object {

        @JvmStatic
        fun newInstance(title: String, themeName: String, login: String) =
            Theory_main().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, title)
                    putString(ARG_PARAM2, themeName)
                    putString(ARG_PARAM3, login)
                }
            }

    }


}