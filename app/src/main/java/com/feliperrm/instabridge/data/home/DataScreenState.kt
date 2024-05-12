package com.feliperrm.instabridge.data.home

import com.feliperrm.instabridge.data.countries.Country

sealed class DataScreenState {
    data class SelectData(
        val selectedCountry: Country,
        val selectedValue: Float,
        val selectedPercentage: Float,
        val internetHours: Int,
        val musicHours: Int,
        val videoHours: Int,
        val listOfShortCuts: List<Float>,
        val network: Network,
        val planType: PlanType,
        val price: String
    ) : DataScreenState()
}

enum class Network {
    Vodafone
}

enum class PlanType {
    DataOnly
}