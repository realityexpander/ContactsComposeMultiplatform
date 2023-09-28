package com.realityexpander.contactscomposemultiplatform.di

import com.realityexpander.contactscomposemultiplatform.contacts.domain.ContactDataSource

expect class AppModule {
    val contactDataSource: ContactDataSource
}
