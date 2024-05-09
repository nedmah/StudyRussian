package com.example.stydyrussian

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
//import com.example.stydyrussian.Theory_adapter.Companion.theoryList
import com.example.stydyrussian.databinding.FragmentTheoryBinding


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"



class TheoryFragment : Fragment(),Theory_adapter.TheoryListener {
    private lateinit var binding: FragmentTheoryBinding
    private val adapter = Theory_adapter(this)
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

    val topicNames = listOf("н/нн\nв причастиях", "(не)\nсовершенные\nглаголы", "Правила\nнаписания не/ни","Спряжения\nглаголов","Разряды\nместоимений","Падежи","Косвенная\nречь","Правила\nнаписания\nнаречий")


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


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentTheoryBinding.inflate(inflater)
        init()
        Theory_main.theoryIndicator.forEachIndexed(){index,item ->
            if(item==1) adapter.theoryList[index].isCompleted = true
        }


        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onPause() {
        super.onPause()
        adapter.theoryList.clear()
    }

    override fun onStart() {
        super.onStart()
        val progress = Theory_main.theoryIndicator.count{it==1}
        binding.theoryPB.progress = progress
        binding.theoryTW.text = binding.theoryTW.text.replaceFirst("\\d+".toRegex(), progress.toString())


    }





    private fun init() = with(binding){
        theoryRecycler.layoutManager = GridLayoutManager(this@TheoryFragment.context,3)
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
        openFragmentWithBackStack(Theory_main.newInstance(theory.title,fileName))
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