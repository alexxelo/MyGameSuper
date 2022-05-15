package com.example.engine2.game.state

import android.util.Log
import com.example.engine2.game.Action
import com.example.engine2.game.Element
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

    fun executeRequest(gameRequest: GameRequest): List<Pair<RequestResultPart, GameState>> {
        return when (gameRequest) {
            is GameRequest.DispatchNode -> {
                prevActiveNodeMinus = false
                val dispatchResult = dispatch(gameRequest)
                listOf(dispatchResult to clone()) + executePatterns()
            }
            is GameRequest.TurnMinusToPlus -> {
                prevActiveNodeMinus = false
                turnMinusToPlus()
                listOf(RequestResultPart.TurnMinusToPlus to clone())
            }
            is GameRequest.ExtractWithMinus -> {
                prevActiveNodeMinus = false
                extractWithMinus(gameRequest)
                listOf(RequestResultPart.Extract(gameRequest.nodeId) to clone()) + executePatterns()
            }
            is GameRequest.DoNothing -> {
                RequestResult(RequestResultPart.DoNothing)
                listOf(RequestResultPart.DoNothing to clone())
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

    private fun radialNodeIndexById(nodeId: Int): Int {
        val node: Node = nodes.find { it.id == nodeId } ?: return -1
        return nodes.indexOf(node)
    }

    private fun dispatch(gameRequest: GameRequest.DispatchNode): RequestResultPart.Dispatch {
        val dispatchIndex = if (nodes.size > 0) radialNodeIndexById(gameRequest.leftNodeId) + 1 else 0
        val oldActiveNode = activeNode
        val newActiveNode = createNewActiveNode()
        nodes.add(dispatchIndex, oldActiveNode)
        activeNode = newActiveNode
        return RequestResultPart.Dispatch(
            dispatchedNodeId = oldActiveNode.id,
            leftNodeId = nodes[findLeftIndex(dispatchIndex)].id,
            rightNodeId = nodes[findRightIndex(dispatchIndex)].id,
            newActiveNodeId = newActiveNode.id,
        )
    }

    private fun executePatterns(): List<Pair<RequestResultPart, GameState>> {
        val states = mutableListOf<Pair<RequestResultPart, GameState>>()
        val pluses = getPluses()
        pluses.forEach { plus ->
            val pattern: MutableList<Pair<NodeElement, NodeElement>> = ArrayList(findRepetitivePattern(plus))
            var mergeNode: Node = plus
            while (pattern.isNotEmpty()) {
                val patternStep = pattern.removeFirst()
                val patternStepNode1 = patternStep.first
                val patternStepNode2 = patternStep.second
                val newNodeElement = merge(patternStep)
                val nodesToRemove = listOf(mergeNode, patternStepNode1, patternStepNode2)
                val startRemoveIndex = nodesToRemove.map { nodes.indexOf(it) }.filter { it >= 0 }.minOf { it }
                nodes.removeAll(nodesToRemove)
                nodes.add(startRemoveIndex, newNodeElement)
                mergeNode = newNodeElement

                val mergeResult = RequestResultPart.Merge(
                    nodeId1 = patternStepNode1.id,
                    nodeId2 = patternStepNode2.id,
                    resultId = newNodeElement.id,
                )
                states.add(mergeResult to clone())
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
                initialActiveNode = NodeAction(action = Action.MINUS, id++),
                initialId = id
            )
        }
    }
}