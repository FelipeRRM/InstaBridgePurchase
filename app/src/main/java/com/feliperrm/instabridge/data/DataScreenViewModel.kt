package com.feliperrm.instabridge.data

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class DataScreenViewModel : ViewModel() {

    private val _screenState = MutableStateFlow(
        DataScreenState.SelectData(
            selectedCountry = "Sweden",
            selectedValue = 5.2f,
            selectedPercentage = 0.1f,
            listOfShortCuts = listOf(5f, 10f, 15f, 20f),
            internetHours = 30,
            musicHours = 15,
            videoHours = 5,
            network = Network.Vodafone,
            planType = PlanType.DataOnly
        )
    )
    val screenState: StateFlow<DataScreenState> = _screenState.asStateFlow()


    val actions = object : DataScreenActions {

        override fun updatePercentage(percentage: Float) {
            val dataOptions = _screenState.value.listOfShortCuts
            val selectedData = dataOptions.first() + ((dataOptions.last() - dataOptions.first()) * percentage)
            _screenState.value = _screenState.value.copy(
                selectedValue = selectedData,
                selectedPercentage = percentage,
                internetHours = (6 * selectedData).toInt(),
                musicHours = (3 * selectedData).toInt(),
                videoHours = selectedData.toInt()
            )
        }

        override fun selectShortcut(value: Float) {
            val dataOptions = _screenState.value.listOfShortCuts
            val amountIncreased = value - dataOptions.first()
            val maxIncrease = dataOptions.last() - dataOptions.first()
            val percentageIncreased = amountIncreased / maxIncrease
            _screenState.value = _screenState.value.copy(
                selectedValue = value,
                selectedPercentage = percentageIncreased,
                internetHours = (6 * value).toInt(),
                musicHours = (3 * value).toInt(),
                videoHours = value.toInt()
            )
        }
    }


}