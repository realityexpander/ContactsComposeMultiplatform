package com.realityexpander.contactscomposemultiplatform.contacts.data

import com.realityexpander.contactscomposemultiplatform.contacts.domain.Contact
import com.realityexpander.contactscomposemultiplatform.contacts.domain.ContactDataSource
import com.realityexpander.contactscomposemultiplatform.core.data.ImageStorage
import com.realityexpander.contactscomposemultiplatform.database.ContactDatabase
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.supervisorScope
import kotlinx.datetime.Clock

class SqlDelightContactDataSource(
    db: ContactDatabase,
    private val imageStorage: ImageStorage
) : ContactDataSource {

    private val queries = db.contactQueries

    override fun getContacts(): Flow<List<Contact>> {
        return queries
            .getContacts()
            .asFlow()
            .mapToList()
            .map { contactEntities ->

                // serially map each contact entity to a contact
                // contactEntities.map { contactEntity ->
                //    contactEntity.toContact(imageStorage)
                // }

                // parallelize the mapping of each contact entity to a contact
                supervisorScope {
                    contactEntities
                        .map { contactEntity ->
                            async { contactEntity.toContact(imageStorage) }
                        }
                        .map { deferredContact ->
                            deferredContact.await()
                        }
                }
            }
    }

    override fun getRecentContacts(amount: Int): Flow<List<Contact>> {
        return queries
            .getRecentContacts(amount.toLong())
            .asFlow()
            .mapToList()
            .map { contactEntities ->
                supervisorScope {
                    contactEntities
                        .map { contactEntity ->
                            async { contactEntity.toContact(imageStorage) }
                        }
                        .map { deferredContact ->
                            deferredContact.await()
                        }
                }
            }
    }

    override suspend fun insertContact(contact: Contact) {
        val imagePath = contact.photoBytes?.let {
            imageStorage.saveImage(it)
        }
        queries.insertContactEntity(
            id = contact.id,
            firstName = contact.firstName,
            lastName = contact.lastName,
            phoneNumber = contact.phoneNumber,
            email = contact.email,
            createdAt = Clock.System.now().toEpochMilliseconds(),
            imagePath = imagePath
        )
    }

    override suspend fun deleteContact(id: Long) {
        val entity = queries.getContactById(id).executeAsOne()
        entity.imagePath?.let {
            imageStorage.deleteImage(it)
        }
        queries.deleteContact(id)
    }
}
