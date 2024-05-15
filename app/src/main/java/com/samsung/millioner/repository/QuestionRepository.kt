package com.samsung.millioner.repository

import android.util.Log
import androidx.annotation.NonNull
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.samsung.millioner.model.QuestionModel

class QuestionRepository(
    private val onQuestionLoad: OnQuestionLoad,
    private val onResultAdded: OnResultAdded,
    private val onResultLoad: OnResultLoad,
) {
    private val firebaseFirestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private lateinit var quizId: String
    private val resultMap: HashMap<String, Long> = HashMap()
    private val currentUserId: String = FirebaseAuth.getInstance().currentUser!!.uid
    val results: Unit
        get() {
            firebaseFirestore.collection("Result").document(currentUserId)
                .get().addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val correct = task.result.getLong("correct")
                        val money = task.result.getLong("money")

                        if (correct != null && money != null) {
                            resultMap["correct"] = correct
                            resultMap["money"] = money
                            onResultLoad.onResultLoad(resultMap)
                        } else {
                            resultMap["money"] = 0
                            onResultLoad.onResultLoad(resultMap)
                            onResultLoad.onError(Exception("One or more values are null"))
                        }
                    } else {
                        onResultLoad.onError(task.exception)
                    }
                }
        }


    fun addResults(resultMap: HashMap<String?, Any?>?) {
        if (resultMap != null) {
            firebaseFirestore.collection("Result").document(currentUserId)
                .set(resultMap).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        onResultAdded.onSubmit()
                    } else {
                        onResultAdded.onError(task.exception)
                    }
                }
        }
    }

    fun setQuizId(quizId: String) {
        this.quizId = quizId
    }

    val questions: Unit
        get() {
            firebaseFirestore.collection("Theme").document(quizId)
                .collection("questions").get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val resultList = task.result!!.toObjects(QuestionModel::class.java)
                        Log.d("RESULT", resultList.toString())
                        Log.d("QUIZID", quizId)
                        onQuestionLoad.onLoad(resultList)
                    } else {
                        onQuestionLoad.onError(task.exception)
                    }
                }
        }

    interface OnResultLoad {
        fun onResultLoad(resultMap: HashMap<String, Long>)
        fun onError(e: Exception?)
    }

    interface OnQuestionLoad {
        fun onLoad(questionModels: List<QuestionModel>)
        fun onError(e: Exception?)
    }

    interface OnResultAdded {
        fun onSubmit(): Boolean
        fun onError(e: Exception?)
    }
}