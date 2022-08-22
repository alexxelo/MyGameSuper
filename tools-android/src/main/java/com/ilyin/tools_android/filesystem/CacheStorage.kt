package com.ilyin.tools_android.filesystem

import android.content.Context

class CacheStorage constructor(ctx: Context) : Storage(ctx.cacheDir)