package atanana.com.sireader.dagger

import android.app.Activity
import android.content.Context
import atanana.com.sireader.App
import atanana.com.sireader.PacksActivity
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.Provides
import dagger.android.AndroidInjector
import dagger.android.ContributesAndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Module
class PacksActivityModule {
    @Provides
    fun provideActivity(packsActivity: PacksActivity): Activity = packsActivity
}

@Module
internal abstract class InjectorsModule {
    @ContributesAndroidInjector(modules = [RxPermissionsModule::class, PacksActivityModule::class])
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