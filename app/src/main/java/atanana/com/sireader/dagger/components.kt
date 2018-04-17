package atanana.com.sireader.dagger

import android.app.Activity
import android.content.Context
import atanana.com.sireader.App
import atanana.com.sireader.FilesListActivity
import atanana.com.sireader.fragments.FileFragment
import atanana.com.sireader.fragments.FilesListFragment
import atanana.com.sireader.fragments.PackFragment
import atanana.com.sireader.fragments.PacksPagerFragment
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.Provides
import dagger.android.AndroidInjector
import dagger.android.ContributesAndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Module
class FilesListActivityModule {
    @Provides
    fun provideActivity(filesListActivity: FilesListActivity): Activity = filesListActivity
}

@Module
internal abstract class InjectorsModule {
    @ContributesAndroidInjector(modules = [FilesListActivityModule::class])
    abstract fun filesListActivity(): FilesListActivity

    @ContributesAndroidInjector
    abstract fun filesListFragment(): FilesListFragment

    @ContributesAndroidInjector
    abstract fun fileFragment(): FileFragment

    @ContributesAndroidInjector
    abstract fun packFragment(): PackFragment

    @ContributesAndroidInjector
    abstract fun packsPagerFragment(): PacksPagerFragment
}

@Component(modules = [
    AndroidSupportInjectionModule::class,
    InjectorsModule::class,
    DatabaseModule::class,
    ContextModule::class,
    ViewModelModule::class
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