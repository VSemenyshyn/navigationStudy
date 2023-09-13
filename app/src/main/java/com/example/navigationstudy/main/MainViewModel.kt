package com.example.navigationstudy.main

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {
    private val _randomId:  MutableStateFlow<Int> = MutableStateFlow(Random.nextInt(0, 100))
    val randomId: StateFlow<Int> get() = _randomId
}