package com.realityexpander.contactscomposemultiplatform.di

import android.content.Context
import com.realityexpander.contactscomposemultiplatform.contacts.data.SqlDelightContactDataSource
import com.realityexpander.contactscomposemultiplatform.contacts.domain.ContactDataSource
import com.realityexpander.contactscomposemultiplatform.core.data.DatabaseDriverFactory
import com.realityexpander.contactscomposemultiplatform.core.data.ImageStorage
import com.realityexpander.contactscomposemultiplatform.database.ContactDatabase

actual class AppModule(
    private val context: Context
) {

    actual val contactDataSource: ContactDataSource by lazy {
        SqlDelightContactDataSource(
            db = ContactDatabase(
                driver = DatabaseDriverFactory(context).create(),
            ),
            imageStorage = ImageStorage(context)
        )
    }
}
