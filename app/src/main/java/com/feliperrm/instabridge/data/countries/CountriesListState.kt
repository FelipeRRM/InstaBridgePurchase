package com.feliperrm.instabridge.data.countries

sealed class CountriesListState {
    data class Countries(
        val selectedCountry: Country,
        val searchedCountries: List<Country>,
        val search: String
    ) : CountriesListState()
}