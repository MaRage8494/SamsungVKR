package com.samsung.millioner.views

import android.media.MediaPlayer
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.samsung.millioner.R
import com.samsung.millioner.viewmodel.AuthViewModel

class SignInFragment : Fragment() {

    private lateinit var emailEdit: EditText
    private lateinit var passEdit: EditText
    private lateinit var signUpText: TextView
    private lateinit var signInBtn: Button
    private lateinit var viewModel: AuthViewModel
    private lateinit var navController: NavController
    private lateinit var mediaPlayer: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory
            .getInstance(requireActivity().application)).get(AuthViewModel::class.java)
        viewModel.getUserData().observe(this, Observer { firebaseUser ->
            if (firebaseUser != null) {
                navController.navigate(R.id.action_signInFragment_to_menuFragment)
            }
        })

        mediaPlayer = MediaPlayer.create(requireContext(), R.raw.background)
        mediaPlayer.isLooping = true
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_in, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        emailEdit = view.findViewById(R.id.emailEditSignIn)
        passEdit = view.findViewById(R.id.passEditSignIn)
        signUpText = view.findViewById(R.id.signUpText)
        signInBtn = view.findViewById(R.id.signInBtn)

        navController = Navigation.findNavController(view)

        signUpText.setOnClickListener {
            navController.navigate(R.id.action_signInFragment_to_signUpFragment)
        }
        signInBtn.setOnClickListener(View.OnClickListener {
            val email: String = emailEdit.text.toString()
            val pass: String = passEdit.text.toString()
            if (!email.isEmpty() && !pass.isEmpty()) {
                viewModel.signIn(email, pass)
            }
        })
    }

}