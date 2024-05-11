package com.feliperrm.instabridge.data

sealed class DataScreenState {
    data class SelectData(
        val selectedCountry: String,
        val selectedValue: Float,
        val selectedPercentage: Float,
        val internetHours: Int,
        val musicHours: Int,
        val videoHours: Int,
        val listOfShortCuts: List<Float>,
        val network: Network,
        val planType: PlanType
    ) : DataScreenState()
}

enum class Network {
    Vodafone
}

enum class PlanType {
    DataOnly
}