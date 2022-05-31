package com.aghogho.stackexchangeapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aghogho.stackexchangeapp.model.StackExchangeResponse
import com.aghogho.stackexchangeapp.repository.StackExchangeRepository
import com.aghogho.stackexchangeapp.utils.Resources
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class StackExchangeViewModel @Inject constructor(
    private val stackExchangeRepository: StackExchangeRepository
): ViewModel() {

    private val _questions = MutableLiveData<Resources<StackExchangeResponse>>()
    val questions: LiveData<Resources<StackExchangeResponse>> get() = _questions

    private val _searchedQuestions: MutableLiveData<Resources<StackExchangeResponse>> = MutableLiveData()
    val searchQuestions: LiveData<Resources<StackExchangeResponse>> = _searchedQuestions

    private val _taggedQuestions: MutableLiveData<Resources<StackExchangeResponse>> = MutableLiveData()
    val taggedQuestions: LiveData<Resources<StackExchangeResponse>> = _taggedQuestions

    init {
        getActiveQuestions()
    }

    fun getActiveQuestions() = viewModelScope.launch {
        _questions.postValue(Resources.Loading())
        Log.d("ViewModelHey", "getActiveQuestions: Status=${_questions.value}")
        val response = stackExchangeRepository.fetchQuestions()
        _questions.postValue(safeHandleResponse(response))
        Log.d("ViewModelHey", "getActiveQuestions: Status=${questions.value} + ${response.body()}")
    }

    private suspend fun safeHandleResponse(response: Response<StackExchangeResponse>): Resources<StackExchangeResponse>? {
        return withContext(Dispatchers.IO) {
            try {
                Resources.Success(data = response.body()!!)
            } catch (e: Exception) {
                Resources.Error(e.message.toString())
            }
        }
    }

    fun searchQuestions(query: String) = viewModelScope.launch {
        _searchedQuestions.postValue(Resources.Loading())
        val response = stackExchangeRepository.searchQuestions(query)
        _searchedQuestions.postValue(safeHandleSearchResponse(response))
    }

    private suspend fun safeHandleSearchResponse(response: Response<StackExchangeResponse>): Resources<StackExchangeResponse>? {
        return withContext(Dispatchers.IO) {
            try {
                Resources.Success(data = response.body()!!)
            } catch (e: Exception) {
                Resources.Error(e.message.toString())
            }
        }
    }

    fun searchWithFilterTags(tags: String) = viewModelScope.launch {
        _taggedQuestions.postValue(Resources.Loading())
        val response = stackExchangeRepository.searchWithFilterTags(tags)
        _taggedQuestions.postValue(safeHandleTaggedResponse(response))
    }

    private suspend fun safeHandleTaggedResponse(response: Response<StackExchangeResponse>): Resources<StackExchangeResponse>? {
        return withContext(Dispatchers.IO) {
            try {
                Resources.Success(data = response.body()!!)
            } catch (e: Exception) {
                Resources.Error(e.message.toString())
            }
        }
    }

}