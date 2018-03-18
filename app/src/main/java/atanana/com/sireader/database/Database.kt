package atanana.com.sireader.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase

@Database(entities = [
    QuestionEntity::class,
    PackEntity::class,
    QuestionFileEntity::class
], version = 1)
abstract class Database : RoomDatabase() {
    abstract fun questionsDao(): QuestionsDao
    abstract fun packsDao(): PacksDao
    abstract fun questionFilesDao(): QuestionFilesDao
}