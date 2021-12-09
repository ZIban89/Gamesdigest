package com.example.gamesdigest

import com.example.gamesdigest.data.repository.FirestoreRepositpryImpl
import com.example.gamesdigest.di.FirestoreModule
import com.example.gamesdigest.domain.repository.FirestoreRepository
import com.example.gamesdigest.domain.usecase.DeleteSubscriptionByDocIdUsecase
import com.example.gamesdigest.domain.usecase.GetSubscriptionsByEmailUsecase
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [FirestoreModule::class]
)
object TestModule {
    const val TEST_FIRESTORE_COLLECTION = "testsubscriptions"

    @Provides
    @Singleton
    fun provideFirestorCollectionReference() =
        FirebaseFirestore
            .getInstance()
            .collection(TEST_FIRESTORE_COLLECTION)

    @Provides
    @Singleton
    fun provideFirestoreRepository(collectionReference: CollectionReference): FirestoreRepository =
        FirestoreRepositpryImpl(collectionReference)

    @Provides
    @Singleton
    fun provideGetSubscriptionsByEmailUsecase(firestoreRepository: FirestoreRepository) =
        GetSubscriptionsByEmailUsecase(firestoreRepository)

    @Provides
    @Singleton
    fun provideDeleteSubscriptionByDocIdUsecase(firestoreRepository: FirestoreRepository) =
        DeleteSubscriptionByDocIdUsecase(firestoreRepository)
}
