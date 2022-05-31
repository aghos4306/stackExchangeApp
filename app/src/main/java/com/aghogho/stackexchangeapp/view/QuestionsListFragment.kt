package com.aghogho.stackexchangeapp.view

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.aghogho.stackexchangeapp.R
import com.aghogho.stackexchangeapp.model.Item
import com.aghogho.stackexchangeapp.utils.Resources
import com.aghogho.stackexchangeapp.view.adapter.QuestionsAdapter
import com.aghogho.stackexchangeapp.viewmodel.StackExchangeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_questions_list.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay

@AndroidEntryPoint
class QuestionsListFragment: Fragment() {
    private val viewModel: StackExchangeViewModel by viewModels()
    private var job: Job? = null
    private lateinit var trendingQuestionsAdapter: QuestionsAdapter
    private lateinit var searchedQuestionsAdapter: QuestionsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_questions_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpRecyclerView()
        clearQuery.setOnClickListener {
            searchET.text.clear()
            hideKeyboard()
        }

        filterIcon.setOnClickListener {
            findNavController().navigate(R.id.action_questionsListFragment_to_tagsFragment)
        }

        viewModel.questions.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resources.Success -> {
                    shimmerQuestions.isGone = true
                    shimmerQuestions.stopShimmer()
                    questions_trending_rv.isGone = false
                    Log.d("QuestionsFragment", "onViewCreated + InSuccess + ${resource.data!!.items}")
                    trendingQuestionsAdapter.submitList(resource.data.items)
                    Log.d("QuestionsFragment", "onViewCreated: + inAdapter + ${trendingQuestionsAdapter.currentList} ")
                }

                is Resources.Error -> {
                    Toast.makeText(requireContext(), resource.message, Toast.LENGTH_SHORT).show()
                    Log.d("QuestionsFragment", "onViewCreated: + In Error")
                }

                is Resources.Loading -> {
                    Toast.makeText(requireContext(), "Loading", Toast.LENGTH_SHORT).show()
                    shimmerQuestions.isGone = false
                    shimmerQuestions.stopShimmer()
                }
            }
        }

        viewModel.searchQuestions.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resources.Success<*> -> {
                    progress_bar.isGone = true
                    if (resource.data!!.items.isNotEmpty()) {
                        searchedQuestionsAdapter.submitList(resource.data.items)
                        questions_searched_rv.isGone = false
                        null_search.isGone = true
                    } else {
                        null_search.isGone = false
                        questions_searched_rv.isGone = true
                    }
                }
                else -> {}
            }
        }

        searchET.doOnTextChanged { text, _, _, _ ->
            text?.let {
                if (it.trim().isNotEmpty() && it.trim().isNotEmpty()) {
                    clearQuery.isGone = false
                    searchIcon.isVisible = false
                    questions_trending_rv.isGone = true
                    //show progress bar loading
                    search_key_text.text = "Searched Questions"
                    progress_bar.isGone = false
                    null_search.isGone = true
                    error_network.isGone = true
                    questions_searched_rv.isGone = true
                    searchQuestion(it.trim().toString())
                } else {
                    clearQuery.isGone = true
                    searchIcon.isVisible = true
                    search_key_text.text = "Trending Questions"
                    questions_trending_rv.isGone = false
                    questions_searched_rv.isGone = true
                }
            }
        }
    }

    fun searchQuestion(query: String) {
        job?.cancel()
        job = viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            delay(2000)
            viewModel.searchQuestions(query)
        }
    }

    fun hideKeyboard() {
        val inputMethodManager: InputMethodManager =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(requireView().windowToken, 0)
    }

    fun setUpRecyclerView() {
        questions_trending_rv.setHasFixedSize(true)
        trendingQuestionsAdapter = QuestionsAdapter(object : QuestionsAdapter.OnClickListener {
            override fun openQuestion(questionItem: Item) {
                //super.openQuestion(questionItem)
                openQuestion(questionItem)
            }
        }, requireContext())
        questions_trending_rv.adapter = trendingQuestionsAdapter

        questions_searched_rv.setHasFixedSize(true)
        searchedQuestionsAdapter = QuestionsAdapter(object : QuestionsAdapter.OnClickListener {
            override fun openQuestion(questionItem: Item) {
                openQuestion(questionItem)
            }
        }, requireContext())
        questions_searched_rv.adapter = searchedQuestionsAdapter
    }

}