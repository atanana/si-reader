package atanana.com.sireader.dagger

import android.app.Activity
import com.tbruyelle.rxpermissions2.RxPermissions
import dagger.Module
import dagger.Provides

@Module
class RxPermissionsModule {
    @Provides
    fun provideRxPermissions(activity: Activity): RxPermissions = RxPermissions(activity)
}