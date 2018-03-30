package atanana.com.sireader

open class SiReaderException(message: String? = null) : RuntimeException(message)

class ParseFileException : SiReaderException()

class CannotSaveInDatabaseException : SiReaderException()