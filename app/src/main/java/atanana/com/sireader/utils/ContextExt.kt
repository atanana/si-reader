package atanana.com.sireader.utils

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

fun Context.checkPermission(permission: String) =
    ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED