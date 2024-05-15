package com.samsung.millioner.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser
import com.samsung.millioner.repository.AuthenticationRepository

public class AuthViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: AuthenticationRepository
    private val userData: MutableLiveData<FirebaseUser>
    private val loggedStatus: MutableLiveData<Boolean>

    fun getUserData(): MutableLiveData<FirebaseUser> {
        return userData
    }

    fun getLoggedStatus(): MutableLiveData<Boolean> {
        return loggedStatus
    }


    init {
        repository = AuthenticationRepository(application)
        userData = repository.getFirebaseUserMutableLiveData()
        loggedStatus = repository.getUserLoggedMutableLiveData()
    }

    fun register(email: String?, pass: String?) {
        repository.register(email, pass)
    }

    fun signIn(email: String?, pass: String?) {
        repository.login(email, pass)
    }

    fun signOut() {
        repository.signOut()
    }
}