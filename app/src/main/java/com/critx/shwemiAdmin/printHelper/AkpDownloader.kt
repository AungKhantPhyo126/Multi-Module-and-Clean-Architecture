package com.example.satoprintertest

import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import androidx.annotation.RequiresApi
import androidx.core.net.toUri


class AkpDownloader(
    private val context:Context
):Downloader {
    @RequiresApi(Build.VERSION_CODES.M)
    val downloadManager = context.getSystemService(DownloadManager::class.java)

    @SuppressLint("Range")
    @RequiresApi(Build.VERSION_CODES.M)
    override fun downloadFile(url: String): String? {
        val request = DownloadManager.Request(url.toUri())
            .setMimeType("document/pdf")
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
            .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI)
            .setTitle("akp.pdf")
            .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,"akp.pdf")
         downloadManager.enqueue(request)

        val downloadId = downloadManager.enqueue(request)

        val query = DownloadManager.Query().setFilterById(downloadId)
        var filePath: String? = null

        while (filePath == null) {
            val cursor = downloadManager.query(query)
            if (cursor.moveToFirst()) {
                val status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))
                if (status == DownloadManager.STATUS_SUCCESSFUL) {
                    filePath = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI))
                    filePath = Uri.parse(filePath).path
                }
            }
            cursor.close()
        }

        return filePath
    }
}