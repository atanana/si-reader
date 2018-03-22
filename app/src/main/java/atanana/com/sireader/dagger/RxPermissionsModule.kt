package atanana.com.sireader.dagger

import atanana.com.sireader.PacksActivity
import com.tbruyelle.rxpermissions2.RxPermissions
import dagger.Module
import dagger.Provides

@Module
class RxPermissionsModule {
    @Provides
    fun provideRxPermissions(activity: PacksActivity): RxPermissions = RxPermissions(activity)
}