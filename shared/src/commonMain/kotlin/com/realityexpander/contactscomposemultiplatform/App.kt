package com.realityexpander.contactscomposemultiplatform

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.realityexpander.contactscomposemultiplatform.contacts.presentation.ContactListScreen
import com.realityexpander.contactscomposemultiplatform.contacts.presentation.ContactListViewModel
import com.realityexpander.contactscomposemultiplatform.core.presentation.ContactsTheme
import com.realityexpander.contactscomposemultiplatform.core.presentation.ImagePicker
import com.realityexpander.contactscomposemultiplatform.di.AppModule
import dev.icerock.moko.mvvm.compose.getViewModel
import dev.icerock.moko.mvvm.compose.viewModelFactory

@Composable
fun App(
    darkTheme: Boolean,
    dynamicColor: Boolean,
    appModule: AppModule,
    imagePicker: ImagePicker
) {
    ContactsTheme(
        darkTheme = darkTheme,
        dynamicColor = dynamicColor
    ) {
        val viewModel = getViewModel(
            key = "contact-list-screen",
            factory = viewModelFactory {
                ContactListViewModel(appModule.contactDataSource)
            }
        )
        val state by viewModel.state.collectAsState()

        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            ContactListScreen(
                state = state,
                newContact = viewModel.newContact,
                onEvent = viewModel::onEvent,
                imagePicker = imagePicker
            )
        }
    }
}
