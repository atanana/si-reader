package atanana.com.sireader.files

import android.content.ContentResolver
import android.net.Uri
import android.provider.OpenableColumns
import atanana.com.sireader.CannotSaveInDatabaseException
import atanana.com.sireader.ParseFileException
import atanana.com.sireader.database.*
import com.atanana.si_parser.QuestionData
import com.atanana.si_parser.QuestionPack
import com.atanana.si_parser.QuestionPackTokenizer
import com.atanana.si_parser.SiInfo
import com.atanana.si_parser.parsing.InfoParser
import com.atanana.si_parser.parsing.QuestionPackParser
import io.reactivex.Completable
import io.reactivex.schedulers.Schedulers
import org.apache.poi.hwpf.HWPFDocument
import org.apache.poi.hwpf.extractor.WordExtractor
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
            val fileId = filesDao.insertFile(
                    QuestionFileEntity(0, siInfo.title, name, siInfo.notes, siInfo.editor)
            )
            questionPacks.forEach { pack ->
                val packId = packsDao.insertPack(
                        PackEntity(0, pack.topic, pack.author, pack.notes, fileId.toInt())
                )
                pack.questions.forEach { question ->
                    saveQuestion(question, packId.toInt())
                }
            }
        }
    }
            .subscribeOn(Schedulers.io())

    private fun saveQuestion(question: QuestionData, packId: Int) {
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
    }

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

    private fun fileName(uri: Uri): String? = contentResolver.query(uri, null, null, null, null)
            ?.use { cursor ->
                if (cursor.moveToFirst()) {
                    cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                } else {
                    null
                }
            }

    private fun parseFile(uri: Uri): Pair<SiInfo, List<QuestionPack>> {
        try {
            contentResolver.openInputStream(uri).use {
                val document = HWPFDocument(it)
                val wordExtractor = WordExtractor(document)
                val text = wordExtractor.text
                val tokens = QuestionPackTokenizer.process(text)
                val siInfo = InfoParser.parse(tokens.first())
                val questionPacks = tokens.drop(1).map {
                    QuestionPackParser.parse(it)
                }
                return Pair(siInfo, questionPacks)
            }
        } catch (e: Exception) {
            throw ParseFileException()
        }
    }
}