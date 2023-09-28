package com.realityexpander.contactscomposemultiplatform.contacts.data

import com.realityexpander.contactscomposemultiplatform.contacts.domain.Contact
import com.realityexpander.contactscomposemultiplatform.core.data.ImageStorage
import database.ContactEntity

suspend fun ContactEntity.toContact(imageStorage: ImageStorage): Contact {
    return Contact(
        id = id,
        firstName = firstName,
        lastName = lastName,
        email = email,
        phoneNumber = phoneNumber,
        photoBytes = imagePath?.let { imageStorage.getImage(it) }
    )
}
