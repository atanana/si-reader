package atanana.com.sireader.dagger

import android.content.ContentResolver
import android.content.Context
import dagger.Module
import dagger.Provides

@Module
class ContextModule {
    @Provides
    fun provideContentResolver(context: Context): ContentResolver = context.contentResolver
}