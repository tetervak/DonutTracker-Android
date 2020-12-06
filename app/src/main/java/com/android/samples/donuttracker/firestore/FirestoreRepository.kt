package com.android.samples.donuttracker.firestore

import android.util.Log
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.Query
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class FirestoreRepository<T>(
    private val type: Class<T>
) {

    companion object{
        private const val TAG = "FirestoreRepository"
    }

    @ExperimentalCoroutinesApi
    fun getDocument(
        documentRef: DocumentReference
    ): Flow<T> = callbackFlow {

        // Registers callback to firestore, which will be called on new events
        val subscription = documentRef.addSnapshotListener { snapshot, _ ->
            Log.d(TAG, "getFlow: added subscription")
            if (snapshot == null) { return@addSnapshotListener }
            // Sends docs to the flow! Consumers will get the new/updated docs
            try {
                val doc: T? = snapshot.toObject(type)
                if(doc != null){
                    offer(doc)
                }
            } catch (e: Throwable) {
                Log.d(TAG, "getFlow: error in getting a doc: ${e.message}")
            }
        }

        // The callback inside awaitClose will be executed when the flow is
        // either closed or cancelled.
        // In this case, remove the callback from Firestore
        awaitClose {
            subscription.remove()
            Log.d(TAG, "getFlow: removed subscription")
        }
    }

    @ExperimentalCoroutinesApi
    fun getAllFromCollection(
        collectionRef: CollectionReference
    ): Flow<List<T>> = getAllFromQuery(collectionRef)

    @ExperimentalCoroutinesApi
    fun getAllFromQuery(
        query: Query
    ): Flow<List<T>> = callbackFlow {

        // Registers callback to firestore, which will be called on new events
        val subscription = query.addSnapshotListener { snapshot, _ ->
            Log.d(TAG, "getFlow: added subscription")
            if (snapshot == null) {
                return@addSnapshotListener
            }
            // Sends lists to the flow! Consumers will get the new/updated lists
            try {
                val list = ArrayList<T>()
                snapshot.forEach { doc -> list.add(doc.toObject(type)) }
                offer(list)
            } catch (e: Throwable) {
                Log.d(TAG, "getAllFlow: error in getting data: ${e.message}")
            }
        }

        // The callback inside awaitClose will be executed when the flow is
        // either closed or cancelled.
        // In this case, remove the callback from Firestore
        awaitClose {
            subscription.remove()
            Log.d(TAG, "getFlow: removed subscription")
        }
    }

}