package com.aghogho.stackexchangeapp.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListAdapter
import android.widget.Toast
import androidx.core.view.isGone
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import com.aghogho.stackexchangeapp.databinding.FragmentTagsBinding
import com.aghogho.stackexchangeapp.databinding.TagItemBinding
import com.aghogho.stackexchangeapp.model.Item
import com.aghogho.stackexchangeapp.utils.HandleOpeningUrl
import com.aghogho.stackexchangeapp.utils.Resources
import com.aghogho.stackexchangeapp.view.adapter.QuestionsAdapter
import com.aghogho.stackexchangeapp.viewmodel.StackExchangeViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_tags.*

@AndroidEntryPoint
class TagsFragment: BottomSheetDialogFragment() {

    private var _binding: FragmentTagsBinding? = null
    private val binding get() = _binding
    private val viewModel by activityViewModels<StackExchangeViewModel>()
    private lateinit var tagsAdapter: QuestionsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //return super.onCreateView(inflater, container, savedInstanceState)
        _binding = FragmentTagsBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpRecyclerView()

        close_tags.setOnClickListener {
            dismiss()
            tagsAdapter.submitList(null)
        }

        use_tags_button.setOnClickListener {
            tags_input_text.editText?.text?.toString()?.trim().let { tags ->
                tags_input_text.error = null
                viewModel.searchWithFilterTags(tags!!)
            } ?: kotlin.run { tags_input_text.error = "Pass a content pls, cant be empty" }
        }

        viewModel.taggedQuestions.observe(viewLifecycleOwner) { resource ->
            when(resource) {
                is Resources.Success -> binding?.apply {
                    useTagsButton.text = "Use Tags"
                    searchingTagsProgressbar.isGone = true

                    if(resource.data!!.items.isNotEmpty()) {
                        tagsAdapter.submitList(resource.data.items)
                        questionsAfterTagRv.isGone = false
                        nullSearchTags.isGone = true
                    } else {
                        nullSearchTags.isGone = false
                        questionsAfterTagRv.isGone = true
                    }
                }

                is Resources.Error -> binding?.apply {
                    searchingTagsProgressbar.isGone = true
                    errorNetworkTags.isGone = true
                    useTagsButton.text = "Use Tags"
                    Toast.makeText(requireContext(), "Error Error", Toast.LENGTH_SHORT).show()
                }

                is Resources.Loading -> binding?.apply {
                    searchingTagsProgressbar.isGone = false
                    errorNetworkTags.isGone = true
                    useTagsButton.text = "Loading..."
                    nullSearchTags.isGone = true
                    questionsAfterTagRv.isGone = true
                }
            }
        }
    }

    private fun setUpRecyclerView() {
        binding?.apply {
            questionsAfterTagRv.setHasFixedSize(true)
            tagsAdapter = QuestionsAdapter(object : QuestionsAdapter.OnClickListener {
                override fun openQuestion(questionItem: Item) {
                    HandleOpeningUrl(requireContext(), questionItem.link)
                }
            }, requireContext())
            questionsAfterTagRv.adapter = tagsAdapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}