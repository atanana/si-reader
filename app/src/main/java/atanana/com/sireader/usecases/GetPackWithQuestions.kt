package atanana.com.sireader.usecases

import android.content.res.Resources
import atanana.com.sireader.R
import atanana.com.sireader.database.PackEntity
import atanana.com.sireader.database.PacksDao
import atanana.com.sireader.database.QuestionEntity
import atanana.com.sireader.database.QuestionsDao
import atanana.com.sireader.screens.pack.QuestionItem
import io.reactivex.Flowable
import io.reactivex.functions.BiFunction
import javax.inject.Inject

class GetPackWithQuestions @Inject constructor(
        private val packsDao: PacksDao,
        private val questionsDao: QuestionsDao,
        private val resources: Resources
) {
    fun getPack(packId: Int): Flowable<Pair<PackEntity, List<QuestionItem>>> =
            pack(packId).zipWith(questions(packId), zipPackWithQuestions())

    private fun questions(packId: Int) = questionsDao.questionsForPack(packId)

    private fun pack(packId: Int) = packsDao.pack(packId)

    private fun zipPackWithQuestions(): BiFunction<PackEntity, List<QuestionEntity>, Pair<PackEntity, List<QuestionItem>>> {
        return BiFunction { pack, questions ->
            Pair(pack, mapQuestions(questions))
        }
    }

    private fun mapQuestions(questions: List<QuestionEntity>): List<QuestionItem> =
            questions.withIndex()
                    .map { (index, question) ->
                        val price = calculateQuestionPrice(index)
                        QuestionItem(question, true, price)
                    }

    private fun calculateQuestionPrice(index: Int): String =
            if (index < 5) {
                ((index + 1) * 10).toString()
            } else {
                resources.getString(R.string.reserve)
            }
}