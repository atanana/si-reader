package atanana.com.sireader.dagger

import android.app.Activity
import android.content.Context
import atanana.com.sireader.App
import atanana.com.sireader.PacksActivity
import atanana.com.sireader.PacksListFragment
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
    @ContributesAndroidInjector(modules = [PacksActivityModule::class])
    abstract fun packsActivity(): PacksActivity

    @ContributesAndroidInjector
    abstract fun packsListFragment(): PacksListFragment
}

@Component(modules = [
    AndroidSupportInjectionModule::class,
    InjectorsModule::class,
    DatabaseModule::class,
    ContextModule::class
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