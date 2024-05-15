package com.samsung.millioner.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.samsung.millioner.model.QuestionModel
import com.samsung.millioner.repository.QuestionRepository

class QuestionViewModel : ViewModel(), QuestionRepository.OnQuestionLoad,
    QuestionRepository.OnResultAdded, QuestionRepository.OnResultLoad {
    private val questionMutableLiveData: MutableLiveData<List<QuestionModel>> = MutableLiveData<List<QuestionModel>>()
    private val repository: QuestionRepository = QuestionRepository(this, this, this)
    private val resultMutableLiveData: MutableLiveData<HashMap<String, Long>> = MutableLiveData<HashMap<String, Long>>()
    fun getResultMutableLiveData(): MutableLiveData<HashMap<String, Long>> {
        return resultMutableLiveData
    }

    val results: Unit
        get() {
            repository.results
        }

    fun getQuestionMutableLiveData(): MutableLiveData<List<QuestionModel>> {
        return questionMutableLiveData
    }

    fun addResults(resultMap: HashMap<String?, Any?>?) {
        repository.addResults(resultMap)
    }

    fun setQuizId(quizId: String) {
        repository.setQuizId(quizId)
    }

    val questions: Unit
        get() {
            repository.questions
        }

    override fun onLoad(questionModels: List<QuestionModel>) {
        questionMutableLiveData.value = questionModels
    }

    override fun onSubmit(): Boolean {
        return true
    }

    override fun onResultLoad(resultMap: HashMap<String, Long>) {
        resultMutableLiveData.value = resultMap
    }

    override fun onError(e: Exception?) {
        Log.d("QuizError", "onError: " + e!!.message)
    }
}