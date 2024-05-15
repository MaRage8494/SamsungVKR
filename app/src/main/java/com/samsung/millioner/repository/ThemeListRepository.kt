package com.samsung.millioner.repository

import android.util.Log
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.samsung.millioner.model.ThemeListModel

class ThemeListRepository {
    private val firebaseFirestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val reference: CollectionReference = firebaseFirestore.collection("Theme")

    fun getThemeData(onFirestoreTaskComplete: OnFirestoreTaskComplete) {
        reference.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                onFirestoreTaskComplete.themeDataLoaded(
                    task.result.toObjects(ThemeListModel::class.java)
                )
            } else {
                onFirestoreTaskComplete.onError(task.exception)
            }
        }
    }

    interface OnFirestoreTaskComplete {
        fun themeDataLoaded(themeListModels: List<ThemeListModel>?)
        fun onError(e: Exception?)
    }
}
