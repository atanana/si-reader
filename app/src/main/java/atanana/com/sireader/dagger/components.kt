package atanana.com.sireader.dagger

import android.app.Activity
import android.content.Context
import atanana.com.sireader.App
import atanana.com.sireader.PacksActivity
import dagger.*
import dagger.android.ActivityKey
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import dagger.multibindings.IntoMap
import javax.inject.Singleton

@Module(subcomponents = [PackActivitySubcomponent::class])
internal abstract class PacksActivityModule {
    @Binds
    @IntoMap
    @ActivityKey(PacksActivity::class)
    abstract fun bindYourActivityInjectorFactory(builder: PackActivitySubcomponent.Builder): AndroidInjector.Factory<out Activity>
}

@Component(modules = [
    AndroidSupportInjectionModule::class,
    PacksActivityModule::class,
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

@Subcomponent
interface PackActivitySubcomponent : AndroidInjector<PacksActivity> {
    @Subcomponent.Builder
    abstract class Builder : AndroidInjector.Builder<PacksActivity>()
}