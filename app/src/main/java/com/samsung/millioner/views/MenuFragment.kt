package com.samsung.millioner.views

import android.media.MediaPlayer
import android.opengl.Visibility
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
import com.samsung.millioner.viewmodel.AuthViewModel
import com.samsung.millioner.viewmodel.QuestionViewModel

class MenuFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_menu, container, false)
    }
    private lateinit var signOutBtn: Button
    private lateinit var menuStartBtn: Button
    private lateinit var menuRulesBtn: Button
    private lateinit var menuProgressBar: ProgressBar
    private lateinit var viewModel: AuthViewModel
    private lateinit var navController: NavController
    private lateinit var record: TextView
    private lateinit var questionViewModel: QuestionViewModel
    private lateinit var mediaPlayer: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory
            .getInstance(requireActivity().application)).get(AuthViewModel::class.java)
        viewModel.getLoggedStatus().observe(this, Observer { status ->
            if (status) {
                navController.navigate(R.id.action_menuFragment_to_signInFragment)
            }
        })

         questionViewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
        )[QuestionViewModel::class.java]

        mediaPlayer = MediaPlayer.create(requireContext(), R.raw.introducing)

        mediaPlayer.start()

        mediaPlayer.setOnCompletionListener {
            mediaPlayer.release()
            mediaPlayer = MediaPlayer.create(requireContext(), R.raw.background)
            mediaPlayer.isLooping = true
            mediaPlayer.start()
        }
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
        signOutBtn = view.findViewById(R.id.signOutBtn)
        menuStartBtn = view.findViewById(R.id.menuStartBtn)
        menuRulesBtn = view.findViewById(R.id.menuRulesBtn)
        record = view.findViewById(R.id.menuRecord)
        menuProgressBar = view.findViewById(R.id.menuProgressBar)

        navController = Navigation.findNavController(view)

        signOutBtn.setOnClickListener {
            viewModel.signOut();
            navController.navigate(R.id.action_menuFragment_to_signInFragment)
        }
        menuStartBtn.setOnClickListener {
            navController.navigate(R.id.action_menuFragment_to_themeListFragment)
        }
        menuRulesBtn.setOnClickListener {
            navController.navigate(R.id.action_menuFragment_to_rulesFragment)
        }

        questionViewModel.results
        questionViewModel.getResultMutableLiveData().observe(viewLifecycleOwner, Observer { stringLongHashMap ->
            val moneyCount = stringLongHashMap["money"]
            record.text = "${moneyCount.toString()}р"
            menuProgressBar.visibility = View.INVISIBLE
            record.visibility = View.VISIBLE
        })
    }
}