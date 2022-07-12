package com.critx.shwemiAdmin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.critx.common.ui.loadImageWithGlide
import com.critx.common.ui.showErrorCommonUI
import com.critx.shwemiAdmin.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding:ActivityMainBinding
    private val imgUrl ="https://live-production.wcms.abc-cdn.net.au/ff1221fbfdb2fe163fdda15df5f77676?impolicy=wcms_crop_resize&cropH=394&cropW=700&xPos=0&yPos=37&width=862&height=485"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main)

        binding.btnShowError.setOnClickListener {
            showErrorCommonUI("Multi Module Power Error")
        }
        binding.btnLoadImage.setOnClickListener {
            binding.ivTest.loadImageWithGlide(imgUrl)
        }
    }
}