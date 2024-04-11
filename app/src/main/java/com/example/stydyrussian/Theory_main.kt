package com.example.stydyrussian

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.stydyrussian.databinding.FragmentTheoryBinding
import com.example.stydyrussian.databinding.FragmentTheoryMainBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Theory_main.newInstance] factory method to
 * create an instance of this fragment.
 */
class Theory_main : Fragment() {

    private lateinit var binding: FragmentTheoryMainBinding
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
        binding = FragmentTheoryMainBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {

            themeText.text = arguments?.getString(ARG_PARAM1)?.replace("\n"," ")

            val fileName = arguments?.getString(ARG_PARAM2)
            val resourceId = resources.getIdentifier(fileName, "raw", requireContext().packageName)
            val inputStream = resources.openRawResource(resourceId)
            val text = inputStream.bufferedReader().use { it.readText() }
            mainText.text = text

            theoryScroll.viewTreeObserver.addOnScrollChangedListener {
                if (theoryScroll.getChildAt(0).bottom <= (theoryScroll.height + theoryScroll.scrollY)) {

                }
            }

        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Theory_main.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(title: String?, param2: String?) =
            Theory_main().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, title)
                    putString(ARG_PARAM2, param2)
                }
            }
    }



}