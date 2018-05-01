package atanana.com.sireader.dagger

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import atanana.com.sireader.viewmodels.*
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
    @ViewModelKey(FilesListViewModel::class)
    internal abstract fun filesListViewModel(viewModel: FilesListViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(FileViewModel::class)
    internal abstract fun fileViewModel(viewModel: FileViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(PackViewModel::class)
    internal abstract fun packViewModel(viewModel: PackViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(PacksPagerViewModel::class)
    internal abstract fun packsPagerViewModel(viewModel: PacksPagerViewModel): ViewModel
}