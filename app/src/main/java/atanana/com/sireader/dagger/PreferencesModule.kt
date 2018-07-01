package atanana.com.sireader.dagger

import android.content.Context
import atanana.com.sireader.preferences.Prefs
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class PreferencesModule {
    @Provides
    @Singleton
    fun providePreferences(context: Context): Prefs = Prefs(context)
}