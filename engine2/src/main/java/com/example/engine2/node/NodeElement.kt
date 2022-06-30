package com.example.engine2.node

import com.example.engine2.game.Element

data class NodeElement constructor(val element: Element, override val id: Int) : Node {

  operator fun compareTo(e: NodeElement): Int {
    return e.element.compareTo(element)
  }
}