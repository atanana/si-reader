package atanana.com.sireader.dagger

import android.content.Context
import atanana.com.sireader.App
import atanana.com.sireader.PacksActivity
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.android.AndroidInjector
import dagger.android.ContributesAndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Module
internal abstract class InjectorsModule {
    @ContributesAndroidInjector
    abstract fun packsActivity(): PacksActivity
}

@Component(modules = [
    AndroidSupportInjectionModule::class,
    InjectorsModule::class,
    DatabaseModule::class
])
@Singleton
interface AppComponent : AndroidInjector<App> {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun applicationContext(context: Context): Builder

        fun build(): AppComponent
    }
}