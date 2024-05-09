package com.example.stydyrussian

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.stydyrussian.RoomData.MainDb
import com.example.stydyrussian.databinding.FragmentProgressBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class Progress : Fragment() {
    // TODO: Rename and change types of parameters
    private var login: String? = null
    private var param2: String? = null
    private lateinit var binding: FragmentProgressBinding
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
        binding = FragmentProgressBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var id = 0

        binding.apply {
            db = MainDb.getDb(requireContext())

            try {
                viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO){
                    id = db.getUsersDao().getUserIdByLogin(login!!)!!
                    db.getProgressDao().getCompletedThemeNumbers(id).forEach{
                            themeNumber -> withContext(Dispatchers.Main){ completeTheory(themeNumber) }
                    }
                    db.getProgressDao().getTestScore(id).forEach {
                        withContext(Dispatchers.Main) {
                            completeTest(it.themeNumber, it.testProgress.toString())
                        }
                    }

                }
            }catch (e: Exception){
                e.printStackTrace()
            }


            button.setOnClickListener {
                try {
                    viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO){
                        db.getProgressDao().deleteProgressByUserId(id)
                    }
                }catch (e: Exception){
                    e.printStackTrace()
                }
            }



            backButton.setOnClickListener {
                requireActivity().supportFragmentManager.popBackStack()
            }
        }
    }


    private fun completeTheory(number: Int) {
        val theoryTextViews = arrayOf(binding.theory1, binding.theory2, binding.theory3, binding.theory4,
            binding.theory5, binding.theory6, binding.theory7, binding.theory8)

        if (number in 1..theoryTextViews.size) {
            theoryTextViews[number - 1].text = "Изучено"
        }
    }

    private fun completeTest(number: Int, progress: String) {
        val testTextViews = arrayOf(binding.test1, binding.test2, binding.test3, binding.test4,
            binding.test5, binding.test6, binding.test7, binding.test8)

        if (number in 1..testTextViews.size) {
            testTextViews[number - 1].text = "$progress/10"
        }
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