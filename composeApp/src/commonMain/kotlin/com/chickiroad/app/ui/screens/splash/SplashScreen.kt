package com.chickiroad.app.ui.screens.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import chickieroad.composeapp.generated.resources.Res
import chickieroad.composeapp.generated.resources.background_splash
import chickieroad.composeapp.generated.resources.chicken
import chickieroad.composeapp.generated.resources.loading_text
import chickieroad.composeapp.generated.resources.logo
import com.chickiroad.app.ui.shared.Background
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun SplashScreen(onTimeout: () -> Unit) {

    LaunchedEffect(Unit) {
        delay(2000)
        onTimeout()
    }

    Background(backgroundResImage = Res.drawable.background_splash)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.systemBars)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Image(
            painter = painterResource(Res.drawable.logo),
            contentDescription = "Logo",
            modifier = Modifier
                .fillMaxWidth()
                .height(192.dp)
        )

        Spacer(modifier = Modifier.height(64.dp))

        Image(
            painter = painterResource(Res.drawable.chicken),
            contentDescription = "Chicken",
            modifier = Modifier
                .width(246.dp)
                .height(369.dp)
        )

        Image(
            painter = painterResource(Res.drawable.loading_text),
            contentDescription = "Loading",
            modifier = Modifier
                .width(186.dp)
                .height(64.dp)
        )
    }
}

@Preview
@Composable
private fun SplashScreenPreview() {
    SplashScreen {}
}