package com.example.engine2.node

import com.example.engine2.game.Action

data class NodeAction(val action: Action, override val id: Int): Node {

}