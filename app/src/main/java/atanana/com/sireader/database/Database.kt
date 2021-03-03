package atanana.com.sireader.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [
        QuestionEntity::class,
        PackEntity::class,
        QuestionFileEntity::class
    ], version = 1
)
abstract class Database : RoomDatabase() {
    abstract fun questionsDao(): QuestionsDao
    abstract fun packsDao(): PacksDao
    abstract fun questionFilesDao(): QuestionFilesDao
}