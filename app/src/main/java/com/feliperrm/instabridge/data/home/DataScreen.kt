package com.feliperrm.instabridge.data.home

import CircularProgressBar
import android.app.Activity
import androidx.activity.ComponentActivity
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.material.ripple.RippleAlpha
import androidx.compose.material.ripple.RippleTheme
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.fragment.app.Fragment
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.feliperrm.instabridge.MainActivity
import com.feliperrm.instabridge.R
import com.feliperrm.instabridge.Screen
import com.feliperrm.instabridge.countryCodeToEmojiFlag
import com.feliperrm.instabridge.data.DataScreenViewModel
import com.feliperrm.instabridge.data.countries.Country
import com.feliperrm.instabridge.findClosestIndexNotAbove
import com.feliperrm.instabridge.formatWithOneDecimal
import com.feliperrm.instabridge.ui.theme.Purple40
import com.feliperrm.instabridge.ui.theme.Purple80

@Composable
fun DataScreen(navController: NavController) {
    val viewModel: DataScreenViewModel = viewModel(
        viewModelStoreOwner = (LocalContext.current as ComponentActivity)
    )
    val screenState by viewModel.dataHomeScreenState.collectAsState()
    DataScreen(screenState, viewModel.dataHomeActions, navController)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DataScreen(screenState: DataScreenState, actions: DataScreenActions? = null, navController: NavController? = null) {
    when (screenState) {
        is DataScreenState.SelectData -> {
            // Compose bug, after exiting this screen and coming back, the sheet can be hidden. There is a workaround: https://issuetracker.google.com/issues/292138966
            val scaffoldState = rememberBottomSheetScaffoldState(rememberStandardBottomSheetState(skipHiddenState = true))
            BottomSheetScaffold(
                scaffoldState = scaffoldState,
                sheetContainerColor = Color.Black,
                sheetPeekHeight = 200.dp,
                sheetContent = {
                    BottomPageContent(screenState = screenState, actions = actions, navController = navController)
                }) {


                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = Purple80)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(all = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = stringResource(id = R.string.add_data), style = MaterialTheme.typography.titleLarge, color = Color.Black)
                        Spacer(modifier = Modifier.weight(1f))
                        Button(
                            colors = ButtonDefaults.buttonColors().copy(containerColor = Color.White, contentColor = Purple40),
                            onClick = {
                                navController?.navigate(Screen.CountriesList.route)
                            }
                        ) {
                            Row {
                                Text(text = "${countryCodeToEmojiFlag(screenState.selectedCountry.countryCode)}  ${screenState.selectedCountry.countryName}", color = Color.Black)
                            }
                        }
                    }

                    Box(
                        modifier = Modifier
                            .size(240.dp)
                            .align(Alignment.CenterHorizontally)
                    ) {


                        CircularProgressBar(
                            modifier = Modifier
                                .fillMaxSize()
                                .align(Alignment.Center),
                            onChange = {
                                actions?.updatePercentage(it)
                            },
                            selection = screenState.selectedPercentage
                        )

                        Column(modifier = Modifier.align(Alignment.Center), horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = screenState.selectedValue.formatWithOneDecimal(), style = MaterialTheme.typography.displayLarge, fontWeight = FontWeight.Bold, color = Color.Black)
                            Text(text = "GB", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = Color.Black)
                        }
                    }

                    Text(
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(top = 8.dp, start = 16.dp, end = 16.dp),
                        text = stringResource(id = R.string.amplify_your_connectivity),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )

                    Row(
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(top = 16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(32.dp)
                    ) {

                        TimeText(time = stringResource(id = R.string.D_hrs, screenState.internetHours), activity = stringResource(id = R.string.internet))

                        TimeText(time = stringResource(id = R.string.D_hrs, screenState.musicHours), activity = stringResource(id = R.string.music))

                        TimeText(time = stringResource(id = R.string.D_hrs, screenState.videoHours), activity = stringResource(id = R.string.video))
                    }


                    Spacer(modifier = Modifier.weight(1f))
                    // Trick to prevent purple from appearing when flinging the bottom bar
                    Spacer(
                        modifier = Modifier
                            .height(80.dp)
                            .fillMaxWidth()
                            .background(Color.Black)
                    )

                }
            }
        }
    }
}

