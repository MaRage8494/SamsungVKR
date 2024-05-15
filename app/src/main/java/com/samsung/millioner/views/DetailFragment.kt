package com.samsung.millioner.views

import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.samsung.millioner.R
import com.samsung.millioner.model.ThemeListModel
import com.samsung.millioner.viewmodel.ThemeListViewModel

class DetailFragment : Fragment() {
    private var navController: NavController? = null
    private var position = 0
    private var viewModel: ThemeListViewModel? = null
    private var quizId: String? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail, container, false)
    }

    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(
            this, ViewModelProvider.AndroidViewModelFactory
                .getInstance(requireActivity().application)
        )[ThemeListViewModel::class.java]
    }

    override fun onViewCreated(@NonNull view: View, @Nullable savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val title : TextView? = view.findViewById(R.id.detailTitleText)
        val startQuizBtn: Button? = view.findViewById(R.id.detailStartBtn)
        val backBtn: Button? = view?.findViewById(R.id.detailBackBtn)
        navController = Navigation.findNavController(view)
        position = DetailFragmentArgs.fromBundle(arguments ?: Bundle()).themeId
        val observe = viewModel?.getThemeListLiveData()?.observe(viewLifecycleOwner) { value ->
            value?.let { themeList ->
                val quiz: ThemeListModel = themeList[position]
                title?.text = quiz.title
                quizId = quiz.themeId
            }
        }

        startQuizBtn?.setOnClickListener(View.OnClickListener {
            val action: DetailFragmentDirections.ActionDetailFragmentToQuestionFragment =
                DetailFragmentDirections.actionDetailFragmentToQuestionFragment()
            action.quizId = quizId
            navController!!.navigate(action)
        })

        backBtn?.setOnClickListener{
            Log.d("TEST", "Click")
            navController!!.navigate(R.id.action_detailFragment_to_themeListFragment)
        }
    }
}