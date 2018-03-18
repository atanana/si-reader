package atanana.com.sireader.dagger

import atanana.com.sireader.files.OpenFileHandler
import atanana.com.sireader.viewmodels.PacksViewModelFactory
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ViewModelModule {
    @Provides
    @Singleton
    fun providePacksViewModelFactory(openFileHandler: OpenFileHandler) = PacksViewModelFactory(openFileHandler)
}