@Composable
private fun TimeText(time: String, activity: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        val style = MaterialTheme.typography.titleSmall.copy(lineHeight = 1.em)
        Text(text = time, style = style, fontWeight = FontWeight.Bold, color = Color.Black)
        Text(text = activity, style = style, fontWeight = FontWeight.Bold, color = Color.DarkGray)
    }
}

@Composable
private fun ColumnScope.BottomPageContent(screenState: DataScreenState.SelectData, actions: DataScreenActions?, navController: NavController?) {
    CompositionLocalProvider(LocalRippleTheme provides ColoredRipple) {
        Row(
            modifier = Modifier
                .animateContentSize()
                .fillMaxWidth()
                .padding(top = 16.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceEvenly
        ) {

            val selectedPosition = screenState.listOfShortCuts.findClosestIndexNotAbove(screenState.selectedValue)

            screenState.listOfShortCuts.forEachIndexed { index, shortcut ->
                val selected = index == selectedPosition
                val textColor by animateColorAsState(targetValue = if (selected) Color.Black else Color.Gray, animationSpec = spring(stiffness = Spring.StiffnessHigh))
                val bgColor by animateColorAsState(targetValue = if (selected) Purple80 else Color.Transparent, animationSpec = spring())

                Text(
                    modifier = Modifier
                        .clip(RoundedCornerShape(30.dp))
                        .background(color = bgColor)
                        .animateContentSize()
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = rememberRipple(),
                            enabled = !selected,
                            onClick = {
                                if (!selected) {
                                    actions?.updateValue(shortcut)
                                }
                            }
                        )
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    textAlign = TextAlign.Center,
                    text = "${(if (selected) screenState.selectedValue else shortcut).formatWithOneDecimal()} GB",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold,
                    color = textColor
                )

            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 32.dp, horizontal = 16.dp)
        ) {
            Text(
                text = stringResource(id = R.string.network), color = Color.Gray, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = screenState.network.name, color = Color.White, style = MaterialTheme.typography.bodyMedium
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Text(
                text = stringResource(id = R.string.plan_type), color = Color.Gray, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = screenState.planType.toTranslatedString(), color = Color.White, style = MaterialTheme.typography.bodyMedium
            )
        }

        Button(
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 32.dp, bottom = 16.dp),
            colors = ButtonDefaults.buttonColors().copy(containerColor = Color.White),
            shape = RoundedCornerShape(16.dp),
            onClick = { navController?.navigate(Screen.Success.route) }) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center
            ) {
                Text(text = "${stringResource(id = R.string.str_continue)} - ", color = Color.Black)
                Text(modifier = Modifier.animateContentSize(), text = screenState.price, fontWeight = FontWeight.Bold, color = Color.Black)
            }
        }
    }
}

@Composable
private fun PlanType.toTranslatedString(): String {
    return when (this) {
        PlanType.DataOnly -> stringResource(id = R.string.data_only)
    }
}

@Immutable
object ColoredRipple : RippleTheme {
    @Composable
    override fun defaultColor() = Purple80

    @Composable
    override fun rippleAlpha(): RippleAlpha = RippleAlpha(0.6f, 0.6f, 0.6f, 0.6f)
}

@Composable
@Preview
private fun Preview_DataScreen() {
    DataScreen(
        screenState = DataScreenState.SelectData(
            selectedCountry = Country("Sweden", "SE"),
            selectedValue = 5.2f,
            selectedPercentage = 0.1f,
            internetHours = 30,
            musicHours = 15,
            videoHours = 5,
            listOfShortCuts = listOf(5f, 10f, 15f, 20f),
            network = Network.Vodafone,
            planType = PlanType.DataOnly,
            price = "40 KR"
        )
    )
}