package com.feliperrm.instabridge.data

import androidx.lifecycle.ViewModel
import com.feliperrm.instabridge.data.countries.CountriesListActions
import com.feliperrm.instabridge.data.countries.CountriesListState
import com.feliperrm.instabridge.data.countries.Country
import com.feliperrm.instabridge.data.home.DataScreenActions
import com.feliperrm.instabridge.data.home.DataScreenState
import com.feliperrm.instabridge.data.home.Network
import com.feliperrm.instabridge.data.home.PlanType
import com.feliperrm.instabridge.formatWithOneDecimal
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Locale

class DataScreenViewModel : ViewModel() {

    init {
        println("Init VM called")
    }

    override fun onCleared() {
        println("VM Cleared")
        super.onCleared()
    }

    private val _dataHomeScreenState = MutableStateFlow(
        DataScreenState.SelectData(
            selectedCountry = Country("Sweden", "SE"),
            selectedValue = 5f,
            selectedPercentage = 0.0f,
            listOfShortCuts = listOf(5f, 10f, 15f, 20f),
            internetHours = 30,
            musicHours = 15,
            videoHours = 5,
            network = Network.Vodafone,
            planType = PlanType.DataOnly,
            price = "${(10 + 5.2f * 7f).toInt()} KR"
        )
    )

    val dataHomeScreenState: StateFlow<DataScreenState> = _dataHomeScreenState.asStateFlow()

    val dataHomeActions = object : DataScreenActions {

        override fun updatePercentage(percentage: Float) {
            val dataOptions = _dataHomeScreenState.value.listOfShortCuts
            val selectedData = dataOptions.first() + ((dataOptions.last() - dataOptions.first()) * percentage)

            val valueSelected = selectedData.formatWithOneDecimal().toFloat()

            if (dataOptions.find { it == valueSelected } != null) {
                updateValue(valueSelected) // Snaps the selection to a specific value if rounding it gives the same result
            } else {
                _dataHomeScreenState.value = _dataHomeScreenState.value.copy(
                    selectedValue = selectedData,
                    selectedPercentage = percentage,
                    internetHours = (6 * selectedData).toInt(),
                    musicHours = (3 * selectedData).toInt(),
                    videoHours = selectedData.toInt(),
                    price = "${(10 + selectedData * 7f).toInt()} KR"
                )
            }
        }

        override fun updateValue(value: Float) {
            val dataOptions = _dataHomeScreenState.value.listOfShortCuts
            val amountIncreased = value - dataOptions.first()
            val maxIncrease = dataOptions.last() - dataOptions.first()
            val percentageIncreased = amountIncreased / maxIncrease
            _dataHomeScreenState.value = _dataHomeScreenState.value.copy(
                selectedValue = value,
                selectedPercentage = percentageIncreased,
                internetHours = (6 * value).toInt(),
                musicHours = (3 * value).toInt(),
                videoHours = value.toInt(),
                price = "${(10 + value * 7f).toInt()} KR"
            )
        }
    }


    private val allOrderedCountries =
        Locale.getISOCountries().map { countryCode ->
            val locale = Locale("", countryCode)
            var countryName: String? = locale.displayCountry
            if (countryName == null) {
                countryName = "UnIdentified"
            }
            Country(countryName, countryCode)
        }.sortedWith(compareBy { it.countryName })


    private val _coutriesListScreenState = MutableStateFlow(
        CountriesListState.Countries(
            selectedCountry = Country("Sweden", "SE"),
            searchedCountries = allOrderedCountries,
            search = ""
        )
    )


    val countriesListScreenState: StateFlow<CountriesListState> = _coutriesListScreenState.asStateFlow()

    val countriesListActions = object : CountriesListActions {
        override fun updateSearch(searchString: String) {
            _coutriesListScreenState.value =
                _coutriesListScreenState.value.copy(
                    search = searchString,
                    searchedCountries = if (searchString.isBlank()) allOrderedCountries else allOrderedCountries.filter { it.countryName.contains(searchString, true) }
                )
        }

        override fun countryClicked(countryClicked: Country) {
            _coutriesListScreenState.value = _coutriesListScreenState.value.copy(selectedCountry = countryClicked)
            _dataHomeScreenState.value = _dataHomeScreenState.value.copy(selectedCountry = countryClicked)
        }
    }

}