package com.thefallendeveloper.indianmetro.features.auth.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thefallendeveloper.indianmetro.corecommon.libs.navigation.FeatureNavigator
import com.thefallendeveloper.indianmetro.features.auth.navigation.AuthNavigationRoutes
import com.thefallendeveloper.indianmetro.features.auth.navigation.OtpEntryArgs
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.scan
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PhoneEntryViewModel(
    private val featureNavigator: FeatureNavigator<AuthNavigationRoutes>,
) : ViewModel() {
    private val events = MutableSharedFlow<Event>()

    val state: StateFlow<State> =
        events
            .scan(initState()) { prevState, event ->
                when (event) {
                    is Event.PhoneNumberChanged -> reduceStateOnPhoneNumberChanged(prevState, event)
                    Event.ContinueClicked -> reduceStateOnContinueButtonClicked(prevState)
                }
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.Eagerly,
                initialValue = initState(),
            )

    fun emitEvent(event: Event) {
        viewModelScope.launch {
            events.emit(event)
            if (event == Event.ContinueClicked && state.value.continueClickable) {
                featureNavigator.navigateTo(
                    AuthNavigationRoutes.OtpEntry(
                        args = OtpEntryArgs(phoneNumber = state.value.phoneNumber),
                    ),
                )
            }
        }
    }

    sealed class Event {
        data class PhoneNumberChanged(
            val value: String,
        ) : Event()

        data object ContinueClicked : Event()
    }

    data class State(
        val phoneNumber: String,
    ) {
        val continueClickable: Boolean
            get() = phoneNumber.length == MAX_PHONE_LENGTH
    }

    private fun initState() =
        State(
            phoneNumber = "",
        )

    private fun reduceStateOnPhoneNumberChanged(
        prevState: State,
        event: Event.PhoneNumberChanged,
    ): State {
        val value = event.value.filter(Char::isDigit).take(MAX_PHONE_LENGTH)
        return prevState.copy(phoneNumber = value)
    }

    private fun reduceStateOnContinueButtonClicked(prevState: State): State = prevState

    private companion object {
        const val MAX_PHONE_LENGTH = 10
    }
}
