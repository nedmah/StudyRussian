package com.example.stydyrussian

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.activity.viewModels
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.stydyrussian.RoomData.MainDb
import com.example.stydyrussian.RoomData.Progress
import com.example.stydyrussian.databinding.FragmentTheoryBinding
import com.example.stydyrussian.databinding.FragmentTheoryMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private const val ARG_PARAM3 = "param3"


class Theory_main : Fragment() {

    private lateinit var binding: FragmentTheoryMainBinding
    private var param1: String? = null
    private var param2: String? = null
    private var param3: String? = null
    private lateinit var db: MainDb
    var isCodeExecuted = false
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


            themeText.text = arguments?.getString(ARG_PARAM1)?.replace("\n"," ")
            val fileName = arguments?.getString(ARG_PARAM2)
            val login = arguments?.getString(ARG_PARAM3)
            val resourceId = resources.getIdentifier(fileName, "raw", requireContext().packageName)
            val inputStream = resources.openRawResource(resourceId)
            val text = inputStream.bufferedReader().use { it.readText() }
            mainText.text = text
            val digit = fileName?.filter { it.isDigit() }
            db = MainDb.getDb(requireContext())



            theoryScroll.viewTreeObserver.addOnScrollChangedListener {
                if (!isCodeExecuted && theoryScroll.getChildAt(0).bottom <= (theoryScroll.height + theoryScroll.scrollY)) {


                    try {
                        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO){
                            val id = db.getUsersDao().getUserIdByLogin(login!!)

                            val existingProgress = db.getProgressDao().getProgressByUserAndTheme(id!!, digit!!.toInt())

                            if (existingProgress != null) {
                                // Если запись уже существует, обновляем
                                existingProgress.isCompletedTheory = true
                                db.getProgressDao().insertProgress(existingProgress)
                            } else {
                                val progress = Progress(null, true, 0, id, digit.toInt())
                                db.getProgressDao().insertProgress(progress)
                            }

                        }
                    }catch (e: Exception){
                        e.printStackTrace()
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
        fun newInstance(title: String, themeName: String, login : String) =
            Theory_main().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, title)
                    putString(ARG_PARAM2, themeName)
                    putString(ARG_PARAM3, login)
                }
            }

    }




}