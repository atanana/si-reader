package atanana.com.sireader.di

import atanana.com.sireader.App
import atanana.com.sireader.PacksActivity
import dagger.Component
import dagger.Subcomponent
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import android.app.Activity
import dagger.android.ActivityKey
import dagger.multibindings.IntoMap
import dagger.Binds
import dagger.Module


@Subcomponent
interface PacksActivitySubcomponent : AndroidInjector<PacksActivity> {
    @Subcomponent.Builder
    abstract class Builder : AndroidInjector.Builder<PacksActivity>()
}

@Module(subcomponents = [(PacksActivitySubcomponent::class)])
internal abstract class PacksActivityModule {
    @Binds
    @IntoMap
    @ActivityKey(PacksActivity::class)
    internal abstract fun bindPacksActivityInjectorFactory(builder: PacksActivitySubcomponent.Builder): AndroidInjector.Factory<out Activity>
}

@Component(modules = [ContextModule::class, AndroidInjectionModule::class, PacksActivityModule::class])
interface AppComponent {
    fun inject(app: App)
}