package com.example.gamesdigest.di

import com.example.gamesdigest.data.remote.BASE_CHEAPSHARK_URL
import com.example.gamesdigest.data.remote.BASE_RAWG_URL
import com.example.gamesdigest.data.remote.CALL_TIMEOUT_SECONDS
import com.example.gamesdigest.data.remote.LOCALHOST_URL
import com.example.gamesdigest.data.remote.api.CheapSharkApi
import com.example.gamesdigest.data.remote.api.LocalApi
import com.example.gamesdigest.data.remote.api.RawgApi
import com.example.gamesdigest.data.repository.CheapSharkRepositoryImpl
import com.example.gamesdigest.data.repository.RawgRepositoryImpl
import com.example.gamesdigest.domain.repository.CheapSharkRepository
import com.example.gamesdigest.domain.repository.RawgRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideOkHttpClient() = OkHttpClient.Builder()
        .callTimeout(CALL_TIMEOUT_SECONDS, TimeUnit.SECONDS)
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .build()

    @Provides
    @Singleton
    fun provideRawgApi(client: OkHttpClient): RawgApi = Retrofit.Builder()
        .baseUrl(BASE_RAWG_URL)
        .addConverterFactory(MoshiConverterFactory.create())
        .client(client)
        .build()
        .create(RawgApi::class.java)

    @Provides
    @Singleton
    fun provideLocalApi(client: OkHttpClient): LocalApi = Retrofit.Builder()
        .baseUrl(LOCALHOST_URL)
        .addConverterFactory(MoshiConverterFactory.create())
        .client(client)
        .build()
        .create(LocalApi::class.java)

    @Provides
    @Singleton
    fun provideCheapSharkApi(client: OkHttpClient) = Retrofit.Builder()
        .baseUrl(BASE_CHEAPSHARK_URL)
        .addConverterFactory(MoshiConverterFactory.create())
        .client(client)
        .build()
        .create(CheapSharkApi::class.java) as CheapSharkApi

    @Provides
    @Singleton
    fun provideCheapSharkRepository(api: CheapSharkApi): CheapSharkRepository =
        CheapSharkRepositoryImpl(api)

    @Provides
    @Singleton
    fun provideRawgRepository(api: RawgApi): RawgRepository = RawgRepositoryImpl(api)

    /*@Provides
    @Singleton
    fun provideLocalRawgRepository(api: LocalApi) : RawgRepository = LocalRawgRepository(api)*/

    @Provides
    @Singleton
    fun provideViewModelScope(): CoroutineScope? = null
}
