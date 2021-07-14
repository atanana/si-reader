package atanana.com.sireader.database

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "questions",
    foreignKeys = [
        ForeignKey(
            entity = PackEntity::class,
            parentColumns = ["id"],
            childColumns = ["packId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("packId")]
)
data class QuestionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val question: String,
    val answer: String,
    val alsoAnswer: String?,
    val notAnswer: String?,
    val comment: String?,
    val reference: String?,
    val packId: Int
)

@Entity(
    tableName = "packs",
    foreignKeys = [
        ForeignKey(
            entity = QuestionFileEntity::class,
            parentColumns = ["id"],
            childColumns = ["fileId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("fileId")]
)
data class PackEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val topic: String,
    val author: String?,
    val notes: String?,
    val index: Int,
    val fileId: Int
) {
    val indexedTitle: String
        get() = "$index. $topic"
}

@Entity(tableName = "files")
data class QuestionFileEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val title: String,
    val filename: String,
    val notes: String?,
    val editor: String?,
    val lastReadPackId: Int? = null
)