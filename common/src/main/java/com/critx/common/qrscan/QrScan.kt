package com.critx.common.qrscan

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import androidx.activity.result.ActivityResultLauncher
import androidx.fragment.app.Fragment
import com.critx.common.ui.showSuccessDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions


fun Fragment.scanQrCode(context: Context,barlauncher:Any){
//    val intentIntegrator = IntentIntegrator(this)
//    intentIntegrator.setDesiredBarcodeFormats(listOf(IntentIntegrator.QR_CODE))
//    intentIntegrator.initiateScan()

    val options = ScanOptions()
//    options.setPrompt("Volume Up to flash on")
//    options.setBeepEnabled(true)
    options.setOrientationLocked(true)

    options.captureActivity =CaptureAct::class.java
//    val barlauncher=this.getBarLauncher(context)

    val gg =barlauncher as ActivityResultLauncher<ScanOptions>
    gg.launch(options)
}

fun Fragment.getBarLauncher(context: Context):ActivityResultLauncher<ScanOptions>{
    return registerForActivityResult(ScanContract()) { result ->
            if (result.getContents() != null) {
                val builder: MaterialAlertDialogBuilder =MaterialAlertDialogBuilder(context)
                builder.setTitle("Result")
                builder.setMessage(result.contents)
                builder.setPositiveButton("OK",
                    DialogInterface.OnClickListener { dialogInterface, i ->
//                        scanAction(result.contents)
                        dialogInterface.dismiss()
                    })
                    .show()
            }
        }
}
fun Fragment.getBarLauncherTest(context: Context, scanAction:(gg:String)-> Unit):ActivityResultLauncher<ScanOptions>{
    return registerForActivityResult(ScanContract()) { result ->
        if (result.getContents() != null) {
            val builder: MaterialAlertDialogBuilder =MaterialAlertDialogBuilder(context)
            builder.setTitle("Result")
            builder.setMessage(result.contents)
            builder.setPositiveButton("OK",
                DialogInterface.OnClickListener { dialogInterface, i ->
                    scanAction(result.contents)
                    dialogInterface.dismiss()
                })
                .show()
        }
    }
}