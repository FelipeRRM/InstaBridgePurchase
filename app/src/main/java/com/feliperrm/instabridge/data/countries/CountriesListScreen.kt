package com.feliperrm.instabridge.data.countries

import androidx.activity.ComponentActivity
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.animation.with
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.feliperrm.instabridge.R
import com.feliperrm.instabridge.countryCodeToEmojiFlag
import com.feliperrm.instabridge.data.DataScreenViewModel
import com.feliperrm.instabridge.ui.theme.Purple80

@Composable
fun CountriesListScreen(navController: NavController) {
    val viewModel: DataScreenViewModel = viewModel(
        viewModelStoreOwner = (LocalContext.current as ComponentActivity)
    )
    val screenState by viewModel.countriesListScreenState.collectAsState()
    when (val state = screenState) {
        is CountriesListState.Countries -> CountriesListScreen(state, viewModel.countriesListActions, navController)
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class, ExperimentalFoundationApi::class)
@Composable
private fun CountriesListScreen(screenState: CountriesListState.Countries, actions: CountriesListActions? = null, navController: NavController? = null) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Black)
    ) {


        var showSearch by remember {
            mutableStateOf(false)
        }

        AnimatedContent(targetState = showSearch,
            transitionSpec = {
                if (targetState) {
                    ((slideInHorizontally { width -> width } + fadeIn()).togetherWith(slideOutHorizontally { width -> -width } + fadeOut()))
                } else {
                    ((slideInHorizontally { width -> -width } + fadeIn()).togetherWith(slideOutHorizontally { width -> +width } + fadeOut()))
                }
                    .using(
                        SizeTransform(clip = false)
                    )
            }
        ) { isSearchVisible ->

            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {


                Icon(
                    modifier = Modifier
                        .padding(8.dp)
                        .clickable(
                            interactionSource = remember {
                                MutableInteractionSource()
                            },
                            indication = rememberRipple(bounded = false),
                            enabled = true,
                            onClick = {
                                navController?.popBackStack()
                            }
                        )
                        .padding(8.dp),
                    imageVector = Icons.Filled.ArrowBackIosNew,
                    contentDescription = null, tint = Color.Gray
                )


                if (!isSearchVisible) {
                    Text(
                        modifier = Modifier.weight(1f),
                        text = stringResource(id = R.string.select_locations), color = Color.White,
                        style = MaterialTheme.typography.titleMedium, textAlign = TextAlign.Center,
                    )
                }


                Icon(
                    modifier = Modifier
                        .padding(8.dp)
                        .clickable(
                            interactionSource = remember {
                                MutableInteractionSource()
                            },
                            indication = rememberRipple(bounded = false),
                            enabled = true,
                            onClick = {
                                showSearch = true
                            }
                        )
                        .padding(8.dp),
                    imageVector = Icons.Filled.Search,
                    contentDescription = null, tint = Color.LightGray
                )


                if (isSearchVisible) {
                    TextField(
                        modifier = Modifier.weight(1f),
                        value = screenState.search,
                        onValueChange = { newSearch -> actions?.updateSearch(newSearch) },
                        singleLine = true,
                        label = { Text(text = stringResource(id = R.string.search_locations)) },
                        colors = TextFieldDefaults.colors().copy(
                            unfocusedContainerColor = Color.Transparent,
                            focusedContainerColor = Color.Transparent,
                            errorContainerColor = Color.Transparent,
                            disabledContainerColor = Color.Transparent,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            unfocusedLabelColor = Purple80,
                            focusedLabelColor = Purple80,
                            focusedIndicatorColor = Purple80
                        )
                    )

                    Icon(
                        modifier = Modifier
                            .padding(8.dp)
                            .clickable(
                                interactionSource = remember {
                                    MutableInteractionSource()
                                },
                                indication = rememberRipple(bounded = false),
                                enabled = true,
                                onClick = {
                                    actions?.updateSearch("")
                                    showSearch = false
                                }
                            )
                            .padding(8.dp),
                        imageVector = Icons.Filled.Close,
                        contentDescription = null, tint = Color.Gray
                    )
                }
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(16.dp)
        ) {
            items(items = screenState.searchedCountries, key = {it.countryCode}) { country ->
                val isSelected = country == screenState.selectedCountry

                val bgColor by animateColorAsState(targetValue = if (isSelected) Purple80 else Color.DarkGray)

                Card(
                    modifier = Modifier
                        .clickable {
                            actions?.countryClicked(country)
                        }
                        .animateItemPlacement(),
                    colors = CardDefaults.cardColors().copy(
                        containerColor = bgColor
                    )
                ) {
                    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            modifier = Modifier.padding(start = 16.dp, end = 8.dp),
                            text = countryCodeToEmojiFlag(country.countryCode),
                            color = if (isSelected) Color.Black else Color.LightGray,
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            modifier = Modifier.padding(vertical = 16.dp),
                            text = country.countryName,
                            color = if (isSelected) Color.Black else Color.LightGray,
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }

            }
        }
    }
}

@Composable
@Preview
private fun Preview_DataScreen() {
    CountriesListScreen(
        screenState = CountriesListState.Countries(
            selectedCountry = Country("Sweden", "SE"),
            searchedCountries = listOf(
                Country("Sweden", "SE"),
                Country("Brazil", "BR")
            ),
            search = ""
        )
    )
}