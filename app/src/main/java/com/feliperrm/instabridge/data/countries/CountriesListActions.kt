package com.feliperrm.instabridge.data.countries

interface CountriesListActions {
    fun updateSearch(searchString: String)
    fun countryClicked(countryClicked: Country)
}