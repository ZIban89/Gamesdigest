package com.example.gamesdigest.di

import com.example.gamesdigest.data.remote.FIRESTORE_COLLECTION
import com.example.gamesdigest.data.repository.FirestoreRepositpryImpl
import com.example.gamesdigest.domain.repository.FirestoreRepository
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FirestoreModule {

    @Provides
    @Singleton
    fun provideFirestorCollectionReference() =
        FirebaseFirestore
            .getInstance()
            .collection(FIRESTORE_COLLECTION)

    @Provides
    @Singleton
    fun provideFirestoreRepository(collectionReference: CollectionReference): FirestoreRepository =
        FirestoreRepositpryImpl(collectionReference)
}
