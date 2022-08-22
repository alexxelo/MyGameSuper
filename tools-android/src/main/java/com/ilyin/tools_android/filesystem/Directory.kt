package com.ilyin.tools_android.filesystem

import java.io.File

open class Directory constructor(val path: String) {

  constructor(parent: Directory, name: String) : this(File(parent.asFile(), name).path)

  fun asFile(): File {
    val file = File(path)
    file.mkdirs()
    file.mkdir()
    return file
  }

  fun clear() {
    asFile().listFiles()?.forEach {
      it.deleteRecursively()
    }
  }

  fun isEmpty(): Boolean {
    return asFile().listFiles()?.isEmpty() ?: true
  }

  val parentFile: File by lazy { asFile().parentFile }
}
