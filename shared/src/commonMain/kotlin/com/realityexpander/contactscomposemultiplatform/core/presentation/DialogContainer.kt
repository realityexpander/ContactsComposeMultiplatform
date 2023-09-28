package com.realityexpander.contactscomposemultiplatform.core.presentation

import androidx.compose.runtime.Composable

@Composable
expect fun DialogContainer(content: @Composable () -> Unit)
