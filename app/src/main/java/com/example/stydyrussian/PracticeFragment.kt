package com.example.stydyrussian

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.stydyrussian.RoomData.MainDb
import com.example.stydyrussian.databinding.FragmentPracticeBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class PracticeFragment : Fragment(), Practice_adapter.PracticeListener {

    val adapter = Practice_adapter(this)
    private lateinit var binding: FragmentPracticeBinding
    private lateinit var db: MainDb
    val topicNames = listOf("н/нн в причастиях", "(не)совершенные глаголы", "Правила написания не/ни","Спряжения","Разряды местоимений","Падежи","Косвенная речь","Правила написания наречий")
    lateinit var login : String
    private var progCount = 0

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
        binding = FragmentPracticeBinding.inflate(inflater)
        init()
        db = MainDb.getDb(requireContext())
        val sharedPreferences = requireContext().getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE)
        login = sharedPreferences.getString("login", "login")!!

//        TestFragment.testIndicator.forEachIndexed(){index,item ->
//            if(item==1) adapter.practiceList[index].isCompletedTest = true
//        }

        try {
            viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO){
                val id = db.getUsersDao().getUserIdByLogin(login)!!
                db.getProgressDao().getTestProgressMoreThan7(id).toSet().forEach{
                    themeNumber -> adapter.practiceList[themeNumber - 1].isCompletedTest = true
                    progCount+=1 //TODO: небольшой баг есть
                }
                withContext(Dispatchers.Main){
                    binding.pracPB.progress = progCount
                    binding.pracTW.text = binding.pracTW.text.replaceFirst("\\d+".toRegex(), progCount.toString())
                }
            }
        }catch (e: Exception){
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
    }



    private fun init() = with(binding){

        val list1 = topicNames.map { topicName ->
            Practice(topicName, false)
        }
        practiceRecycler.layoutManager = LinearLayoutManager(this@PracticeFragment.context)
        practiceRecycler.adapter= adapter
        adapter.addItems(list1)
    }


    companion object {

        // TODO: Rename and change types and number of parameters
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
        openFragmentWithBackStack(TestFragment.newInstance(topicNames.indexOf(practice.title),practice.title,login))
    }
}