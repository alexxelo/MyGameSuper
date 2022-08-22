package com.ilyin.tools_android.filesystem

import android.content.Context

class InternalStorage constructor(ctx: Context) : Storage(ctx.filesDir)