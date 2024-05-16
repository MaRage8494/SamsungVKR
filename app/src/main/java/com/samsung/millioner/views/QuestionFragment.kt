package com.samsung.millioner.views

import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.samsung.millioner.R
import com.samsung.millioner.viewmodel.QuestionViewModel
import kotlin.random.Random

class QuestionFragment : Fragment(), View.OnClickListener {
    private var mediaPlayer: MediaPlayer? = null
    private var mediaPlayerHint: MediaPlayer? = null
    private lateinit var viewModel: QuestionViewModel
    private var navController: NavController? = null
    private var progressBar: ProgressBar? = null
    private var option1Btn: Button? = null
    private var option2Btn: Button? = null
    private var option3Btn: Button? = null
    private var option4Btn: Button? = null
    private var questionPercentA: TextView? = null
    private var questionPercentB: TextView? = null
    private var questionPercentC: TextView? = null
    private var questionPercentD: TextView? = null
    private var nextBtn: Button? = null
    private var questionText: TextView? = null
    private var questionNumber: TextView? = null
    private var moneyBank: TextView? = null
    private var quizId: String? = null
    private var totalQuestions: Long = 10L
    private var currentQuestion = 0
    private var canAnswer = false
    private var usedHalf = false
    private var usedHelp = false
    private var usedLife = false
    private var life = false
    private var getWrong = false
    private var correctAnswer = 0
    private var hintHalf: ImageButton? = null
    private var hintHelp: ImageButton? = null
    private var hintLife: ImageButton? = null
    private var moneyIndex = 0
    private var moneyArray: IntArray =
        intArrayOf(0, 500, 1000, 5000, 10000, 25000, 50000, 100000, 250000, 500000, 1000000)
    private var answer = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
        )[QuestionViewModel::class.java]

        mediaPlayer = MediaPlayer.create(requireContext(), R.raw.background).apply {
            isLooping = true
            start()
            setOnCompletionListener {
                releaseMediaPlayer()
            }
        }
    }
    private fun releaseMediaPlayer() {
        mediaPlayer?.apply {
            try {
                if (isPlaying) {
                    stop()
                }
                release()
            } catch (e: IllegalStateException) {
                Log.e("MediaPlayer", "Error releasing MediaPlayer", e)
            }
            mediaPlayer = null
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        releaseMediaPlayer()
        releaseMediaPlayerHint()
    }

    override fun onPause() {
        super.onPause()
        try {
            if (mediaPlayer?.isPlaying == true) {
                mediaPlayer?.pause()
                Log.d("PAUSE", "TRUE")
            }
        } catch (e: IllegalStateException) {
            Log.e("MediaPlayer", "Error pausing MediaPlayer", e)
        }
    }

    override fun onResume() {
        super.onResume()
        try {
            mediaPlayer?.start()
            Log.d("RESUME", "TRUE")
        } catch (e: IllegalStateException) {
            Log.e("MediaPlayer", "Error resuming MediaPlayer", e)
        }
    }

    private fun releaseMediaPlayerHint() {
        mediaPlayerHint?.apply {
            try {
                if (isPlaying) {
                    stop()
                }
                release()
            } catch (e: IllegalStateException) {
                Log.e("MediaPlayerHint", "Error releasing MediaPlayerHint", e)
            }
            mediaPlayerHint = null
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_question, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        option1Btn = view.findViewById(R.id.questionAnswerA)
        option2Btn = view.findViewById(R.id.questionAnswerB)
        option3Btn = view.findViewById(R.id.questionAnswerC)
        option4Btn = view.findViewById(R.id.questionAnswerD)
        questionPercentA = view.findViewById(R.id.questionPercentA)
        questionPercentB = view.findViewById(R.id.questionPercentB)
        questionPercentC = view.findViewById(R.id.questionPercentC)
        questionPercentD = view.findViewById(R.id.questionPercentD)
        nextBtn = view.findViewById(R.id.questionFinishBtn)
        questionText = view.findViewById(R.id.questionText)
        questionNumber = view.findViewById(R.id.questionTitle)
        moneyBank = view.findViewById(R.id.questionBank)
        hintHalf = view.findViewById(R.id.questionHalf)
        hintHelp = view.findViewById(R.id.questionHelp)
        hintLife = view.findViewById(R.id.questionLife)
        progressBar = view.findViewById(R.id.questionProgressBar)
        quizId = QuestionFragmentArgs.fromBundle(arguments ?: Bundle()).quizId
        viewModel.setQuizId(quizId!!)
        viewModel.questions
        (hintHalf as ImageButton).setOnClickListener(this)
        (hintHelp as ImageButton).setOnClickListener(this)
        (hintLife as ImageButton).setOnClickListener(this)
        (option1Btn as Button).setOnClickListener(this)
        (option2Btn as Button).setOnClickListener(this)
        (option3Btn as Button).setOnClickListener(this)
        (option4Btn as Button).setOnClickListener(this)
        (nextBtn as Button).setOnClickListener(this)
        loadData()
    }


    private fun loadData() {
        enableOptions()
        loadQuestions(1)
    }

    private fun enableOptions() {
        option1Btn!!.visibility = View.VISIBLE
        option2Btn!!.visibility = View.VISIBLE
        option3Btn!!.visibility = View.VISIBLE
        option4Btn!!.visibility = View.VISIBLE


        option1Btn!!.isEnabled = false
        option2Btn!!.isEnabled = false
        option3Btn!!.isEnabled = false
        option4Btn!!.isEnabled = false
        hintHalf!!.isEnabled = false
        hintHelp!!.isEnabled = false
        hintLife!!.isEnabled = false
        nextBtn!!.visibility = View.INVISIBLE
    }

    private fun loadQuestions(i: Int) {
        currentQuestion = i
        viewModel.getQuestionMutableLiveData()
            .observe(viewLifecycleOwner) { value ->
                if (value.isNotEmpty() && value.size >= i) {
                    Log.d("Value", value.toString())
                    questionText?.text = value[i - 1].question
                    moneyBank?.text = "Банк: ${moneyArray[moneyIndex]}р"
                    option1Btn?.text = "A. ${value[i - 1].option_a}"
                    option2Btn?.text = "B. ${value[i - 1].option_b}"
                    option3Btn?.text = "C. ${value[i - 1].option_c}"
                    option4Btn?.text = "D. ${value[i - 1].option_d}"
                    answer = value[i - 1].answer!!
                    questionNumber?.text ="Вопрос №$currentQuestion"
                    option1Btn!!.isEnabled = true
                    option2Btn!!.isEnabled = true
                    option3Btn!!.isEnabled = true
                    option4Btn!!.isEnabled = true
                    hintHalf!!.isEnabled = true
                    hintHelp!!.isEnabled = true
                    hintLife!!.isEnabled = true
                } else {
                    Log.e("ERROR", "Value пустой")
                }
            }
        canAnswer = true
    }

    private fun showNextBtn() {
        if (currentQuestion.toLong() == totalQuestions || getWrong) {
            nextBtn!!.text = "Завершить"
            nextBtn!!.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.red)
            nextBtn!!.isEnabled = true
            nextBtn!!.visibility = View.VISIBLE
        } else {
            if(life){
                life = false
            }
            nextBtn!!.visibility = View.VISIBLE
            nextBtn!!.isEnabled = true
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.questionHalf -> useHintHalf()
            R.id.questionHelp -> useHintHelp()
            R.id.questionLife -> useHintLife()
            R.id.questionAnswerA -> verifyAnswer(option1Btn)
            R.id.questionAnswerB -> verifyAnswer(option2Btn)
            R.id.questionAnswerC -> verifyAnswer(option3Btn)
            R.id.questionAnswerD -> verifyAnswer(option4Btn)
            R.id.questionFinishBtn -> if (currentQuestion.toLong() == totalQuestions || getWrong) {
                submitResults()
            } else {
                try {
                    if (mediaPlayer != null && mediaPlayer!!.isPlaying) {
                        mediaPlayer!!.stop()
                    }
                    mediaPlayer?.reset()
                    mediaPlayer = MediaPlayer.create(requireContext(), R.raw.background).apply {
                        isLooping = true
                        start()
                    }
                } catch (e: IllegalStateException) {
                    e.printStackTrace()
                }
                currentQuestion++
                loadQuestions(currentQuestion)
                resetOptions()
            }
        }
    }

    private fun getTwoRandomNumbersExcluding(result: Int): Pair<Int, Int> {
        val numbers = (0..3).toMutableList()

        numbers.remove(result)

        numbers.shuffle()

        return Pair(numbers[0], numbers[1])
    }

    private fun getAnswerOption(): Int{
        var result = 0
        val buttons = listOf(option1Btn, option2Btn, option3Btn, option4Btn)
        buttons.forEachIndexed { index, button ->
            if (answer == button!!.text.substring(3)){
                result = index
            }
        }
        return result
    }

    private fun deleteSaturation (button: ImageButton?){
        val colorMatrix = ColorMatrix()
        colorMatrix.setSaturation(0f)

        val filter = ColorMatrixColorFilter(colorMatrix)
        button!!.colorFilter = filter
    }

    private fun generatePercent(correctIndex: Int): List<Int> {
        require(correctIndex in 0..3) { "Index must be between 0 and 3" }

        val correctValue = Random.nextInt(60, 91)

        val remainingSum = 100 - correctValue

        val value1 = Random.nextInt(0, remainingSum)
        val value2 = Random.nextInt(0, remainingSum - value1)
        val value3 = remainingSum - value1 - value2

        val values = mutableListOf(0, 0, 0, 0)
        values[correctIndex] = correctValue

        var idx = 0
        for (i in values.indices) {
            if (i != correctIndex) {
                when (idx) {
                    0 -> values[i] = value1
                    1 -> values[i] = value2
                    2 -> values[i] = value3
                }
                idx++
            }
        }

        return values
    }

    private fun useHintHalf() {
        val buttons = listOf(option1Btn, option2Btn, option3Btn, option4Btn)
        if (canAnswer && !usedHalf) {
            try {
                mediaPlayerHint?.apply {
                    if (isPlaying) {
                        stop()
                    }
                    reset()
                }
                mediaPlayerHint = MediaPlayer.create(requireContext(), R.raw.use_hint).apply {
                    start()
                    setOnCompletionListener {
                        releaseMediaPlayerHint()
                    }
                }
                val result = getAnswerOption()
                val (removeNum1, removeNum2) = getTwoRandomNumbersExcluding(result)
                buttons[removeNum1]!!.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.red)
                buttons[removeNum2]!!.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.red)
                buttons[removeNum1]!!.isEnabled = false
                buttons[removeNum2]!!.isEnabled = false
                usedHalf = true

                deleteSaturation(hintHalf)
            } catch (e: IllegalStateException) {
                e.printStackTrace()
            }
        }
    }


    private fun useHintHelp() {
        val percents = listOf(questionPercentA, questionPercentB, questionPercentC, questionPercentD)
        if (canAnswer && !usedHelp) {
            try {
                mediaPlayerHint?.apply {
                    if (isPlaying) {
                        stop()
                    }
                    reset()
                }
                mediaPlayerHint = MediaPlayer.create(requireContext(), R.raw.use_hint).apply {
                    start()
                    setOnCompletionListener {
                        releaseMediaPlayerHint()
                    }
                }
                val result = getAnswerOption()
                val values = generatePercent(result)

                percents.forEachIndexed { index, percentValue ->
                    percentValue!!.text = "${values[index]}%"
                    percentValue.visibility = View.VISIBLE
                }
                usedHelp = true

                deleteSaturation(hintHelp)
            } catch (e: IllegalStateException) {
                e.printStackTrace()
            }
        }
    }


    private fun useHintLife() {
        if (canAnswer && !usedLife) {
            try {
                mediaPlayerHint?.apply {
                    if (isPlaying) {
                        stop()
                    }
                    reset()
                }
                mediaPlayerHint = MediaPlayer.create(requireContext(), R.raw.use_hint).apply {
                    start()
                    setOnCompletionListener {
                        releaseMediaPlayerHint()
                    }
                }
                life = true
                usedLife = true
                deleteSaturation(hintLife)
            } catch (e: IllegalStateException) {
                e.printStackTrace()
            }
        }
    }


    private fun resetOptions() {
        nextBtn!!.visibility = View.INVISIBLE
        nextBtn!!.isEnabled = false
        option1Btn!!.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.yellow)
        option2Btn!!.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.yellow)
        option3Btn!!.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.yellow)
        option4Btn!!.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.yellow)
        option1Btn!!.isEnabled = true
        option2Btn!!.isEnabled = true
        option3Btn!!.isEnabled = true
        option4Btn!!.isEnabled = true
        questionPercentA!!.visibility = View.INVISIBLE
        questionPercentB!!.visibility = View.INVISIBLE
        questionPercentC!!.visibility = View.INVISIBLE
        questionPercentD!!.visibility = View.INVISIBLE
    }

    private fun submitResults() {
        val resultMap = HashMap<String?, Any?>()
        resultMap["correct"] = correctAnswer
        resultMap["money"] = moneyArray[moneyIndex]
        Log.d("Result", resultMap.toString())
        viewModel.addResults(resultMap)
        val action: QuestionFragmentDirections.ActionQuestionFragmentToResultFragment =
            QuestionFragmentDirections.actionQuestionFragmentToResultFragment()
        action.quizId = quizId
        navController?.navigate(action)
    }

    private fun verifyAnswer(button: Button?) {
        val buttons = listOf(option1Btn, option2Btn, option3Btn, option4Btn)
        if (canAnswer) {
            if (answer == button!!.text.substring(3)) {
                button.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.green)

                correctAnswer++
                moneyIndex++
                try {
                    if (mediaPlayer?.isPlaying == true) {
                        mediaPlayer?.stop()
                    }
                    mediaPlayer?.reset()
                    mediaPlayer = MediaPlayer.create(requireContext(), R.raw.correct).apply {
                        isLooping = false
                        start()
                    }
                } catch (e: IllegalStateException) {
                    e.printStackTrace()
                }
            } else {
                button.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.red)
                if (!life) {
                    val result = getAnswerOption()
                    buttons[result]!!.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.green)
                    getWrong = true
                    try {
                        if (mediaPlayer?.isPlaying == true) {
                            mediaPlayer?.stop()
                        }
                        mediaPlayer?.reset()
                        mediaPlayer = MediaPlayer.create(requireContext(), R.raw.wrong).apply {
                            isLooping = false
                            start()
                        }
                    } catch (e: IllegalStateException) {
                        e.printStackTrace()
                    }
                }
            }
        }
        if (!life || answer == button!!.text.substring(3)) {
            canAnswer = false
            showNextBtn()
        } else {
            life = false
        }
    }
}