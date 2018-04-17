package atanana.com.sireader.viewmodels

import atanana.com.sireader.database.PacksDao
import javax.inject.Inject

class PacksPagerViewModel @Inject constructor(
        private val packsDao: PacksDao
) : BaseViewModel() {

}