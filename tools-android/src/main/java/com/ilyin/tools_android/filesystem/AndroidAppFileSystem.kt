package com.ilyin.tools_android.filesystem

import android.content.Context

class AndroidAppFileSystem constructor(private val ctx: Context) {

  fun getInternal(): InternalStorage {
    return InternalStorage(ctx)
  }

  fun getExternal(): ExternalStorage? {
    return ExternalStorage.tryToAccess(ctx)
  }

  fun getCache(): CacheStorage {
    return CacheStorage(ctx)
  }
}
