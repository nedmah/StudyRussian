package com.example.stydyrussian

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.stydyrussian.databinding.FragmentPracticeBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [PracticeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PracticeFragment : Fragment(), Practice_adapter.PracticeListener {

    val adapter = Practice_adapter(this)
    private lateinit var binding: FragmentPracticeBinding
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
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }


    private fun init() = with(binding){
        val topicNames = listOf("н/нн в причастиях", "(не)совершенные глаголы", "не/ни","спряжения","разряды местоимений","падежи","косвенная речь","правила написания наречий")
        val list1 = topicNames.map { topicName ->
            Practice(topicName, false)
        }
        practiceRecycler.layoutManager = LinearLayoutManager(this@PracticeFragment.context)
        practiceRecycler.adapter= adapter
        adapter.addItems(list1)
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment PracticeFragment.
         */
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
        openFragmentWithBackStack(TestFragment.newInstance(null,null))
    }
}