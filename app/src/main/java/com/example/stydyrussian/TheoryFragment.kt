package com.example.stydyrussian

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.stydyrussian.RoomData.MainDb
import com.example.stydyrussian.RoomData.Progress
//import com.example.stydyrussian.Theory_adapter.Companion.theoryList
import com.example.stydyrussian.databinding.FragmentTheoryBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class TheoryFragment : Fragment(),Theory_adapter.TheoryListener {
    private lateinit var binding: FragmentTheoryBinding
    private val adapter = Theory_adapter(this)
    private lateinit var db: MainDb
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
    lateinit var login : String
    val topicNames = listOf("н/нн\nв причастиях", "(не)\nсовершенные\nглаголы", "Правила\nнаписания не/ни","Спряжения\nглаголов","Разряды\nместоимений","Падежи","Косвенная\nречь","Правила\nнаписания\nнаречий")


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


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentTheoryBinding.inflate(inflater)
        init()

        val sharedPreferences = requireContext().getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE)
        login = sharedPreferences.getString("login", "login")!!


        db = MainDb.getDb(requireContext())

        try {
            viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO){
                val id = db.getUsersDao().getUserIdByLogin(login)!!
                db.getProgressDao().getCompletedThemeNumbers(id).toSet().forEach{
                    themeNumber -> adapter.theoryList[themeNumber - 1].isCompleted = true
                    progCount+=1
                }
                withContext(Dispatchers.Main){
                    binding.theoryPB.progress = progCount
                    binding.theoryTW.text = binding.theoryTW.text.replaceFirst("\\d+".toRegex(), progCount.toString())
                }
            }
        }catch (e: Exception){
            e.printStackTrace()
        }


//            it.forEach { themeNumber -> adapter.theoryList[themeNumber - 1].isCompleted = true }




        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onPause() {
        super.onPause()
        adapter.theoryList.clear()
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
        openFragmentWithBackStack(Theory_main.newInstance(theory.title,fileName,login))
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