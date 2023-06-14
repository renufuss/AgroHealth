package com.renufus.agrohealth.ui.predictDisease.result

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.renufus.agrohealth.databinding.ActivityPredictDiseaseResultBinding

class PredictDiseaseResultActivity : AppCompatActivity() {
    private val binding: ActivityPredictDiseaseResultBinding by lazy { ActivityPredictDiseaseResultBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}
