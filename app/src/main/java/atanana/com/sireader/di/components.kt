package atanana.com.sireader.di

import atanana.com.sireader.App
import atanana.com.sireader.PacksActivity
import dagger.Component
import dagger.Module
import dagger.android.AndroidInjectionModule
import dagger.android.ContributesAndroidInjector
import javax.inject.Singleton

@Module
internal abstract class InjectorsModule {
    @ContributesAndroidInjector
    abstract fun packsActivity(): PacksActivity
}

@Component(modules = [
    ContextModule::class,
    AndroidInjectionModule::class,
    InjectorsModule::class,
    DbModule::class
])
@Singleton
interface AppComponent {
    fun inject(app: App)
}