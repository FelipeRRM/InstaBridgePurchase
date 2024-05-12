package com.feliperrm.instabridge

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, @StringRes val resourceId: Int? = null, val iconVector: ImageVector? = null) {
    data object Data : Screen("data", R.string.data, Icons.Filled.CloudDownload)
    data object Wifi : Screen("wifi", R.string.wifi, Icons.Filled.Wifi)
    data object Menu : Screen("menu", R.string.menu, Icons.Filled.Menu)
    data object CountriesList : Screen("countriesList")
    data object Success : Screen("success")
}