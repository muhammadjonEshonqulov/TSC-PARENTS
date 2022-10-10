package uz.jbnuu.tsc.parents.di

import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import uz.jbnuu.tsc.parents.app.App
import uz.jbnuu.tsc.parents.data.database.MyDatabase
import uz.jbnuu.tsc.parents.utils.Constants.Companion.DATABASE_NAME
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase() = Room.databaseBuilder(App.context, MyDatabase::class.java, DATABASE_NAME).build()

    @Singleton
    @Provides
    fun provideDatabaseDao(myDao: MyDatabase) = myDao.dao()
}