package com.example.stydyrussian

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.stydyrussian.RoomData.MainDb
import com.example.stydyrussian.databinding.FragmentPracticeBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

@AndroidEntryPoint
class PracticeFragment : Fragment(), Practice_adapter.PracticeListener {

    private lateinit var binding: FragmentPracticeBinding
    val adapter = Practice_adapter(this)
    private val practiceViewModel: PracticeViewModel by activityViewModels()

    val topicNames = listOf(
        "н/нн в причастиях",
        "(не)совершенные глаголы",
        "Правила написания не/ни",
        "Спряжения",
        "Разряды местоимений",
        "Падежи",
        "Косвенная речь",
        "Правила написания наречий"
    )
    lateinit var login: String
    private var progCount = 0

    @Inject
    lateinit var sharedPreferences: SharedPreferences

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
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPracticeBinding.inflate(inflater)
        init()

        login = sharedPreferences.getString("login", "login")!!


        try {
            practiceViewModel.practiceListStrokeUpdate(login)

            practiceViewModel.completedThemesList.observe(viewLifecycleOwner) {
                Log.d("Dont panic", it.toString())

                if (it.isNotEmpty()) {
                    it.forEach { themeNumber ->
                        adapter.practiceList[themeNumber - 1].isCompletedTest = true
                    }
                }
                adapter.notifyDataSetChanged()

                practiceViewModel.progressCount.observe(viewLifecycleOwner) { count ->
                    binding.pracPB.progress = count ?: 0
                    binding.pracTW.text = "Прогресс прохождения $count/8"

                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onPause() {
        super.onPause()
        adapter.practiceList.clear()
        progCount = 0
    }


    private fun init() = with(binding) {

        val list1 = topicNames.map { topicName ->
            Practice(topicName, false)
        }
        practiceRecycler.layoutManager = LinearLayoutManager(this@PracticeFragment.context)
        practiceRecycler.adapter = adapter
        adapter.addItems(list1)
    }


    companion object {


        @JvmStatic
        fun newInstance(param1: String?, param2: String?) =
            PracticeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onClick(practice: Practice) {
        openFragmentWithBackStack(
            TestFragment.newInstance(
                topicNames.indexOf(practice.title),
                practice.title,
                login
            )
        )
    }
}