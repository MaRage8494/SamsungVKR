package com.samsung.millioner.views

import android.media.MediaPlayer
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.samsung.millioner.R

class RulesFragment : Fragment() {

    lateinit var backBtn: Button
    private lateinit var navController: NavController
    private lateinit var mediaPlayer: MediaPlayer

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        backBtn = view.findViewById(R.id.rulesBackBtn)

        navController = Navigation.findNavController(view)

        backBtn.setOnClickListener {
            navController.navigate(R.id.action_rulesFragment_to_menuFragment)
        }

        mediaPlayer = MediaPlayer.create(requireContext(), R.raw.background)

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
        return inflater.inflate(R.layout.fragment_rules, container, false)
    }
}