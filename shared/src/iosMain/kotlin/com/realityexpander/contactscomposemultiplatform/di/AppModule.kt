package com.realityexpander.contactscomposemultiplatform.di

import com.realityexpander.contactscomposemultiplatform.contacts.data.SqlDelightContactDataSource
import com.realityexpander.contactscomposemultiplatform.contacts.domain.ContactDataSource
import com.realityexpander.contactscomposemultiplatform.core.data.DatabaseDriverFactory
import com.realityexpander.contactscomposemultiplatform.core.data.ImageStorage
import com.realityexpander.contactscomposemultiplatform.database.ContactDatabase

actual class AppModule {

    actual val contactDataSource: ContactDataSource by lazy {
        SqlDelightContactDataSource(
            db = ContactDatabase(
                driver = DatabaseDriverFactory().create()
            ),
            imageStorage = ImageStorage()
        )
    }
}
