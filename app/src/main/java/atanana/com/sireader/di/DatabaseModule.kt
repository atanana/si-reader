package atanana.com.sireader.di

import android.content.Context
import androidx.room.Room
import atanana.com.sireader.database.Database
import atanana.com.sireader.database.PacksDao
import atanana.com.sireader.database.QuestionFilesDao
import atanana.com.sireader.database.QuestionsDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): Database {
        return Room.databaseBuilder(context, Database::class.java, "si_reader_db").build()
    }

    @Provides
    @Singleton
    fun provideQuestionsDao(database: Database): QuestionsDao = database.questionsDao()

    @Provides
    @Singleton
    fun providePacksDao(database: Database): PacksDao = database.packsDao()

    @Provides
    @Singleton
    fun provideQuestionFilesDao(database: Database): QuestionFilesDao = database.questionFilesDao()
}