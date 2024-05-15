package com.samsung.millioner.repository

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

public class AuthenticationRepository {
    private var application: Application;
    private var firebaseUserMutableLiveData: MutableLiveData<FirebaseUser>
    private var userLoggedMutableLiveData: MutableLiveData<Boolean>
    private var auth: FirebaseAuth

    constructor(application: Application){
        this.application = application
        firebaseUserMutableLiveData = MutableLiveData<FirebaseUser>()
        userLoggedMutableLiveData = MutableLiveData<Boolean>()
        auth = FirebaseAuth.getInstance()
    }

    fun getFirebaseUserMutableLiveData(): MutableLiveData<FirebaseUser> {
        return firebaseUserMutableLiveData
    }

    fun getUserLoggedMutableLiveData(): MutableLiveData<Boolean> {
        return userLoggedMutableLiveData
    }


    fun register(email: String?, pass: String?) {
        auth!!.createUserWithEmailAndPassword(email!!, pass!!).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                firebaseUserMutableLiveData!!.postValue(auth!!.currentUser)
            } else {
                Toast.makeText(application, task.exception!!.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun login(email: String?, pass: String?) {
        auth!!.signInWithEmailAndPassword(email!!, pass!!).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                firebaseUserMutableLiveData!!.postValue(auth!!.currentUser)
            } else {
                Toast.makeText(application, task.exception!!.message, Toast.LENGTH_SHORT).show()
            }
        }
    }
    fun signOut() {
        auth!!.signOut()
    }
}