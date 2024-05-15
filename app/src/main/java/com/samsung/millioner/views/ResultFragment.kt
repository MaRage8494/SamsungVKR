package com.samsung.millioner.views

import android.media.MediaPlayer
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.samsung.millioner.R
import com.samsung.millioner.viewmodel.QuestionViewModel


class ResultFragment : Fragment() {

    private lateinit var againBtn: Button
    private lateinit var mainMenuBtn: Button
    private lateinit var navController: NavController
    private lateinit var viewModel: QuestionViewModel
    private lateinit var correctAnswer: TextView
    private lateinit var money: TextView
    private lateinit var resultField: TextView
    private lateinit var resultProgressBar: ProgressBar
    private var quizId: String? = null
    private lateinit var mediaPlayer: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(
            this, ViewModelProvider.AndroidViewModelFactory
                .getInstance(requireActivity().application)
        )[QuestionViewModel::class.java]

        mediaPlayer = MediaPlayer.create(requireContext(), R.raw.results)
        mediaPlayer.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (this::mediaPlayer.isInitialized && mediaPlayer.isPlaying) {
            mediaPlayer.stop()
            mediaPlayer.release()
        }
    }
    override fun onPause() {
        super.onPause()
        if (this::mediaPlayer.isInitialized && mediaPlayer.isPlaying) {
            mediaPlayer.pause()
        }
    }
    override fun onResume() {
        super.onResume()
        if (this::mediaPlayer.isInitialized) {
            mediaPlayer.start()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        againBtn = view.findViewById(R.id.resultAgainBtn)
        mainMenuBtn = view.findViewById(R.id.resultMenuBtn)
        correctAnswer = view.findViewById(R.id.resultAnswers)
        money = view.findViewById(R.id.resultMoney)
        resultField = view.findViewById(R.id.resultField)
        resultProgressBar = view.findViewById(R.id.resultProgressBar)

        quizId = QuestionFragmentArgs.fromBundle(arguments ?: Bundle()).quizId
        viewModel.setQuizId(quizId!!)
        viewModel.results
        viewModel.getResultMutableLiveData().observe(viewLifecycleOwner, Observer { stringLongHashMap ->
            val correctCount = stringLongHashMap["correct"]
            val moneyCount = stringLongHashMap["money"]

            correctAnswer.text = "${correctCount.toString()}/10"
            money.text = "Банк: ${moneyCount.toString()}р"
            resultProgressBar.visibility = View.INVISIBLE
            correctAnswer.visibility = View.VISIBLE
            money.visibility = View.VISIBLE
            resultField.visibility = View.VISIBLE
        })


        navController = Navigation.findNavController(view)

        againBtn.setOnClickListener {
            navController.navigate(R.id.action_resultFragment_to_themeListFragment)
        }
        mainMenuBtn.setOnClickListener {
            navController.navigate(R.id.action_resultFragment_to_menuFragment)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_result, container, false)
    }


}