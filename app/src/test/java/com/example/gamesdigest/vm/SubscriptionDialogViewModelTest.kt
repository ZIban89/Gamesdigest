package com.example.gamesdigest.vm

import com.example.gamesdigest.domain.repository.CheapSharkRepository
import com.example.gamesdigest.domain.usecase.AlertWhenPriceLessUseCase
import com.example.gamesdigest.domain.usecase.SaveSubscriptionIntoFirestoreUsecase
import com.example.gamesdigest.fakeRepository.FakeFirestoreRepository
import com.example.gamesdigest.presentation.subscription.SubscriptionDialogViewModel
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineScope
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.never
import org.mockito.kotlin.verify

@RunWith(MockitoJUnitRunner::class)
class SubscriptionDialogViewModelTest {

    private val scope = TestCoroutineScope()
    private val fakeFirestoreRepo = FakeFirestoreRepository()
    private var fakeCheapSharkRepository: CheapSharkRepository =
        Mockito.mock(CheapSharkRepository::class.java)

    private val alertWhenPriceLessUseCase = AlertWhenPriceLessUseCase(fakeCheapSharkRepository)
    private val saveSubscriptionIntoFirestoreUsecase =
        SaveSubscriptionIntoFirestoreUsecase(fakeFirestoreRepo)

    lateinit var subscriptionDialogViewModel: SubscriptionDialogViewModel

    @Before
    fun setUp() {
        subscriptionDialogViewModel = SubscriptionDialogViewModel(
            alertWhenPriceLessUseCase,
            saveSubscriptionIntoFirestoreUsecase,
            scope
        )
    }

    @Test
    fun successSubscribeTest() {
        val validEmail = "em@ai.ll"
        val gameId = 1
        val gameName = "The Witcher 3"
        val validPrice = 1.0f
        val startSubscriptionsCount = getSubscriptionsCount()

        subscriptionDialogViewModel.subscribe(validEmail, gameId, gameName, validPrice.toString())
        val newSubscriptionsCount = getSubscriptionsCount()

        assert(startSubscriptionsCount + 1 == newSubscriptionsCount)
        runBlocking {
            verify(fakeCheapSharkRepository).alertWhenPriceLess(validEmail, gameId, validPrice)
        }
    }

    @Test
    fun failedSubscribeTest() {
        val inValidEmail = "email"
        val gameId = 1
        val gameName = "The Witcher 3"
        val validPrice = 1.0f
        val startSubscriptionsCount = getSubscriptionsCount()

        subscriptionDialogViewModel.subscribe(inValidEmail, gameId, gameName, validPrice.toString())
        val newSubscriptionsCount = getSubscriptionsCount()

        assert(startSubscriptionsCount == newSubscriptionsCount)
        runBlocking {
            verify(fakeCheapSharkRepository, never()).alertWhenPriceLess(
                inValidEmail,
                gameId,
                validPrice
            )
        }
    }

    private fun getSubscriptionsCount() = fakeFirestoreRepo.subscriptions.flatMap { entry ->
        entry.value
    }.size


}