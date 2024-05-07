package com.example.stydyrussian

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import com.example.stydyrussian.databinding.FragmentTestBinding
import com.example.stydyrussian.databinding.FragmentTheoryBinding
import com.example.stydyrussian.testData.Answer
import com.example.stydyrussian.testData.Question
import com.example.stydyrussian.testData.allTests

// TODO: Rename parameter arguments, choose names that match
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class TestFragment : Fragment(), View.OnClickListener {

    private lateinit var binding : FragmentTestBinding
    private var param1: String? = null
    private var param2: String? = null
    private var themeName = ""
    private var themeIndex = 0

    private lateinit var testQuestions: List<Question>
    private var currentQuestionIndex: Int = 0
    private var correctCounter = 0


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
        binding = FragmentTestBinding.inflate(inflater)
        themeName = arguments?.getString(ARG_PARAM2)!!
        binding.testTheme.text = "Тема:  $themeName"
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {

            themeIndex = arguments?.getInt(ARG_PARAM1)!!
            testQuestions = allTests[themeIndex!!]
            showQuestion(0)

            button1.setOnClickListener(this@TestFragment)
            button2.setOnClickListener(this@TestFragment)
            button3.setOnClickListener(this@TestFragment)




            backButton.setOnClickListener {
                requireActivity().supportFragmentManager.popBackStack()
            }
        }
    }




    private fun showQuestion(index: Int)= with(binding){

        val question = testQuestions[index]
        mainText.text = question.text

        val answers = question.answers
        button1.text = answers[0].text
        button2.text = answers[1].text
        button3.text = answers[2].text
    }

    private fun checkAnswer(selectedAnswerIndex: Int) {
        val correctAnswerIndex = testQuestions[currentQuestionIndex].answers.indexOfFirst { it.isCorrect }
        val isAnswerCorrect = selectedAnswerIndex == correctAnswerIndex
        binding.testTW.text = "${currentQuestionIndex+1}/10"
        binding.testPB.progress = currentQuestionIndex+1
        // Оценить ответ и выполнить необходимые действия (например, обновить счетчик правильных ответов)
        if (isAnswerCorrect){
            correctCounter++
        }else{

        }

        // Перейти к следующему вопросу
        currentQuestionIndex++

        // Если еще остались вопросы, показать следующий вопрос
        if (currentQuestionIndex < testQuestions.size) {
            showQuestion(currentQuestionIndex)
        } else {
            // Если это был последний вопрос, завершить тест или выполнить действия по завершению теста

            val message = if (correctCounter > 7) {
                "Поздравляем! Вы прошли тест на $correctCounter из 10. Можно с уверенностью сказать, что вы усвоили тему $themeName"
            } else {
                "Уже неплохо!! Вы прошли тест на $correctCounter из 10. Но чтобы закрепить тему $themeName, повторите теорию и пропробуйте ещё раз."
            }

            val drawableResId = if (correctCounter > 7) {
                R.drawable.congrats
            } else {
                R.drawable.learn
            }

            CustomDialog(
                requireContext(),
                drawableResId,
                if (correctCounter > 7) "Поздравляем!" else "Уже неплохо!!",
                message,
                false,
                "Понятно"
            ) {
                requireActivity().supportFragmentManager.popBackStack()
            }.show()




            testIndicator[themeIndex] = if (correctCounter > 7) 1 else 0
        }
    }

    override fun onClick(view: View?) = with(binding) {
        when (view?.id) {
            button1.id -> checkAnswer(0)
            button2.id -> checkAnswer(1)
            button3.id -> checkAnswer(2)
        }
    }


    companion object {
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: Int?, param2: String?) =
            TestFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_PARAM1, param1!!)
                    putString(ARG_PARAM2, param2)
                }
            }

        val testIndicator = MutableList(8) { 0 }
    }
}