package atanana.com.sireader.files

import atanana.com.sireader.CannotSaveInDatabaseException
import atanana.com.sireader.ParseFileException
import atanana.com.sireader.database.*
import com.atanana.si_parser.QuestionData
import com.atanana.si_parser.QuestionPack
import com.atanana.si_parser.QuestionPackTokenizer
import com.atanana.si_parser.SiInfo
import com.atanana.si_parser.parsing.InfoParser
import com.atanana.si_parser.parsing.QuestionPackParser
import org.apache.poi.hwpf.HWPFDocument
import org.apache.poi.hwpf.extractor.WordExtractor
import java.io.File
import javax.inject.Inject

class ParseFileUseCase @Inject constructor(
        private val questionsDao: QuestionsDao,
        private val packsDao: PacksDao,
        private val filesDao: QuestionFilesDao,
        private val database: Database
) {
    fun process(file: File) {
        val (siInfo, questionPacks) = parseFile(file)

        database.save {
            val fileId = filesDao.insertFile(
                    QuestionFileEntity(0, siInfo.title, file.name, siInfo.notes, siInfo.editor)
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

    private fun parseFile(file: File): Pair<SiInfo, List<QuestionPack>> {
        try {
            file.inputStream().use {
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