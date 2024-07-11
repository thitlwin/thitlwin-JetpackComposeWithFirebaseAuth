package com.thit.firebaseauthentication.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

abstract class BaseViewModel<UiState, Event>(initialState: UiState): ViewModel() {
    private val events = MutableSharedFlow<Event>(replay = 0)
    private val _uiState = MutableStateFlow(initialState)
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            events.collect{
                handleEvent(it)
            }
        }
    }

    protected abstract suspend fun handleEvent(event: Event)

    protected fun updateUiState(update: UiState.() -> UiState){
        _uiState.update { _uiState.value.update() }
    }

    fun onEvent(event: Event){
        viewModelScope.launch {
            events.emit(event)
        }
    }
}