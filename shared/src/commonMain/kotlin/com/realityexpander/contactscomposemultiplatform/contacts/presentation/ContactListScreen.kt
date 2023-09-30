package com.realityexpander.contactscomposemultiplatform.contacts.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.PersonAdd
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.realityexpander.contactscomposemultiplatform.contacts.domain.Contact
import com.realityexpander.contactscomposemultiplatform.contacts.presentation.components.AddContactSheet
import com.realityexpander.contactscomposemultiplatform.contacts.presentation.components.ContactDetailSheet
import com.realityexpander.contactscomposemultiplatform.contacts.presentation.components.ContactListItem
import com.realityexpander.contactscomposemultiplatform.contacts.presentation.components.RecentlyAddedContacts
import com.realityexpander.contactscomposemultiplatform.core.presentation.CameraPosition
import com.realityexpander.contactscomposemultiplatform.core.presentation.GoogleMaps
import com.realityexpander.contactscomposemultiplatform.core.presentation.ImagePicker
import com.realityexpander.contactscomposemultiplatform.core.presentation.LatLong
import com.realityexpander.contactscomposemultiplatform.core.presentation.MapMarker

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactListScreen(
    state: ContactListState,
    newContact: Contact?,
    onEvent: (ContactListEvent) -> Unit,
    imagePicker: ImagePicker
) {
    imagePicker.registerPicker { imageBytes ->
        onEvent(ContactListEvent.OnPhotoPicked(imageBytes))
    }
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    onEvent(ContactListEvent.OnAddNewContactClick)
                },
                shape = RoundedCornerShape(20.dp)
            ) {
                Icon(
                    imageVector = Icons.Rounded.PersonAdd,
                    contentDescription = "Add contact"
                )
            }
        }
    ) {

        Column(Modifier.fillMaxSize()) {
            LazyColumn(
                modifier = Modifier
                    .height(400.dp)
                    .weight(1f)
                    .fillMaxWidth(),
                contentPadding = PaddingValues(vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                userScrollEnabled = true
            ) {


                item {
                    RecentlyAddedContacts(
                        contacts = state.recentlyAddedContacts,
                        onClick = {
                            onEvent(ContactListEvent.SelectContact(it))
                        }
                    )
                }

                item {
                    Text(
                        text = "My contacts (${state.contacts.size})",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        fontWeight = FontWeight.Bold
                    )
                }

                items(state.contacts) { contact ->
                    ContactListItem(
                        contact = contact,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onEvent(ContactListEvent.SelectContact(contact))
                            }
                            .padding(horizontal = 16.dp)
                    )
                }

                item(key="GoogleMaps") {
                    GoogleMaps(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(400.dp)
                            .weight(.5f)
                            .padding(horizontal = 16.dp),
                        markers = state.contacts.map { _ -> //contact ->
                            MapMarker(
                                key = "Contact1", //contact.id,
                                position = LatLong(
                                    latitude = 18.98603, //0.0, //contact.latitude,
                                    longitude = -99.09914, //contact.longitude
                                ),
                                title = "Contact1", //contact.name,
                                alpha = 1.0f
                            )
                        },
                        cameraPosition = CameraPosition(
                            target = LatLong(
                                18.982579225106615,
                                -99.09380710785197
                                //                    latitude = 0.0, //state.contacts.firstOrNull()?.latitude ?: 0.0,
                                //                    longitude = 0.0 //state.contacts.firstOrNull()?.longitude ?: 0.0
                            ),
                            zoom = 15f
                        ),
                        //            cameraPositionLatLongBounds = CameraPositionLatLongBounds(
                        //                coordinates = state.contacts.map { _ -> //contact ->
                        //                    LatLong(
                        //                        latitude = 0.0, //contact.latitude,
                        //                        longitude = 0.0 //contact.longitude
                        //                    )
                        //                },
                        //                padding = 100
                        //            )

                    )
                }
            }
        }
    }

    ContactDetailSheet(
        isOpen = state.isSelectedContactSheetOpen,
        selectedContact = state.selectedContact,
        onEvent = onEvent,
    )
    AddContactSheet(
        state = state,
        newContact = newContact,
        isOpen = state.isAddContactSheetOpen,
        onEvent = { event ->
            if(event is ContactListEvent.OnAddPhotoClicked) {
                // Trap the event and call the image picker, which will call the registered picker
                // This keeps the image picker out of the viewmodel, and reduces ping-ponging back to this screen
                imagePicker.pickImage()
            }
            onEvent(event)
        }
    )
}
