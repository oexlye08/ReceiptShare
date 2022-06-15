package id.my.okisulton.receiptprint

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import id.my.okisulton.receiptprint.databinding.ActivityInputBinding
import id.my.okisulton.receiptprint.model.Detail
import id.my.okisulton.receiptprint.util.Constants.DETAIL_COSTUMER

class InputActivity : AppCompatActivity() {
    private var _binding: ActivityInputBinding? = null
    private val binding get() = _binding!!

    private var costumer = ""
    private var product = ""
    private var amount = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityInputBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onStart() {
        setupListener()
        super.onStart()
    }

    private fun setupListener() {
        binding.layoutContent.btnNext.setOnClickListener { onBtnNextClicked() }
    }

    private fun onBtnNextClicked() {
        binding.layoutContent.apply {
            costumer = etCostumer.text.toString()
            product = etProduct.text.toString()
            amount = etAMount.text.toString()
        }
        val detail = Detail(
            costumer = costumer,
            product = product,
            amount = amount
        )
        Log.d("TAG", "onBtnNextClicked: $detail")
        val moveData =Intent(this, MainActivity::class.java)
        with(moveData) {
            putExtra(DETAIL_COSTUMER, detail)
            startActivity(this)
        }
    }
}