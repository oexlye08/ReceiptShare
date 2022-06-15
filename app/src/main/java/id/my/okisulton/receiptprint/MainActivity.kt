package id.my.okisulton.receiptprint

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.LinearLayout
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.content.FileProvider
import androidx.core.view.WindowCompat
import androidx.navigation.ui.AppBarConfiguration
import com.vmadalin.easypermissions.EasyPermissions
import com.vmadalin.easypermissions.dialogs.SettingsDialog
import id.my.okisulton.receiptprint.databinding.ActivityMainBinding
import id.my.okisulton.receiptprint.model.Detail
import id.my.okisulton.receiptprint.util.Constants.DETAIL_COSTUMER
import id.my.okisulton.receiptprint.util.Permission.hasStoragePermission
import id.my.okisulton.receiptprint.util.Permission.requestStoragePermission
import java.io.File
import java.io.FileOutputStream
import java.text.NumberFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.random.Random

class MainActivity : AppCompatActivity(), EasyPermissions.PermissionCallbacks {
    private lateinit var appBarConfiguration: AppBarConfiguration
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    private lateinit var mainLayout: LinearLayout
    private var dateTime = ""
    private var savedDateTime = ""
    private var costumerName = ""
    private var productName = ""
    private var totalAmount = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)

        _binding = ActivityMainBinding.inflate(layoutInflater)
        mainLayout = binding.mainLayout
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_settings -> moveToSetting()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun moveToSetting() {
        startActivity(Intent(this,SettingActivity::class.java))
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStart() {
        super.onStart()
        getData()
        getDateTime()
        updateData()
        setupListener()
    }

    private fun getData() {
        val data = intent.getParcelableExtra<Detail>(DETAIL_COSTUMER) as Detail
        data.apply {
            costumerName = costumer.toString()
            productName = product.toString()
            totalAmount = amount.toString()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun updateData() {
        val random = (1000..9999).random()

        val format = NumberFormat.getCurrencyInstance()
        format.maximumFractionDigits = 2
        format.currency = Currency.getInstance("IDR")

        binding.areaScreenShoot.apply {
            val nominal = format.format(totalAmount.toInt())

            tvCostumerName.text = ": $costumerName"
            tvProduct.text = ": $productName"
            tvAmount.text = ": $nominal"
            tvDateAndTime.text = dateTime
            tvOrderId.text = ": $random"
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getDateTime() {
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH:mm:ss")
        val formatter2 = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")
        val formatted = current.format(formatter)
        val formatted2 = current.format(formatter2)

        dateTime = formatted2
        savedDateTime = formatted
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setupListener() {
        binding.apply {
            fab.setOnClickListener {
                onFabClicked()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun onFabClicked() {
        if (hasStoragePermission(this)){
            captureScreenShoot()
        } else {
            requestStoragePermission(this)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun captureScreenShoot() {

        val path = getExternalFilesDir(null)!!.absolutePath+"/"+savedDateTime+".jpg"
        val bitmap = Bitmap.createBitmap(mainLayout.width,mainLayout.height,Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        mainLayout.draw(canvas)
        val imageFile = File(path)
        val outputStream = FileOutputStream(imageFile)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        outputStream.flush()
        outputStream.close()

        val URI = FileProvider.getUriForFile(applicationContext,"id.my.okisulton.receiptprint.fileprovider",imageFile)
        val intent = Intent()
        intent.action = Intent.ACTION_SEND
        intent.putExtra(Intent.EXTRA_TEXT, "Hai $costumerName.\nIni pembelian anda : $productName.\nTotal yang harus dibayarkan adalah $totalAmount")
        intent.putExtra(Intent.EXTRA_STREAM,URI)
        intent.type = "text/plain"
        startActivity(intent)
    }

    override fun onPermissionsDenied(requestCode: Int, perms: List<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)){
            SettingsDialog.Builder(this).build().show()
        } else {
            requestStoragePermission(this)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {
        if (hasStoragePermission(this)){
            captureScreenShoot()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

}
