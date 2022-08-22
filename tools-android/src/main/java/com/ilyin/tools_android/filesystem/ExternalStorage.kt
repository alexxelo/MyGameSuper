package com.ilyin.tools_android.filesystem

import android.content.Context
import java.io.File

class ExternalStorage constructor(externalFileStorage: File) : Storage(externalFileStorage) {

  companion object {

    fun tryToAccess(ctx: Context): ExternalStorage? {
      return ctx.getExternalFilesDir(null)?.let { ExternalStorage(it) }
    }

    fun access(ctx: Context): ExternalStorage {
      return tryToAccess(ctx) ?: throw RuntimeException("External storage is not accessed")
    }
  }
}
