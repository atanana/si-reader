package atanana.com.sireader.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface QuestionsDao {
    @Query("select * from questions where packId = :packId")
    fun questionsForPack(packId: Int): Flow<List<QuestionEntity>>

    @Insert
    fun insertQuestion(question: QuestionEntity)
}

@Dao
interface PacksDao {
    @Query("select * from packs where fileId = :fileId")
    fun packForFile(fileId: Int): Flow<List<PackEntity>>

    @Query("select * from packs where id = :packId")
    fun pack(packId: Int): Flow<PackEntity>

    @Insert
    fun insertPack(pack: PackEntity): Long
}

@Dao
interface QuestionFilesDao {
    @Query("select * from files")
    fun all(): Flow<List<QuestionFileEntity>>

    @Query("select * from files where id = :fileId")
    fun file(fileId: Int): Flow<QuestionFileEntity>

    @Insert
    fun insertFile(file: QuestionFileEntity): Long

    @Query("delete from files where id in (:fileIds)")
    fun deleteFilesByIds(fileIds: IntArray)

    @Query("update files set lastReadPackId = :packId where id = :fileId")
    fun updateLastReadPack(fileId: Int, packId: Int)
}