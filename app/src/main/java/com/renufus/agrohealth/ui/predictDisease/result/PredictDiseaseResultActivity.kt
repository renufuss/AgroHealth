package com.renufus.agrohealth.ui.predictDisease.result

import android.graphics.Bitmap
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.renufus.agrohealth.R
import com.renufus.agrohealth.data.model.predictDisease.predictHistory.PredictHistoryItem
import com.renufus.agrohealth.databinding.ActivityPredictDiseaseResultBinding
import com.renufus.agrohealth.utility.GeneralUtility

class PredictDiseaseResultActivity : AppCompatActivity() {
    private val binding: ActivityPredictDiseaseResultBinding by lazy { ActivityPredictDiseaseResultBinding.inflate(layoutInflater) }
    private val utility = GeneralUtility()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        utility.setButtonClickAnimation(binding.imageViewPredictResultButtonBack, R.anim.button_click_animation) {
            finish()
        }

        setDiseaseData()
    }

    private fun setDiseaseData() {
        val receivedDisease = intent.getParcelableExtra<PredictHistoryItem>(DISEASE_DATA)

        if (receivedDisease != null) {
            binding.textViewPredictDiseaseDiseaseName.text = receivedDisease.diseaseName
            binding.textViewPredictDiseaseDiseaseDescription.text = receivedDisease.diseaseDescription
            binding.textViewPredictDiseaseDiseaseSolution.text = receivedDisease.diseaseSolution

            Glide.with(binding.imageViewPredictDiseasePreview)
                .load(receivedDisease.imageUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .override(800, 600)
                .apply(RequestOptions().encodeFormat(Bitmap.CompressFormat.JPEG))
                .thumbnail(0.25f)
                .placeholder(R.drawable.text_logo)
                .error(R.drawable.text_logo)
                .centerCrop()
                .into(binding.imageViewPredictDiseasePreview)
        }
    }

    companion object {
        const val DISEASE_DATA = "DISEASE_DATA"
    }
}
