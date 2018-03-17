package atanana.com.sireader.di

import android.arch.persistence.room.Room
import android.content.Context
import atanana.com.sireader.db.Database
import atanana.com.sireader.db.PacksDao
import atanana.com.sireader.db.QuestionFilesDao
import atanana.com.sireader.db.QuestionsDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DbModule {
    @Provides
    @Singleton
    fun provideDatabase(context: Context): Database {
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