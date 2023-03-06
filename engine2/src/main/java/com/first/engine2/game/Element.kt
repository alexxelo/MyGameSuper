package com.first.engine2.game

data class Element(val atomicMass: Int) {

  fun compareTo(e: Element): Int {
    return e.atomicMass - atomicMass
  }
}