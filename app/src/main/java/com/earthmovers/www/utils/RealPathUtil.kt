package com.earthmovers.www.utils

import android.content.Context
import android.net.Uri
import android.os.Environment
import java.io.File
import java.io.FileOutputStream

object RealPathUtil {

    private fun createImageFile(context: Context, fileName: String = "temp_image"): File {
        val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(fileName, ".jpg", storageDir)
    }

    private fun uriToFile(context: Context, uri: Uri, fileName: String): File? {
        context.contentResolver.openInputStream(uri)?.let { inputStream ->

            val tempFile: File = createImageFile(context, fileName)
            val fileOutputStream = FileOutputStream(tempFile)

            inputStream.copyTo(fileOutputStream)
            inputStream.close()
            fileOutputStream.close()

            return tempFile
        }

        return null
    }

    fun returnImagePath(context: Context, uri: Uri, fileName: String): String? {
        return uriToFile(context, uri, fileName)?.absolutePath
    }

}