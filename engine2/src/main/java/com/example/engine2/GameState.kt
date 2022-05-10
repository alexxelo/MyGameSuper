package com.example.engine2

import kotlin.random.Random

class GameState constructor(
    val nodes: MutableList<Node>,
    initialActiveNode: Node,
    initialId: Int = 1,
    initialActiveNodeMinus: Boolean = false,
) {
    var activeNode: Node = initialActiveNode

    var id: Int = initialId
    var prevActiveNodeMinus = initialActiveNodeMinus

    fun dispatchActiveNodeAt(leftNodeIndex: Int, clickedNode: Node?) {
        if (nodes.size < MAX_ELEM_COUNT) {
            prevActiveNodeMinus = false
            // -
            if ((activeNode as? NodeAction)?.action == Action.MINUS) {
                if (clickedNode !== null) {
                    prevActiveNodeMinus = true
                    activeNode = clickedNode
                    nodes.remove(clickedNode)
                }
            } else {
                if (nodes.size > 0) {
                    nodes.add(leftNodeIndex + 1, activeNode)
                } else {
                    nodes.add(0, activeNode)
                }

                val pluses = nodes.filterIsInstance(NodeAction::class.java)
                    .filter { nodeAction -> nodeAction.action == Action.PLUS }
                pluses.forEach { plus ->
                    val pattern = findRepetitivePattern(plus)
                    if (pattern.isNotEmpty()) {
                        val newNodeElement = merge(pattern)
                        nodes.removeAll(pattern.flatMap { pair -> listOf(pair.first, pair.second) })
                        val plusIndex = nodes.indexOf(plus)
                        nodes.add(plusIndex, newNodeElement)
                        nodes.remove(plus)
                    }
                }

                activeNode = if (Random.nextFloat() > 0.3f) {
                    val ass = Random.nextInt(1, 4)
                    NodeElement(element = Element(ass), id++)
                } else {
                    NodeAction(action = Action.MINUS, id++)
                }
            }


        }
    }

    fun onActiveNodeClick(): Boolean {
        if (prevActiveNodeMinus) {
            activeNode = NodeAction(Action.PLUS, id++)
            prevActiveNodeMinus = false
            return true
        }
        return false
    }

    private fun merge(pattern: List<Pair<NodeElement, NodeElement>>): NodeElement {
        return NodeElement(
            element = Element(pattern[0].first.element.atomicMass + pattern.size),
            id++
        )
    }

    fun findRepetitivePattern(plus: NodeAction): List<Pair<NodeElement, NodeElement>> {
        val plusPosition = nodes.indexOf(plus)
        val listOfPairs = mutableListOf<Pair<NodeElement, NodeElement>>()
        var leftNodeIndex = findLeftIndex(plusPosition)
        var rightNodeIndex = findRightIndex(plusPosition)
        while (true) {
            if (leftNodeIndex == rightNodeIndex) break

            val leftNode = nodes.getOrNull(leftNodeIndex)
            val rightNode = nodes.getOrNull(rightNodeIndex)

            if (leftNode !== null && rightNode !== null) {
                if (leftNode is NodeElement && rightNode is NodeElement) {
                    if (leftNode.element.atomicMass == rightNode.element.atomicMass) {
                        listOfPairs.add(Pair(leftNode, rightNode))
                        rightNodeIndex = findRightIndex(rightNodeIndex)
                        leftNodeIndex = findLeftIndex(leftNodeIndex)
                    } else {
                        break
                    }
                } else {
                    break
                }
            } else {
                break
            }
        }
        return listOfPairs
    }

    fun findLeftIndex(index: Int): Int {
        return if (0 == index) {
            nodes.lastIndex
        } else {
            index - 1
        }
    }

    fun findRightIndex(index: Int): Int {
        return if (nodes.lastIndex == index) {
            0
        } else {
            index + 1
        }
    }

    fun clone(gameState: GameState = this): GameState {
        return GameState(
            nodes = ArrayList(gameState.nodes).toMutableList(),
            initialActiveNode = gameState.activeNode,
            initialId = gameState.id,
            initialActiveNodeMinus = gameState.prevActiveNodeMinus
        )
    }

    companion object {
        const val MAX_ELEM_COUNT = 20
    }

    override fun toString(): String {
        return "Nodes amount: ${nodes.size}, activeNode = $activeNode"
    }
}