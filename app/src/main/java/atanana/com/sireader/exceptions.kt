package atanana.com.sireader

open class SiReaderException(message: String? = null, cause: Throwable? = null) : RuntimeException(message, cause)

class ParseFileException(cause: Throwable) : SiReaderException(cause = cause)

class CannotSaveInDatabaseException(cause: Throwable) : SiReaderException(cause = cause)