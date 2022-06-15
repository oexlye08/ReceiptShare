package id.my.okisulton.receiptprint.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Detail(
	var product: String? = null,

	var amount: String? = null,

	var costumer: String? = null
) : Parcelable
