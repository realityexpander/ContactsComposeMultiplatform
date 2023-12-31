package com.realityexpander.contactscomposemultiplatform.android

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import com.realityexpander.contactscomposemultiplatform.App
import com.realityexpander.contactscomposemultiplatform.core.presentation.ImagePickerFactory
import com.realityexpander.contactscomposemultiplatform.di.AppModule

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // assumes the request is accepted (must add more logic to handle the case where it's not)
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
            ),
            0
        )

        setContent {
            App(
                darkTheme = isSystemInDarkTheme(),
                dynamicColor = true,
                appModule = AppModule(LocalContext.current.applicationContext),
                imagePicker = ImagePickerFactory().createPicker(),
            )
        }
    }
}
