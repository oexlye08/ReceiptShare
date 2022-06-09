package id.my.okisulton.receiptprint

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Build
import android.os.Bundle
import android.widget.LinearLayout
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.content.FileProvider
import androidx.core.view.WindowCompat
import id.my.okisulton.receiptprint.databinding.ActivityMainBinding
import java.io.File
import java.io.FileOutputStream
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    private lateinit var mainLayout: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)

        _binding = ActivityMainBinding.inflate(layoutInflater)
        mainLayout = binding.mainLayout
        setContentView(binding.root)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStart() {
        super.onStart()
        setupListener()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setupListener() {
        binding.apply {
            fab.setOnClickListener {
                captureScreenShoot()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun captureScreenShoot() {
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH:mm:ss")
        val formatted = current.format(formatter)

        val path = getExternalFilesDir(null)!!.absolutePath+"/"+formatted+".jpg"
        var bitmap = Bitmap.createBitmap(mainLayout.width,mainLayout.height,Bitmap.Config.ARGB_8888)
        var canvas = Canvas(bitmap)
        mainLayout.draw(canvas)
        val imageFile = File(path)
        val outputStream = FileOutputStream(imageFile)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        outputStream.flush()
        outputStream.close()

        val URI = FileProvider.getUriForFile(applicationContext,"id.my.okisulton.receiptprint.fileprovider",imageFile)
        val intent = Intent()
        intent.action = Intent.ACTION_SEND
        intent.putExtra(Intent.EXTRA_TEXT,"Coba share"+"\n"+"disini deskripsinya")
        intent.putExtra(Intent.EXTRA_STREAM,URI)
        intent.type = "text/plain"
        startActivity(intent)
    }

}
