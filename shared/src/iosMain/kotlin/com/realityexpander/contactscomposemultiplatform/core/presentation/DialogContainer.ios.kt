package com.realityexpander.contactscomposemultiplatform.core.presentation

import androidx.compose.runtime.Composable
import androidx.compose.ui.window.Popup

@Composable
actual fun DialogContainer(content: @Composable () -> Unit) {
    Popup(content = content)
}
