package atanana.com.sireader.usecases

import android.content.ContentResolver
import android.content.ContentResolver.SCHEME_CONTENT
import android.content.ContentResolver.SCHEME_FILE
import android.net.Uri
import android.provider.OpenableColumns
import android.webkit.MimeTypeMap
import atanana.com.sireader.CannotSaveInDatabaseException
import atanana.com.sireader.ParseFileException
import atanana.com.sireader.database.*
import com.atanana.si_parser.*
import io.reactivex.Completable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ParseFileUseCase @Inject constructor(
        private val questionsDao: QuestionsDao,
        private val packsDao: PacksDao,
        private val filesDao: QuestionFilesDao,
        private val database: Database,
        private val contentResolver: ContentResolver
) {
    fun process(uri: Uri): Completable = Completable.fromCallable {
        val (siInfo, questionPacks) = parseFile(uri)

        database.save {
            val name = fileName(uri) ?: "Unknown"
            val fileId = saveFile(siInfo, name)
            questionPacks.withIndex()
                    .forEach { (index, pack) ->
                        savePackWithQuestions(index, pack, fileId)
                    }
        }
    }
            .subscribeOn(Schedulers.io())

    private fun savePackWithQuestions(index: Int, pack: QuestionPack, fileId: Long) {
        val packId = savePack(index, pack, fileId)
        pack.questions.forEach { question ->
            saveQuestion(question, packId.toInt())
        }
    }

    private fun savePack(index: Int, pack: QuestionPack, fileId: Long): Long =
            packsDao.insertPack(
                    PackEntity(0, pack.topic, pack.author, pack.notes, index + 1, fileId.toInt())
            )

    private fun saveFile(siInfo: SiInfo, name: String): Long =
            filesDao.insertFile(
                    QuestionFileEntity(0, siInfo.title, name, siInfo.notes, siInfo.editor)
            )

    private fun saveQuestion(question: QuestionData, packId: Int) =
            questionsDao.insertQuestion(
                    QuestionEntity(
                            0,
                            question.question,
                            question.answer,
                            question.alsoAnswer,
                            question.notAnswer,
                            question.comment,
                            question.reference,
                            packId
                    )
            )

    private fun Database.save(block: () -> Unit) {
        beginTransaction()
        try {
            block()
            setTransactionSuccessful()
        } catch (e: Exception) {
            throw CannotSaveInDatabaseException()
        } finally {
            endTransaction()
        }
    }

    private fun fileName(uri: Uri): String? =
            when (uri.scheme) {
                SCHEME_CONTENT -> queryFileName(uri)
                SCHEME_FILE -> uri.lastPathSegment
                else -> null
            }

    private fun queryFileName(uri: Uri): String? =
            contentResolver.query(uri, null, null, null, null)
                    ?.use { cursor ->
                        if (cursor.moveToFirst()) {
                            cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                        } else {
                            null
                        }
                    }

    private fun parseFile(uri: Uri): FileParseResult {
        try {
            contentResolver.openInputStream(uri).use {
                val documentType = getDocumentType(uri)
                return Parser().parse(it!!, documentType)
            }
        } catch (e: Exception) {
            throw ParseFileException()
        }
    }

    private fun getDocumentType(uri: Uri): DocumentType {
        val extension = MimeTypeMap.getFileExtensionFromUrl(uri.path)
        return if ("doc".equals(extension, ignoreCase = true)) {
            DocumentType.DOC
        } else {
            DocumentType.TXT
        }
    }
}