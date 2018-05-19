package atanana.com.sireader.dagger

import android.content.ContentResolver
import android.content.Context
import android.content.res.Resources
import dagger.Module
import dagger.Provides

@Module
class ContextModule {
    @Provides
    fun provideContentResolver(context: Context): ContentResolver = context.contentResolver

    @Provides
    fun provideResources(context: Context): Resources = context.resources
}