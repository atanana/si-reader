package atanana.com.sireader.di

import android.content.Context
import atanana.com.sireader.files.OpenFileHandler
import dagger.Module
import dagger.Provides

@Module
class FilesModule {
    @Provides
    fun openFileHandler(context: Context): OpenFileHandler {
        return OpenFileHandler(context)
    }
}