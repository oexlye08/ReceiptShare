package id.my.okisulton.receiptprint.util

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.app.Activity
import android.content.Context
import com.vmadalin.easypermissions.EasyPermissions
import id.my.okisulton.receiptprint.MainActivity
import id.my.okisulton.receiptprint.util.Constants.PERMISSION_STORAGE_REQUEST_CODE

/**
 * Created by Oki Sulton on 15/06/2022.
 */
object Permission {
    fun hasStoragePermission(context: Context) =
        EasyPermissions.hasPermissions(
            context,
            READ_EXTERNAL_STORAGE
        )

    fun requestStoragePermission(activity: Activity){
        EasyPermissions.requestPermissions(
            activity,
            "This Application need to read storage location",
            PERMISSION_STORAGE_REQUEST_CODE,
            READ_EXTERNAL_STORAGE
        )
    }
}