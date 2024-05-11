package com.feliperrm.instabridge.data

import CircularProgressBar
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateValueAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.material.ripple.RippleAlpha
import androidx.compose.material.ripple.RippleTheme
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.feliperrm.instabridge.R
import com.feliperrm.instabridge.findClosestIndexNotAbove
import com.feliperrm.instabridge.formatWithOneDecimal
import com.feliperrm.instabridge.ui.theme.Purple40
import com.feliperrm.instabridge.ui.theme.Purple80

@Composable
fun DataScreen(navController: NavController) {
    val viewModel: DataScreenViewModel = viewModel()
    val screenState by viewModel.screenState.collectAsState()
    DataScreen(screenState, viewModel.actions)
}

@Composable
private fun DataScreen(screenState: DataScreenState, actions: DataScreenActions? = null) {
    when (screenState) {
        is DataScreenState.SelectData -> {
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
                    Text(text = stringResource(id = R.string.add_data), style = MaterialTheme.typography.titleLarge)
                    Spacer(modifier = Modifier.weight(1f))
                    Button(
                        colors = ButtonDefaults.buttonColors().copy(containerColor = Color.White, contentColor = Purple40),
                        onClick = { /*TODO*/ }
                    ) {
                        Row {
                            Text(text = screenState.selectedCountry, color = Color.Black)
                        }
                    }
                }

                Box(
                    modifier = Modifier
                        .padding(16.dp)
                        .size(240.dp)
                        .align(Alignment.CenterHorizontally)
                ) {

                    
                    CircularProgressBar(
                        modifier = Modifier
                            .fillMaxSize()
                            .align(Alignment.Center),
                        onChange = {
                            val valueSelected = screenState.selectedValue.formatWithOneDecimal().toFloat()
                            actions?.updatePercentage(it)

                        },
                        selection = screenState.selectedPercentage
                    )

                    Column(modifier = Modifier.align(Alignment.Center), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = screenState.selectedValue.formatWithOneDecimal(), style = MaterialTheme.typography.displayLarge, fontWeight = FontWeight.Bold)
                        Text(text = "GB", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)

                    }
                }

                Text(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(horizontal = 16.dp),
                    text = stringResource(id = R.string.amplify_your_connectivity),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold
                )

                Row(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 32.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(32.dp)
                ) {

                    TimeText(time = stringResource(id = R.string.D_hrs, screenState.internetHours), activity = stringResource(id = R.string.internet))

                    TimeText(time = stringResource(id = R.string.D_hrs, screenState.musicHours), activity = stringResource(id = R.string.music))

                    TimeText(time = stringResource(id = R.string.D_hrs, screenState.videoHours), activity = stringResource(id = R.string.video))
                }

                BottomPage(screenState, actions)

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
private fun BottomPage(screenState: DataScreenState.SelectData, actions: DataScreenActions?) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(top = 32.dp)
            .background(shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp), color = Color.Black)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 32.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceEvenly
        ) {

            val selectedPosition = screenState.listOfShortCuts.findClosestIndexNotAbove(screenState.selectedValue)

            screenState.listOfShortCuts.forEachIndexed { index, shortcut ->
                val selected = index == selectedPosition
                CompositionLocalProvider(LocalRippleTheme provides ColoredRipple) {
                    Text(
                        modifier = Modifier
                            .clip(RoundedCornerShape(30.dp))
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = rememberRipple(),
                                enabled = !selected,
                                onClick = {
                                    if (!selected) {
                                        actions?.selectShortcut(shortcut)
                                    }
                                }
                            )
                            .background(color = if (selected) Purple80 else Color.Transparent)
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        textAlign = TextAlign.Center,
                        text = "${(if (selected) screenState.selectedValue else shortcut).formatWithOneDecimal()} GB",
                        color = Color.Gray
                    )
                }
            }
        }
    }
}

@Immutable
object ColoredRipple : RippleTheme {
    @Composable
    override fun defaultColor() = Purple80

    @Composable
    override fun rippleAlpha(): RippleAlpha = RippleAlpha(1f, 1f, 1f, 1f)
}

@Composable
@Preview
private fun Preview_DataScreen() {
    DataScreen(
        screenState = DataScreenState.SelectData(
            selectedCountry = "Sweden",
            selectedValue = 5.2f,
            selectedPercentage = 0.1f,
            internetHours = 30,
            musicHours = 15,
            videoHours = 5,
            listOfShortCuts = listOf(5f, 10f, 15f, 20f),
            network = Network.Vodafone,
            planType = PlanType.DataOnly
        )
    )
}