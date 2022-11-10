package uz.jbnuu.tsc.parents.di

import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import uz.jbnuu.tsc.parents.BuildConfig
import uz.jbnuu.tsc.parents.app.App
import uz.jbnuu.tsc.parents.data.network.ApiService
import uz.jbnuu.tsc.parents.utils.Constants.Companion.BASE_URL
import uz.jbnuu.tsc.parents.utils.Constants.Companion.BASE_URL_HEMIS
import uz.jbnuu.tsc.parents.utils.Prefs
import uz.jbnuu.tsc.parents.utils.lg
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideSharedPref() = Prefs(App.context)

    @Singleton
    @Provides
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor()
    }

    @Singleton
    @Provides
    @Named("provideHttpClient")
    fun provideHttpClient(
        prefs: Prefs
    ): OkHttpClient {
        val builder = OkHttpClient().newBuilder()
            .addInterceptor { chain ->
                lg("token->" + prefs.get(prefs.token, "..."))
                val request = chain.request().newBuilder().addHeader("Authorization", prefs.get(prefs.token, "...")).build()
                chain.proceed(request)
            }
            .connectTimeout(10000L, TimeUnit.MILLISECONDS)
            .readTimeout(10000L, TimeUnit.MILLISECONDS)
            .writeTimeout(10000L, TimeUnit.MILLISECONDS)

        if (BuildConfig.isDebug) {
            builder.addInterceptor(ChuckerInterceptor.Builder(App.context).collector(ChuckerCollector(App.context)).build())
        }

        return builder.build()
    }

    @Singleton
    @Provides
    @Named("provideHttpClientHemis")
    fun provideHttpClientHemis(
        prefs: Prefs
    ): OkHttpClient {
        val builder = OkHttpClient().newBuilder()
            .addInterceptor { chain ->
                val request = chain.request().newBuilder().addHeader("Authorization", "Bearer " + prefs.get(prefs.hemisToken, "hemis")).build()
                chain.proceed(request)
            }
            .connectTimeout(10000L, TimeUnit.MILLISECONDS)
            .readTimeout(10000L, TimeUnit.MILLISECONDS)
            .writeTimeout(10000L, TimeUnit.MILLISECONDS)

        if (BuildConfig.isDebug) {
            builder.addInterceptor(ChuckerInterceptor.Builder(App.context).collector(ChuckerCollector(App.context)).build())
        }

        return builder.build()
    }

    @Singleton
    @Provides
    @Named("provideRetrofit")
    fun provideRetrofit(@Named("provideHttpClient") okHttpClient: OkHttpClient): Retrofit {

        return Retrofit.Builder()
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .baseUrl(BASE_URL)
            .build()
    }

    @Singleton
    @Provides
    @Named("provideRetrofitHemis")
    fun provideRetrofitHemis(@Named("provideHttpClientHemis") okHttpClient: OkHttpClient): Retrofit {

        return Retrofit.Builder()
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .baseUrl(BASE_URL_HEMIS)
            .build()
    }

    @Singleton
    @Provides
    @Named("provideApiService")
    fun provideApiService(@Named("provideRetrofit") retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }

    @Singleton
    @Provides
    @Named("provideApiServiceHemis")
    fun provideApiServiceHemis(@Named("provideRetrofitHemis") retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }
}