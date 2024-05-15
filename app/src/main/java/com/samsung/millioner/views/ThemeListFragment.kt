package com.samsung.millioner.views

import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.samsung.millioner.R
import com.samsung.millioner.adapter.ThemeListAdapter
import com.samsung.millioner.model.ThemeListModel
import com.samsung.millioner.viewmodel.ThemeListViewModel

class ThemeListFragment : Fragment(), ThemeListAdapter.OnItemClickedListner {
        private var recyclerView: RecyclerView? = null
        private var progressBar: ProgressBar? = null
        private var navController: NavController? = null
        private var viewModel: ThemeListViewModel? = null
        private var adapter: ThemeListAdapter? = null
    private lateinit var mediaPlayer: MediaPlayer


    override fun onViewCreated(@NonNull view: View, @Nullable savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.themeListRecyclerView)
        progressBar = view.findViewById(R.id.themeListProgressBar)
        navController = Navigation.findNavController(view)

        adapter = ThemeListAdapter(this)
        Log.d("Adapter", adapter.toString())

        (recyclerView as? RecyclerView)?.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = this@ThemeListFragment.adapter // Устанавливаем свой собственный адаптер
            Log.d("Adapter", adapter.toString()) // Теперь должен быть не null
        }


        viewModel?.getThemeListLiveData()?.observe(viewLifecycleOwner) { themeListModels ->
            Log.d("ThemeListModels", themeListModels.toString())
            progressBar?.visibility = View.GONE
            adapter?.setThemeListModels(themeListModels)
            adapter?.notifyDataSetChanged()
        }
    }

    override fun onItemClick(themeId: Int) {
        Log.d("ThemeID", themeId.toString())
        val action: ThemeListFragmentDirections.ActionThemeListFragmentToDetailFragment =
            ThemeListFragmentDirections.actionThemeListFragmentToDetailFragment()
        action.themeId = themeId
        navController!!.navigate(action)
    }

    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(
            this, ViewModelProvider.AndroidViewModelFactory
                .getInstance(requireActivity().application)
        )[ThemeListViewModel::class.java]
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
        return inflater.inflate(R.layout.fragment_theme_list, container, false)
    }
}