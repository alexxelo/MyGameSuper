package com.ilyin.tools_android.filesystem

import java.io.File

open class Storage constructor(file: File) : Directory(file.path)
