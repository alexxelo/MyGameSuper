package com.ilyin.tools_android.util

import android.annotation.TargetApi
import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.webkit.MimeTypeMap
import java.io.*
import java.util.*
import java.util.zip.ZipInputStream


object FileUtil {

  fun getMimeType(file: File): String {
    val fileExtension = getExtension(file)
    return MimeTypeMap.getSingleton()
      .getMimeTypeFromExtension(fileExtension) ?: ""
  }

  fun getFileName(file: File): String {
    val name = file.name
    return name.substring(0, name.lastIndexOf("."))
  }

  fun getExtension(file: File): String {
    return MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(file)
      .toString())
      .lowercase(Locale.getDefault())
  }

  /**
   * Get a file path from a Uri. This will get the the path for Storage Access
   * Framework Documents, as well as the _data field for the MediaStore and
   * other file-based ContentProviders.
   *
   * @param context The context.
   * @param uri     The Uri to query.
   */
  @TargetApi(Build.VERSION_CODES.KITKAT)
  fun getPath(context: Context, uri: Uri): String? {
    val colon = ":"

    val isNewerThanKitkat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT

    // DocumentProvider
    if (isNewerThanKitkat && DocumentsContract.isDocumentUri(context, uri)) {
      // ExternalStorageProvider
      if (isExternalStorageDocument(uri)) {
        val docId = DocumentsContract.getDocumentId(uri)
        val split = docId.split(colon.toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val type = split[0]

        if ("primary".equals(type, ignoreCase = true)) {
          return context.getExternalFilesDir(null).toString() + File.separator + split[1]
        }
      } else if (isDownloadsDocument(uri)) {
        // DownloadsProvider
        val id = DocumentsContract.getDocumentId(uri)
        val contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), java.lang.Long.valueOf(id))

        return getDataColumn(context, contentUri, null, null)
      } else if (isMediaDocument(uri)) {
        // MediaProvider
        val docId = DocumentsContract.getDocumentId(uri)
        val split = docId.split(colon.toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val type = split[0]
        val contentUri = when (type) {
          "image" -> MediaStore.Images.Media.EXTERNAL_CONTENT_URI
          "video" -> MediaStore.Video.Media.EXTERNAL_CONTENT_URI
          "audio" -> MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
          else -> null
        }

        val selection = "_id=?"
        val selectionArgs = arrayOf(split[1])
        return getDataColumn(context, contentUri, selection, selectionArgs)
      }
    } else if ("content".equals(uri.scheme, ignoreCase = true)) {
      // MediaStore (and general)
      return getDataColumn(context, uri, null, null)
    } else if ("file".equals(uri.scheme, ignoreCase = true)) {
      // File
      return uri.path
    }

    return null
  }

  /**
   * Get the value of the data column for this Uri. This is useful for
   * MediaStore Uris, and other file-based ContentProviders.
   *
   * @param context       The context.
   * @param uri           The Uri to query.
   * @param selection     (Optional) Filter used in the query.
   * @param selectionArgs (Optional) Selection arguments used in the query.
   * @return The value of the _data column, which is typically a file path.
   */
  private fun getDataColumn(context: Context, uri: Uri?, selection: String?, selectionArgs: Array<String>?): String? {

    var cursor: Cursor? = null
    val column = "_data"
    val projection = arrayOf(column)

    try {
      cursor = context.contentResolver
        .query(uri!!, projection, selection, selectionArgs, null)
      if (cursor != null && cursor.moveToFirst()) {
        val columnIndex = cursor.getColumnIndexOrThrow(column)
        return cursor.getString(columnIndex)
      }
    } finally {
      cursor?.close()
    }
    return null
  }


  /**
   * @param uri The Uri to check.
   * @return Whether the Uri authority is ExternalStorageProvider.
   */
  private fun isExternalStorageDocument(uri: Uri): Boolean {
    return "com.android.externalstorage.documents" == uri.authority
  }

  /**
   * @param uri The Uri to check.
   * @return Whether the Uri authority is DownloadsProvider.
   */
  private fun isDownloadsDocument(uri: Uri): Boolean {
    return "com.android.providers.downloads.documents" == uri.authority
  }

  /**
   * @param uri The Uri to check.
   * @return Whether the Uri authority is MediaProvider.
   */
  private fun isMediaDocument(uri: Uri): Boolean {
    return "com.android.providers.media.documents" == uri.authority
  }

  @Throws(IOException::class)
  fun writeToFile(text: String, file: File) {
    FileWriter(file).apply {
      write(text)
      flush()
      close()
    }
  }

  @Throws(IOException::class)
  fun readFromFile(filePath: String): String {
    return readFromFile(File(filePath))
  }

  @Throws(IOException::class)
  fun readFromFile(file: File): String {
    val fin = FileInputStream(file)
    val ret = convertStreamToString(fin)
    //Make sure you close all streams.
    fin.close()
    return ret
  }

  @Throws(IOException::class)
  fun convertStreamToString(stream: InputStream): String {
    // http://www.java2s.com/Code/Java/File-Input-Output/ConvertInputStreamtoString.htm
    val reader = BufferedReader(InputStreamReader(stream))
    val sb = StringBuilder()
    var firstLine = true
    while (true) {
      val line = reader.readLine() ?: break
      if (firstLine) {
        sb.append(line)
        firstLine = false
      } else {
        sb.append("\n")
          .append(line)
      }
    }
    reader.close()
    return sb.toString()
  }

  fun saveZip(file: File, zipInputStream: ZipInputStream): File {
    file.delete()
    file.createNewFile()
    val fout = FileOutputStream(file)
    val buffer = ByteArray(1024 * 1024)
    var len = zipInputStream.read(buffer)
    while (len != -1) {
      fout.write(buffer, 0, len)
      len = zipInputStream.read(buffer)
    }
    zipInputStream.closeEntry()
    fout.close()
    return file
  }

  fun saveFile(file: File, inputStream: InputStream): File {
    inputStream.use {
      FileOutputStream(file).use { fout ->
        val buffer = ByteArray(1024 * 1024)
        var len = inputStream.read(buffer)
        while (len != -1) {
          fout.write(buffer, 0, len)
          len = inputStream.read(buffer)
        }
      }
    }
    return file
  }

  fun saveBitmapToFile(bmp: Bitmap, file: File) {
    try {
      FileOutputStream(file.absolutePath).use { out ->
        bmp.compress(Bitmap.CompressFormat.PNG, 100, out) // bmp is your Bitmap instance
      }
    } catch (e: IOException) {
      e.printStackTrace()
    }
  }
}