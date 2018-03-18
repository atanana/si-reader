package atanana.com.sireader

open class SiReaderException : RuntimeException()

class ParseFileException : SiReaderException()

class CannotSaveInDatabaseException : SiReaderException()