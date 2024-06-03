package com.example.stydyrussian

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.stydyrussian.databinding.FragmentTheoryBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

@AndroidEntryPoint
class TheoryFragment : Fragment(), Theory_adapter.TheoryListener {
    private lateinit var binding: FragmentTheoryBinding
    private val adapter = Theory_adapter(this)

    private val theoryViewModel : TheoryViewModel by activityViewModels()

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    private val imageList = listOf(
        R.drawable.n_nn,
        R.drawable.glagol,
        R.drawable.ne_ni,
        R.drawable.sprazheniya,
        R.drawable.mestoimeniya,
        R.drawable.padezhi,
        R.drawable.speech,
        R.drawable.narechiya
    )
    lateinit var login: String
    val topicNames = listOf(
        "н/нн\nв причастиях",
        "(не)\nсовершенные\nглаголы",
        "Правила\nнаписания не/ни",
        "Спряжения\nглаголов",
        "Разряды\nместоимений",
        "Падежи",
        "Косвенная\nречь",
        "Правила\nнаписания\nнаречий"
    )


    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var progCount = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTheoryBinding.inflate(inflater)
        init()


        login = sharedPreferences.getString("login", "login")?: "login"



        try {
            theoryViewModel.theoryListStrokeUpdate(login)

            theoryViewModel.completedThemesList.observe(viewLifecycleOwner){
                Log.d("Dont panic", it.toString())
                it.forEach { themeNumber ->
                    adapter.theoryList[themeNumber - 1].isCompleted = true
                }

                adapter.notifyDataSetChanged()
            }
            theoryViewModel.progressCount.observe(viewLifecycleOwner){count ->
                binding.theoryPB.progress = count ?: 0
                binding.theoryTW.text = "Прогресс изучения $count/8"

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
        adapter.theoryList.clear()
        progCount = 0
    }


    private fun init() = with(binding) {
        theoryRecycler.layoutManager = GridLayoutManager(this@TheoryFragment.context, 3)
        theoryRecycler.adapter = adapter
//        val topicNames = listOf("н/нн\nв причастиях", "(не)\nсовершенные\nглаголы", "Правила\nнаписания не/ни","Спряжения\nглаголов","Разряды местоимений","Падежи","Косвенная\nречь","Правила\nнаписания\nнаречий")
        val list1 = imageList.mapIndexed { index, image ->
            val topicName = if (index < topicNames.size) topicNames[index] else "Тема ${index + 1}"
            Theory(image, topicName, false)
        }

        adapter.addItems(list1)
    }

    override fun onClick(theory: Theory) {
        val fileName = "theme${topicNames.indexOf(theory.title) + 1}"
        openFragmentWithBackStack(Theory_main.newInstance(theory.title, fileName, login))
    }


    companion object {

        @JvmStatic
        fun newInstance(param1: String?, param2: String?) =
            TheoryFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }


}