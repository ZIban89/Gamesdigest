package com.example.gamesdigest

import com.example.gamesdigest.domain.usecase.DeleteSubscriptionByDocIdUsecase
import com.example.gamesdigest.domain.usecase.GetSubscriptionsByEmailUsecase
import com.example.gamesdigest.presentation.subscriptions.SubscriptionsViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineScope
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.never
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class SubscriptionsViewModelTest {

    lateinit var subscriptionsViewModel: SubscriptionsViewModel

    @Mock
    lateinit var getSubscriptionsByEmailUsecase: GetSubscriptionsByEmailUsecase

    @ExperimentalCoroutinesApi
    val scope = TestCoroutineScope()

    @Mock
    lateinit var deleteSubscriptionByDocIdUsecase: DeleteSubscriptionByDocIdUsecase

    @ExperimentalCoroutinesApi
    @Before
    fun before() {
        subscriptionsViewModel = SubscriptionsViewModel(
            getSubscriptionsByEmailUsecase,
            deleteSubscriptionByDocIdUsecase,
            scope
        )
    }

    @Test
    fun getSubscriptionsByEmail() {
        val validEmail = "aa@aa.aa"
        val invalidEmail = "aaaaaa"
        subscriptionsViewModel.getSubscriptionsByEmail(validEmail)
        verify(getSubscriptionsByEmailUsecase).invoke(validEmail)
        verify(getSubscriptionsByEmailUsecase, never()).invoke(invalidEmail)
    }
}
