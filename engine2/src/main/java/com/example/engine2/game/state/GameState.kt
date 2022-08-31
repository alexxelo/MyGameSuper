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
import kotlin.math.max
import kotlin.random.Random

class GameState constructor(
  val nodes: MutableList<Node>,
  initialActiveNode: Node,
  initialId: Int = 1,
  initialActiveNodeMinus: Boolean = false,
) {

  val bestPattern: List<Pair<NodeElement, NodeElement>>? = findBestPattern(nodes)
  var activeNode: Node = initialActiveNode

  val allPluses: List<NodeAction> = getPluses()

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
    val leftNodeId = gameRequest.leftNodeId
    val dispatchIndex = if (nodes.size > 0 && leftNodeId != null) radialNodeIndexById(leftNodeId) + 1 else 0
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

  fun findMaxNode(): NodeElement? {
    return nodes.filterIsInstance(NodeElement::class.java).maxByOrNull { nodeElement ->
      nodeElement.element.atomicMass
    }
  }

  private fun executePatterns(): List<Pair<RequestResultPart, GameState>> {
    val states = mutableListOf<Pair<RequestResultPart, GameState>>()
    val pluses: List<NodeAction> = getPluses()
    pluses.forEach { plus ->
      val pattern: MutableList<Pair<NodeElement, NodeElement>> = ArrayList(findRepetitivePattern(plus))

      var mergeNode: Node = plus

      while (pattern.isNotEmpty()) {

        val patternStep = pattern.removeFirst()
        val patternStepNode1: NodeElement = patternStep.first
        val patternStepNode2 = patternStep.second

        Score += countScore(patternStepNode1, patternStepNode2)

        val newNodeElement = merge(patternStepNode1, mergeNode)

        val nodesToRemove = listOf(mergeNode, patternStepNode1, patternStepNode2)
        val startRemoveIndex = nodesToRemove.map { nodes.indexOf(it) }.filter { it >= 0 }.minOf { it }
        nodes.removeAll(nodesToRemove)
        nodes.add(startRemoveIndex, newNodeElement)

        val mergeResult = RequestResultPart.Merge(
          nodeId1 = patternStepNode1.id,
          nodeId2 = patternStepNode2.id,
          preMergeId = mergeNode.id,
          postMergeId = newNodeElement.id,
        )
        mergeNode = newNodeElement
        states.add(mergeResult to clone())

      }
    }
    return states
  }

  private fun countScore(firstElement: NodeElement, secondElement: NodeElement): Int {
    return firstElement.element.atomicMass + secondElement.element.atomicMass
  }

  private fun createNewActiveNode(): Node {
    return if (Random.nextFloat() > 0.3f) {

      val maxNode: NodeElement = findMaxNode()!!
      val diapasonEnd = maxNode.element.atomicMass
      val diapasonStart = max(diapasonEnd - 10, 1)
      val diapason: List<Int> = (diapasonStart until diapasonEnd).toList()

      val diapasonAdvanced: List<Int> = diapason.flatMap { diapasonMember ->
        (0 until diapasonEnd - diapasonMember).map { diapasonMember }
      }

      val mass = diapasonAdvanced.random()

      NodeElement(element = Element(mass), getIdAndInc())
    } else {
      NodeAction(action = listOf(Action.PLUS, Action.MINUS).random(), getIdAndInc())
    }
  }

  private fun getPluses(): List<NodeAction> {
    return nodes.filterIsInstance(NodeAction::class.java).filter { nodeAction -> nodeAction.action == Action.PLUS }
  }

  private fun merge(patternStepNode: NodeElement, mergeNode: Node): NodeElement {

    return if (mergeNode is NodeElement) {
      if (mergeNode.element.atomicMass > patternStepNode.element.atomicMass) {
        NodeElement(
          element = Element(mergeNode.element.atomicMass + 1),
          id = getIdAndInc()
        )
      } else {
        // add more atomic mass
        NodeElement(
          element = Element(patternStepNode.element.atomicMass + mergeNode.element.atomicMass),
          id = getIdAndInc()
        )
      }
    } else {
      NodeElement(
        element = Element(patternStepNode.element.atomicMass + 1),
        id = getIdAndInc()
      )
    }
  }

  private fun getIdAndInc(): Int {
    val idToReturn = nextId++
    Log.d("lol", "$idToReturn")
    return idToReturn
  }

  private fun findBestPattern(nodes: List<Node>): List<Pair<NodeElement, NodeElement>>? {
    val patterns = nodes.mapIndexed { leftIndex, node ->
      val rightNodeIndex = findRightIndex(leftIndex)
      findRepetitivePattern(leftIndex, rightNodeIndex)
    }.maxByOrNull { it.size }
    if (patterns != null) {
      if (patterns.isNotEmpty())
        return patterns
    }
    return null
  }

  /**
   * Найти повторяющийся паттерн вокруг плюсика.
   */
  private fun findRepetitivePattern(plus: NodeAction): List<Pair<NodeElement, NodeElement>> {
    val plusPosition = nodes.indexOf(plus)
    val leftNodeIndex = findLeftIndex(plusPosition)
    val rightNodeIndex = findRightIndex(plusPosition)

    return findRepetitivePattern(leftNodeIndex, rightNodeIndex)
  }

  private fun findRepetitivePattern(
    initialLeftNodeIndex: Int,
    initialRightNodeIndex: Int,
  ): List<Pair<NodeElement, NodeElement>> {
    var leftNodeIndex = initialLeftNodeIndex
    var rightNodeIndex = initialRightNodeIndex

    val patternSteps = mutableListOf<Pair<NodeElement, NodeElement>>()
    while (true) {
      if (leftNodeIndex == rightNodeIndex) break

      val leftNode: NodeElement = nodes.getOrNull(leftNodeIndex) as? NodeElement ?: break
      val rightNode: NodeElement = nodes.getOrNull(rightNodeIndex) as? NodeElement ?: break

      // Проверить, не пытаемся ли мы добавить ноды в паттерн повторно. Если пытаемся, значит паттерн закончился.
      val nodesAreAlreadyInPattern = patternSteps.flatMap { listOf(it.first, it.second) }.any { it == leftNode || it == rightNode }
      if (nodesAreAlreadyInPattern) break

      // Если атомные массы нод различаются, значит паттерн закончился
      if (leftNode.element.atomicMass != rightNode.element.atomicMass) break

      patternSteps.add(leftNode to rightNode)
      rightNodeIndex = findRightIndex(rightNodeIndex)
      leftNodeIndex = findLeftIndex(leftNodeIndex)
    }
    return patternSteps
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
      initialId = gameState.nextId,
      initialActiveNodeMinus = gameState.prevActiveNodeMinus
    )
  }

  override fun toString(): String {
    return "Nodes amount: ${nodes.size}, activeNode = $activeNode"
  }

  companion object {
    const val MAX_ELEM_COUNT = 20
    var Score = 0


    fun createGame(): GameState {
      var id = 1
      Score = 0
      val random = Random(System.currentTimeMillis())
      val nodes = (0 until 6).map {
        NodeElement(element = Element(random.nextInt(1, 5)), id++)
      }
      return GameState(
        nodes = nodes.toMutableList(),
        initialActiveNode = NodeAction(action = listOf(Action.PLUS, Action.MINUS).random(random), id++),
        initialId = id
      )
    }
  }
}