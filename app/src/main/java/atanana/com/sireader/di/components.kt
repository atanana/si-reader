package atanana.com.sireader.di

import atanana.com.sireader.App
import atanana.com.sireader.PacksActivity
import dagger.Component
import dagger.Module
import dagger.android.AndroidInjectionModule
import dagger.android.ContributesAndroidInjector

@Module
internal abstract class PacksActivityModule {
    @ContributesAndroidInjector
    abstract fun packsActivity(): PacksActivity
}

@Component(modules = [
    ContextModule::class,
    AndroidInjectionModule::class,
    PacksActivityModule::class
])
interface AppComponent {
    fun inject(app: App)
}