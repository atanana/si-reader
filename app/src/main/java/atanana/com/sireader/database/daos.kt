package atanana.com.sireader.database

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import io.reactivex.Flowable

@Dao
interface QuestionsDao {
    @Query("select * from questions where packId = :packId")
    fun questionsForPack(packId: Int): Flowable<List<QuestionEntity>>

    @Insert
    fun insertQuestion(question: QuestionEntity)
}

@Dao
interface PacksDao {
    @Query("select * from packs where fileId = :fileId")
    fun packForFile(fileId: Int): Flowable<List<PackEntity>>

    @Query("select * from packs where id = :packId")
    fun pack(packId: Int): Flowable<PackEntity>

    @Insert
    fun insertPack(pack: PackEntity): Long
}

@Dao
interface QuestionFilesDao {
    @Query("select * from files")
    fun all(): Flowable<List<QuestionFileEntity>>

    @Query("select * from files where id = :fileId")
    fun file(fileId: Int): Flowable<QuestionFileEntity>

    @Insert
    fun insertFile(file: QuestionFileEntity): Long

    @Query("delete from files where id in (:fileIds)")
    fun deleteFilesByIds(fileIds: IntArray)
}