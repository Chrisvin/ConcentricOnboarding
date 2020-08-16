package com.example.concentriconboarding

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.jem.concentriconboarding.clippathprovider.ConcentricOnboardingClipPathProvider
import com.jem.concentriconboardingdemo.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
