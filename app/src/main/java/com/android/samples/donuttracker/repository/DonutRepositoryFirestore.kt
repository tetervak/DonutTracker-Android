package com.android.samples.donuttracker.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.android.samples.donuttracker.domain.Donut
import com.android.samples.donuttracker.firestore.FirestoreCollectionLiveData
import com.android.samples.donuttracker.firestore.FirestoreRepository
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import java.util.*
import java.util.concurrent.ExecutionException
import javax.inject.Inject

class DonutRepositoryFirestore @Inject constructor() : DonutRepository {

    companion object {
        private const val TAG = "DonutRepositoryFirestore"
    }

    private val firestoreRepository = FirestoreRepository(DonutFirestore::class.java)

    private val firestore = Firebase.firestore

    private fun getUserId(): String {
        return Firebase.auth.currentUser?.uid ?: throw Exception("Could'n get user id")
    }

    private fun getUserName(): String {
        return Firebase.auth.currentUser?.displayName ?: throw Exception("Could'n get user name")
    }

    private fun donutCollection(): CollectionReference {

        return firestore.collection("users")
            .document(getUserId())
            .collection("donuts")
    }

//    override fun getAll(): LiveData<List<Donut>> {
//        Log.d(TAG, "getAll: called")
//        return Transformations.map(
//            FirestoreCollectionLiveData(
//                donutCollection().orderBy("date", Query.Direction.DESCENDING),
//                DonutFirestore::class.java
//            )
//        ) { list -> list.map { it.asDonut() } }
//    }

    @ExperimentalCoroutinesApi
    override fun getAllFlow(): Flow<List<Donut>> {
        return firestoreRepository.getAllFromQuery(
            donutCollection().orderBy("date", Query.Direction.DESCENDING)
        ).map { list -> list.map { it.asDonut() } }
    }

    override suspend fun get(id: String): Donut? {
        return donutCollection()
            .document(id)
            .get()
            .await()
            .toObject(DonutFirestore::class.java)
            ?.asDonut()
    }

    @Suppress("BlockingMethodInNonBlockingContext")
    override suspend fun insert(donut: Donut): String {
        Log.d(TAG, "insert(donut = $donut)")

        var user: UserFirestore? = null;
        val userId = getUserId()
        val userRef = firestore.collection("users").document(userId)
        try {
            val snapshot = Tasks.await(userRef.get())
            if (snapshot.exists()) {
                Log.d(TAG, "insert: the document exists.")
                user = snapshot.toObject<UserFirestore>()
            }
        }catch (e: ExecutionException) {
            // The Task failed, this is the same exception you'd get in a non-blocking
            // failure handler.
            Log.d(TAG, "insert: ExecutionException $e")
        } catch (e: InterruptedException) {
            // An interrupt occurred while waiting for the task to complete.
            Log.d(TAG, "insert: InterruptedException $e")
        }

        if (user == null) {
            Log.d(TAG, "insert: the user is not found")
            user = UserFirestore(getUserId(), getUserName(), 0)
        }
        Log.d(TAG, "insert: user.name = ${user.name}")
        user.count++

        val donutRef = userRef.collection("donuts").document()

        firestore.runTransaction { transaction ->
            transaction.set(userRef, user)
            transaction.set(donutRef,donut.asDonutFirestore())
        }.await()

        return donutRef.id
    }

    override suspend fun delete(donut: Donut) {
        if (donut.id != null) {
            val userRef = firestore.collection("users").document(getUserId())
            val user = userRef.get().await().toObject(UserFirestore::class.java)
            if(user != null){
                user.count -= 1
                val donutRef = userRef.collection("donuts").document(donut.id)
                firestore.runTransaction { transaction ->
                    transaction.set(userRef, user)
                    transaction.delete(donutRef)
                }.await()
            }
        }
    }

    override suspend fun update(donut: Donut) {
        if (donut.id != null) {
            donutCollection()
                .document(donut.id)
                .set(donut.asDonutFirestore())
                .await()
        }
    }

    data class UserFirestore(
        @DocumentId val id: String? = null,
        val name: String? = null,
        var count: Int = 0
    )

    data class DonutFirestore(
        @DocumentId val id: String? = null,
        val name: String? = null,
        val description: String? = null,
        val rating: Float = 3F,
        val lowFat: Boolean = false,
        @ServerTimestamp var date: Date? = null
    ){
        fun asDonut(): Donut {
            return Donut(id, name ?: "", description ?: "", rating, lowFat, date)
        }
    }
}

fun Donut.asDonutFirestore(): DonutRepositoryFirestore.DonutFirestore{
    return DonutRepositoryFirestore.DonutFirestore(id, name, description, rating, lowFat, date)
}
