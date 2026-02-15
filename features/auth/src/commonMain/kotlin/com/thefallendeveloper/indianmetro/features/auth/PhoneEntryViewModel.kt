package com.thefallendeveloper.indianmetro.features.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PhoneEntryViewModel : ViewModel() {

    private val _state = MutableStateFlow(State())
    val state: StateFlow<State> = _state.asStateFlow()

    private val _effects = MutableSharedFlow<Effect>()
    val effects: SharedFlow<Effect> = _effects.asSharedFlow()

    fun onEvent(event: Event) {
        when (event) {
            is Event.PhoneNumberChanged -> reducePhoneChanged(event.value)
            Event.ContinueClicked -> handleContinueClicked()
        }
    }

    private fun reducePhoneChanged(input: String) {
        val phone = input.filter(Char::isDigit).take(MAX_PHONE_LENGTH)
        _state.update {
            it.copy(
                phoneNumber = phone,
                isContinueEnabled = phone.length == MAX_PHONE_LENGTH,
            )
        }
    }

    private fun handleContinueClicked() {
        val currentState = _state.value
        if (!currentState.isContinueEnabled) {
            return
        }
        viewModelScope.launch {
            _effects.emit(Effect.NavigateToOtp(phoneNumber = currentState.phoneNumber))
        }
    }

    data class State(
        val phoneNumber: String = "",
        val isContinueEnabled: Boolean = false,
    )

    sealed class Event {
        data class PhoneNumberChanged(
            val value: String,
        ) : Event()

        data object ContinueClicked : Event()
    }

    sealed class Effect {
        data class NavigateToOtp(
            val phoneNumber: String,
        ) : Effect()
    }

    private companion object {
        const val MAX_PHONE_LENGTH = 10
    }
}
