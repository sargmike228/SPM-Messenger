package com.sargmike228.feature.contacts

import android.content.ContentResolver
import android.provider.ContactsContract
import android.content.Context
import javax.inject.Inject
import javax.inject.Singleton

data class SystemContact(
    val id: String,
    val name: String,
    val phoneNumber: String
)

@Singleton
class SystemContactsManager @Inject constructor(
    private val context: Context
) {
    
    fun syncSystemContacts(): List<SystemContact> {
        val contacts = mutableListOf<SystemContact>()
        val contentResolver: ContentResolver = context.contentResolver
        
        val cursor = contentResolver.query(
            ContactsContract.Contacts.CONTENT_URI,
            null,
            null,
            null,
            ContactsContract.Contacts.DISPLAY_NAME + " ASC"
        )
        
        cursor?.use {
            while (it.moveToNext()) {
                val id = it.getString(it.getColumnIndexOrThrow(ContactsContract.Contacts._ID))
                val name = it.getString(it.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME))
                val hasPhoneNumber = it.getInt(it.getColumnIndexOrThrow(ContactsContract.Contacts.HAS_PHONE_NUMBER))
                
                if (hasPhoneNumber > 0) {
                    val phoneCursor = contentResolver.query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                        arrayOf(id),
                        null
                    )
                    
                    phoneCursor?.use { pc ->
                        while (pc.moveToNext()) {
                            val phoneNumber = pc.getString(
                                pc.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER)
                            )
                            contacts.add(
                                SystemContact(
                                    id = id,
                                    name = name,
                                    phoneNumber = phoneNumber
                                )
                            )
                        }
                    }
                }
            }
        }
        
        return contacts
    }
    
    fun getContactDetails(contactId: String): SystemContact? {
        val contentResolver: ContentResolver = context.contentResolver
        
        val cursor = contentResolver.query(
            ContactsContract.Contacts.CONTENT_URI,
            null,
            ContactsContract.Contacts._ID + " = ?",
            arrayOf(contactId),
            null
        )
        
        return cursor?.use {
            if (it.moveToFirst()) {
                val name = it.getString(it.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME))
                
                val phoneCursor = contentResolver.query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                    arrayOf(contactId),
                    null
                )
                
                val phoneNumber = phoneCursor?.use { pc ->
                    if (pc.moveToFirst()) {
                        pc.getString(pc.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER))
                    } else null
                } ?: ""
                
                SystemContact(
                    id = contactId,
                    name = name,
                    phoneNumber = phoneNumber
                )
            } else null
        }
    }
}