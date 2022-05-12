package com.example.engine2.game

import android.util.Log
import com.example.engine2.game.request.GameRequest
import com.example.engine2.game.result.RequestResult
import com.example.engine2.game.result.RequestResultPart
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

    var nextId: Int = initialId
    var prevActiveNodeMinus = initialActiveNodeMinus

    fun executeRequest(gameRequest: GameRequest): List<GameState> {
        return when (gameRequest) {
            is GameRequest.DispatchNode -> {
                prevActiveNodeMinus = false
                dispatch(gameRequest)
            }
            is GameRequest.TurnMinusToPlus -> {
                prevActiveNodeMinus = false
                turnMinusToPlus()
//                RequestResult(RequestResultPart.TurnMinusToPlus)
                listOf(clone())
            }
            is GameRequest.ExtractWithMinus -> {
                prevActiveNodeMinus = false
                extractWithMinus(gameRequest)
//                RequestResult(RequestResultPart.Extract(gameRequest.nodeId))
                listOf(clone()) + executePatterns()
            }
            is GameRequest.DoNothing -> {
                RequestResult(RequestResultPart.DoNothing)
                listOf(clone())
            }
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
        activeNode = NodeAction(Action.PLUS, getIdAndInc())
        prevActiveNodeMinus = false
    }

    private fun dispatch(gameRequest: GameRequest.DispatchNode): List<GameState> {
        val dispatchIndex = if (nodes.size > 0) gameRequest.leftNodeIndex + 1 else 0
        nodes.add(dispatchIndex, activeNode)
        this.activeNode = createNewActiveNode()
        val afterDispatch = clone()
        val afterPatterns = executePatterns()
        return listOf(afterDispatch) + afterPatterns
    }

    private fun executePatterns(): List<GameState> {
        val states = mutableListOf<GameState>()
        val pluses = getPluses()
        pluses.forEach { plus ->
            val pattern: MutableList<Pair<NodeElement, NodeElement>> = ArrayList(findRepetitivePattern(plus))
            var mergeNode: Node = plus
            while (pattern.isNotEmpty()) {
                val patternStep = pattern.removeFirst()
                val newNodeElement = merge(patternStep)
                val nodesToRemove = listOf(mergeNode, patternStep.first, patternStep.second)
                val startRemoveIndex = nodesToRemove.map { nodes.indexOf(it) }.minOf { it }
                nodes.removeAll(nodesToRemove)
                nodes.add(startRemoveIndex, newNodeElement)
                mergeNode = newNodeElement
                states.add(clone())
            }
        }
        return states
    }

    private fun createNewActiveNode(): Node {
        return if (Random.nextFloat() > 0.3f) {
            val ass = Random.nextInt(1, 4)
            NodeElement(element = Element(ass), getIdAndInc())
        } else {
            NodeAction(action = listOf(Action.PLUS, Action.MINUS).random(), getIdAndInc())
        }
    }

    private fun getPluses(): List<NodeAction> {
        return nodes.filterIsInstance(NodeAction::class.java).filter { nodeAction -> nodeAction.action == Action.PLUS }
    }

    private fun merge(patternStep: Pair<NodeElement, NodeElement>): NodeElement {
        return NodeElement(
            element = Element(patternStep.first.element.atomicMass + 1),
            getIdAndInc()
        )
    }

    private fun merge(pattern: List<Pair<NodeElement, NodeElement>>): NodeElement {
        return NodeElement(
            element = Element(pattern[0].first.element.atomicMass + pattern.size),
            getIdAndInc()
        )
    }

    private fun getIdAndInc(): Int {
        val idToReturn = nextId++
        Log.d("lol", "$idToReturn")
        return idToReturn
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
            initialId = gameState.nextId,
            initialActiveNodeMinus = gameState.prevActiveNodeMinus
        )
    }

    override fun toString(): String {
        return "Nodes amount: ${nodes.size}, activeNode = $activeNode"
    }

    companion object {
        const val MAX_ELEM_COUNT = 20

        fun createInitial(): GameState {
            var id = 1
            return GameState(
                nodes = mutableListOf(
                    NodeElement(element = Element(3), id++),
                    NodeElement(element = Element(2), id++),
                    NodeElement(element = Element(4), id++),
                    NodeElement(element = Element(4), id++),
                    NodeElement(element = Element(2), id++),
                    NodeElement(element = Element(3), id++),
                    NodeElement(element = Element(1), id++),
                ),
                initialActiveNode = NodeAction(action = Action.PLUS, id++),
                initialId = id
            )
        }
    }
}