package com.first.engine2.node

import com.first.engine2.game.Action

data class NodeAction constructor(val action: Action, override val id: Int): Node