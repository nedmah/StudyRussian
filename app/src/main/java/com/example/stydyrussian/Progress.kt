package com.example.stydyrussian

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.stydyrussian.RoomData.MainDb
import com.example.stydyrussian.databinding.FragmentProgressBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

@AndroidEntryPoint
class Progress : Fragment() {


    private var login: String? = null
    private var param2: String? = null
    private lateinit var binding: FragmentProgressBinding
    private val progressViewModel: ProgressViewModel by activityViewModels()
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
        binding = FragmentProgressBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.apply {


            progressViewModel.getProgress(login!!)

            progressViewModel.completedThemesList.observe(viewLifecycleOwner) {
                it.forEach { themeNumber ->
                    completeTheory(themeNumber)
                }
            }

            progressViewModel.completedTestsList.observe(viewLifecycleOwner) {
                it.forEach { testScore ->
                    completeTest(testScore.themeNumber, testScore.testProgress.toString())
                }
            }



            button.setOnClickListener {

                CustomDialog(
                    requireContext(),
                    R.drawable.sure,
                    "Уверены?",
                    "Вы точно хотите сбросить весь прогресс? Это действие нельзя отменить.",
                    true,
                    "Сбросить",
                    onPositiveButtonCallback = {

                        progressViewModel.deleteProgress(login!!)
                        resetProgressUI()

                    }
                ).show()
            }



            backButton.setOnClickListener {
                requireActivity().supportFragmentManager.popBackStack()
            }
        }
    }


    private fun completeTheory(number: Int) {
        val theoryTextViews = arrayOf(
            binding.theory1, binding.theory2, binding.theory3, binding.theory4,
            binding.theory5, binding.theory6, binding.theory7, binding.theory8
        )

        if (number in 1..theoryTextViews.size) {
            theoryTextViews[number - 1].text = "Изучено"
        }
    }

    private fun completeTest(number: Int, progress: String) {
        val testTextViews = arrayOf(
            binding.test1, binding.test2, binding.test3, binding.test4,
            binding.test5, binding.test6, binding.test7, binding.test8
        )

        if (number in 1..testTextViews.size) {
            testTextViews[number - 1].text = "$progress/10"
        }
    }

    private fun resetProgressUI() {
        // Сбросить текст всех TextView для теории и тестов
        val theoryTextViews = arrayOf(
            binding.theory1, binding.theory2, binding.theory3, binding.theory4,
            binding.theory5, binding.theory6, binding.theory7, binding.theory8
        )
        val testTextViews = arrayOf(
            binding.test1, binding.test2, binding.test3, binding.test4,
            binding.test5, binding.test6, binding.test7, binding.test8
        )

        // Очистить текст всех TextView
        theoryTextViews.forEach { it.text = "Не изучено" }
        testTextViews.forEach { it.text = "0/10" }
    }

    companion object {

        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String?, param2: String?) =
            Progress().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}