package com.feliperrm.instabridge.data.success

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.feliperrm.instabridge.R

@Composable
fun SuccessScreen(navController: NavController? = null) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {

        val preloaderLottieComposition by rememberLottieComposition(
            LottieCompositionSpec.RawRes(
                R.raw.success_animation
            )
        )

        val preloaderProgress by animateLottieCompositionAsState(
            preloaderLottieComposition,
            iterations = LottieConstants.IterateForever,
            isPlaying = true
        )


        LottieAnimation(
            modifier = Modifier.align(Alignment.Center),
            composition = preloaderLottieComposition,
            progress = { preloaderProgress })

        val context = LocalContext.current

        Button(
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp, top = 32.dp, bottom = 16.dp)
                .align(Alignment.BottomCenter),
            colors = ButtonDefaults.buttonColors().copy(containerColor = Color.White),
            shape = RoundedCornerShape(16.dp),
            onClick = { (context as Activity).finishAndRemoveTask() }) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center
            ) {
                Text(text = stringResource(id = R.string.finish), color = Color.Black)
            }
        }

    }
}


@Preview
@Composable
private fun Preview_SuccessScreen() {
    SuccessScreen()
}
