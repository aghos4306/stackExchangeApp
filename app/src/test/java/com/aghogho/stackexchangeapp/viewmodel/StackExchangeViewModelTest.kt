package com.aghogho.stackexchangeapp.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule

import androidx.lifecycle.LifecycleOwner
import com.aghogho.stackexchangeapp.repository.StackExchangeRepository
import com.aghogho.stackexchangeapp.utils.CoroutineTestRule
import com.aghogho.stackexchangeapp.utils.LifeCycleTestOwner
import com.aghogho.stackexchangeapp.utils.Resources
import org.junit.After

import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.Mockito
import java.util.*

class StackExchangeViewModelTest {

    @get: Rule
    val coroutineTestRule = CoroutineTestRule()
//
    @get: Rule
    val instantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    private lateinit var questionsStateObserver: Observer<Resources.Loading<>>

    private lateinit var searchQuestionsObserver: Observer

    private lateinit var taggedQuestionsObserver: Observer

    private lateinit var stackExchangeRepository: StackExchangeRepository

    private lateinit var stackExchangeViewModel: StackExchangeViewModel

    private lateinit var lifeCycleTestOwner: LifeCycleTestOwner

    @Before
    fun setUp() {
        lifeCycleTestOwner = LifeCycleTestOwner()
        (lifeCycleTestOwner as LifeCycleTestOwner).onCreate()

        stackExchangeRepository = Mockito.mock(StackExchangeRepository::class.java)

        stackExchangeViewModel = StackExchangeViewModel(stackExchangeRepository)

        questionsStateObserver = Mockito.mock(Observer::class.java)
        stackExchangeViewModel.questions.observe(lifeCycleTestOwner, questionsStateObserver)
    }

    @Test
    fun whenGetActiveQuestions_thenGetActiveQuestionsCalled() {
        coroutineTestRule.testDispatcher.runBlockingTest {
            lifeCycleTestOwner.onResume().run {
                lifeCycleTestOwner.onResume()
                //Given
                Mockito.`when`(stackExchangeRepository.fetchQuestions()).thenReturn(null)
                //when
                stackExchangeViewModel.questions
                //Then
                Mockito.verify(stackExchangeRepository, Mockito.times(2)).fetchQuestions()
                Mockito.verify(questionsStateObserver).onChanged(Resources.Loading)
            }
        }
    }

    @After
    fun tearDown() {
        lifeCycleTestOwner.onDestroy()
    }
}