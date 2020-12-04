package com.android.samples.donuttracker.firestore

import android.util.Log
import androidx.lifecycle.LiveData
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.ListenerRegistration

class FirestoreDocumentLiveData<T>(
        private val documentReference: DocumentReference,
        private val type: Class<T>
) : LiveData<T>() {

    companion object {
        private const val TAG = "FirestoreDocumentLiveData"
    }

    private var registration: ListenerRegistration? = null
    private val listener = EventListener<DocumentSnapshot> { snapshot, error ->

        if (error != null) {
            Log.d(TAG, "Listen failed.")
        }

        if(snapshot == null) return@EventListener

        value = snapshot.toObject(type)
    }

    override fun onActive() {
        super.onActive()
        // add listener
        registration = documentReference.addSnapshotListener(listener)
        Log.d(TAG, "Added the listener.")
    }

    override fun onInactive() {
        super.onInactive()
        // remove listener
        registration?.remove()
        Log.d(TAG, "Removed the listener.")
    }
}