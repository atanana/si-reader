package atanana.com.sireader.dagger

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import atanana.com.sireader.viewmodels.FileViewModel
import atanana.com.sireader.viewmodels.FilesListActivityViewModel
import atanana.com.sireader.viewmodels.FilesListViewModel
import atanana.com.sireader.viewmodels.ViewModelFactory
import dagger.Binds
import dagger.MapKey
import dagger.Module
import dagger.multibindings.IntoMap
import kotlin.reflect.KClass

@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
@MapKey
internal annotation class ViewModelKey(val value: KClass<out ViewModel>)

@Module
abstract class ViewModelModule {
    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(FilesListActivityViewModel::class)
    internal abstract fun filesListActivityViewModel(viewModel: FilesListActivityViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(FilesListViewModel::class)
    internal abstract fun filesListViewModel(viewModel: FilesListViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(FileViewModel::class)
    internal abstract fun fileViewModel(viewModel: FileViewModel): ViewModel
}