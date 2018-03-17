package atanana.com.sireader.db

import android.arch.persistence.room.RoomDatabase

abstract class Database : RoomDatabase() {
    abstract fun questionsDao(): QuestionsDao
    abstract fun packsDao(): PacksDao
    abstract fun questionFilesDao(): QuestionFilesDao
}