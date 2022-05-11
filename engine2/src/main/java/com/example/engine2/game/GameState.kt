package com.example.engine2.game

import com.example.engine2.game.request.GameRequest
import com.example.engine2.node.Node
import com.example.engine2.node.NodeAction
import com.example.engine2.node.NodeElement
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

    fun executeRequest(gameRequest: GameRequest) {
        when (gameRequest) {
            is GameRequest.DispatchNode -> {
                prevActiveNodeMinus = false
                dispatch(gameRequest)
            }
            is GameRequest.TurnMinusToPlus -> {
                prevActiveNodeMinus = false
                turnMinusToPlus()
            }
            is GameRequest.ExtractWithMinus -> {
                prevActiveNodeMinus = false
                extractWithMinus(gameRequest)
            }
            is GameRequest.DoNothing -> {}
        }
    }

    private fun extractWithMinus(gameRequest: GameRequest.ExtractWithMinus) {
        val clickedNode = nodes.find { it.id == gameRequest.nodeId }
        if (clickedNode !== null) {
            prevActiveNodeMinus = true
            activeNode = clickedNode
            nodes.remove(clickedNode)
        }
    }

    private fun turnMinusToPlus() {
        activeNode = NodeAction(Action.PLUS, id++)
        prevActiveNodeMinus = false
    }

    private fun dispatch(gameRequest: GameRequest.DispatchNode) {
        val dispatchIndex = if (nodes.size > 0) {
            gameRequest.leftNodeIndex + 1
        } else {
            0
        }
        nodes.add(dispatchIndex, activeNode)

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
            NodeAction(action = listOf(Action.PLUS, Action.MINUS).random(), id++)
        }
    }

    private fun merge(pattern: List<Pair<NodeElement, NodeElement>>): NodeElement {
        return NodeElement(
            element = Element(pattern[0].first.element.atomicMass + pattern.size),
            id++
        )
    }

    private fun findRepetitivePattern(plus: NodeAction): List<Pair<NodeElement, NodeElement>> {
        val plusPosition = nodes.indexOf(plus)
        val listOfPairs = mutableListOf<Pair<NodeElement, NodeElement>>()
        var leftNodeIndex = findLeftIndex(plusPosition)
        var rightNodeIndex = findRightIndex(plusPosition)
        while (true) {
            if (leftNodeIndex == rightNodeIndex) break

            val leftNode = nodes.getOrNull(leftNodeIndex) as? NodeElement ?: break
            val rightNode = nodes.getOrNull(rightNodeIndex) as? NodeElement ?: break

            if (leftNode.element.atomicMass != rightNode.element.atomicMass) break

            listOfPairs.add(Pair(leftNode, rightNode))
            rightNodeIndex = findRightIndex(rightNodeIndex)
            leftNodeIndex = findLeftIndex(leftNodeIndex)
        }
        return listOfPairs
    }

    private fun findLeftIndex(index: Int): Int {
        return if (0 == index) {
            nodes.lastIndex
        } else {
            index - 1
        }
    }

    private fun findRightIndex(index: Int): Int {
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