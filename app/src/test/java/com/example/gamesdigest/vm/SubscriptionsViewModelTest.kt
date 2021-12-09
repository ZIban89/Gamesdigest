package com.example.gamesdigest.vm

import com.example.gamesdigest.domain.model.Subscription
import com.example.gamesdigest.domain.model.UiState
import com.example.gamesdigest.domain.usecase.DeleteSubscriptionByDocIdUsecase
import com.example.gamesdigest.domain.usecase.GetSubscriptionsByEmailUsecase
import com.example.gamesdigest.fakeRepository.FakeFirestoreRepository
import com.example.gamesdigest.presentation.subscriptions.SubscriptionsViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestCoroutineScope
import org.junit.Before
import org.junit.Test

class SubscriptionsViewModelTest {

    lateinit var subscriptionsViewModel: SubscriptionsViewModel

    private val fakeFirestoreRepo = FakeFirestoreRepository()
    private val getSubscriptionsByEmailUsecase = GetSubscriptionsByEmailUsecase(fakeFirestoreRepo)
    private val deleteSubscriptionByDocIdUsecase = DeleteSubscriptionByDocIdUsecase(fakeFirestoreRepo)

    @ExperimentalCoroutinesApi
    private val scope = TestCoroutineScope()


    @ExperimentalCoroutinesApi
    @Before
    fun before() {
        subscriptionsViewModel = SubscriptionsViewModel(
            getSubscriptionsByEmailUsecase,
            deleteSubscriptionByDocIdUsecase,
            scope
        )

        fakeFirestoreRepo.saveSubscription("test@mail.ky", 1, "The Witcher", 1.0f)
        fakeFirestoreRepo.saveSubscription("test@mail.ky", 2, "The Witcher 2", 1.0f)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun getSubscriptionsByEmail(){
        val list = mutableListOf<UiState<List<Subscription>>>()
        scope.launch {
            subscriptionsViewModel.subscriptionsState.collectLatest {
                list.add(it)
            }
        }
        subscriptionsViewModel.getSubscriptionsByEmail("test@mail.ky")
        scope.launch { delay(500)}
        assert(list.size == 2)
        assert(list[0].isLoading)
        assert(list[1].data?.size == 2)
    }

    @Test
    fun deleteSubscriptionById(){
        val subscriptionCount = getSubscriptionsCount()
        subscriptionsViewModel.deleteSubscriptionByDocId(fakeFirestoreRepo.id.toString())
        val newSubscriptionsCount = getSubscriptionsCount()

        assert(subscriptionCount == newSubscriptionsCount + 1)
    }

    private fun getSubscriptionsCount() = fakeFirestoreRepo.subscriptions.flatMap { entry ->
        entry.value
    }.size

}