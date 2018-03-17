package atanana.com.sireader.db

import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "questions")
data class QuestionEntity(
        @PrimaryKey(autoGenerate = true)
        val id: Int,
        val question: String,
        val answer: String,
        val alsoAnswer: String?,
        val notAnswer: String?,
        val comment: String?,
        val reference: String?,
        @ForeignKey(
                entity = PackEntity::class,
                parentColumns = ["id"],
                childColumns = ["packId"],
                onDelete = ForeignKey.CASCADE
        )
        val packId: Int
)

@Entity(tableName = "packs")
data class PackEntity(
        @PrimaryKey(autoGenerate = true)
        val id: Int,
        val topic: String,
        val author: String?,
        val notes: String?,
        @ForeignKey(
                entity = QuestionFile::class,
                parentColumns = ["id"],
                childColumns = ["fileId"],
                onDelete = ForeignKey.CASCADE
        )
        val fileId: Int
)

@Entity(tableName = "files")
data class QuestionFile(
        @PrimaryKey(autoGenerate = true)
        val id: Int,
        val title: String,
        val notes: String?,
        val editor: String?
)