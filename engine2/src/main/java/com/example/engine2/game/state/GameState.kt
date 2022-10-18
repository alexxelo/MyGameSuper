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
  initialRecordAtomicMass: Int = findMaxNode(nodes)?.element?.atomicMass ?: 0,
  initialGameScore: Int = 0
) {

  val bestPattern: List<Pair<NodeElement, NodeElement>>? = findBestPattern(nodes)
  var activeNode: Node = initialActiveNode

  var recordAtomicMass: Int = initialRecordAtomicMass

  var gameScore: Int = initialGameScore
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
      is GameRequest.CopyWithSphere -> {
        prevActiveNodeMinus = false
        copyNode(gameRequest)
        listOf(RequestResultPart.DoNothing to clone())
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
      invalidateRecord()

    }
  }

  private fun copyNode(gameRequest: GameRequest.CopyWithSphere) {
    val clickedNode = nodes.find { it.id == gameRequest.nodeId }
    if (clickedNode !== null) {
      activeNode = when (clickedNode) {
        is NodeAction -> NodeAction(clickedNode.action, getIdAndInc())
        is NodeElement -> NodeElement(Element(clickedNode.element.atomicMass), getIdAndInc())
      }
    }
  }

  private fun invalidateRecord() {
    val newRecord = findMaxNode()?.element?.atomicMass ?: 0
    if (recordAtomicMass < newRecord) {
      recordAtomicMass = newRecord
    }
  }

  private fun turnMinusToPlus() {
    activeNode = NodeAction(Action.PLUS, getIdAndInc())
    prevActiveNodeMinus = false
  }

  // ищет ноду в списке нод айди которой передали и возвращает
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
    invalidateRecord()
    activeNode = newActiveNode
    return RequestResultPart.Dispatch(
      dispatchedNodeId = oldActiveNode.id,
      leftNodeId = nodes[findLeftIndex(dispatchIndex)].id,
      rightNodeId = nodes[findRightIndex(dispatchIndex)].id,
      newActiveNodeId = newActiveNode.id,
    )
  }

  fun findMaxNode(): NodeElement? {
    return Companion.findMaxNode(nodes)
  }

  private fun executePatterns(): List<Pair<RequestResultPart, GameState>> {
    val states: MutableList<Pair<RequestResultPart, GameState>> = mutableListOf<Pair<RequestResultPart, GameState>>()

    val antimatter = getaAntimatter()
    executePattern(antimatter,states)

    val pluses = getPluses()
    executePattern(pluses,states)

    val blackPluses = getBlackPluses()
    executePattern(blackPluses,states)

    return states
  }

  private fun executePattern(action: List<NodeAction>, states: MutableList<Pair<RequestResultPart, GameState>>):MutableList<Pair<RequestResultPart, GameState>>{
    action.forEach { act ->
      val pattern: MutableList<Pair<NodeElement, NodeElement>> = ArrayList(findRepetitivePattern(act))

      var mergeNode: Node = act

      while (pattern.isNotEmpty()) {

        val patternStep = pattern.removeFirst()
        val patternStepNode1: NodeElement = patternStep.first
        val patternStepNode2 = patternStep.second

        gameScore += countScore(patternStepNode1, patternStepNode2)

        val newNodeElement = merge(patternStepNode1, mergeNode)

        val nodesToRemove = listOf(mergeNode, patternStepNode1, patternStepNode2)
        val startRemoveIndex = nodesToRemove.map { nodes.indexOf(it) }.filter { it >= 0 }.minOf { it }
        nodes.removeAll(nodesToRemove)
        nodes.add(startRemoveIndex, newNodeElement)
        invalidateRecord()

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
    return if (Random.nextFloat() > 0.88f) {
      if (gameScore > 1000) {
        NodeAction(action = listOf(Action.PLUS, Action.MINUS, Action.BLACK_PLUS, Action.SPHERE).random(), getIdAndInc())
      } else {
        NodeAction(action = listOf(Action.PLUS, Action.MINUS).random(), getIdAndInc())
      }
    } else if (Random.nextFloat() > 0.99f) {
      NodeAction(action = Action.BLACK_PLUS, getIdAndInc())
    } else if (Random.nextFloat() > 0.7f && nodes.size > 10) {
      NodeAction(action = Action.ANTIMATTER, getIdAndInc())
    } else {
      val diapasonEnd = recordAtomicMass
      val diapasonStart = max(diapasonEnd - 10, 1)
      val diapason: List<Int> = (diapasonStart until diapasonEnd).toList()

      val diapasonAdvanced: List<Int> = diapason.flatMap { diapasonMember ->
        (0 until diapasonEnd - diapasonMember).map { diapasonMember }
      }
      val mass = diapasonAdvanced.random()
      NodeElement(element = Element(mass), getIdAndInc())
    }
  }

  private fun getPluses(): List<NodeAction> {
    return nodes.filterIsInstance(NodeAction::class.java).filter { nodeAction -> nodeAction.action == Action.PLUS }
  }

  private fun getBlackPluses(): List<NodeAction> {
    return nodes.filterIsInstance(NodeAction::class.java).filter { nodeAction -> nodeAction.action == Action.BLACK_PLUS }
  }

  private fun getaAntimatter(): List<NodeAction> {
    return nodes.filterIsInstance(NodeAction::class.java).filter { nodeAction -> nodeAction.action == Action.ANTIMATTER }
  }

  // create new element after merge
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
      // first step when mergeNode = plus
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
    val patterns = nodes.mapIndexed { leftIndex, _ ->
      val rightNodeIndex = findRightIndex(leftIndex)
      findRepetitivePattern( false, leftIndex, rightNodeIndex)
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
  /*
  private fun findRepetitivePattern(plus: NodeAction): List<Pair<NodeElement, NodeElement>> {
    val plusPosition = nodes.indexOf(plus)
    val leftNodeIndex = findLeftIndex(plusPosition)
    val rightNodeIndex = findRightIndex(plusPosition)

    return indRepetitivePattern(leftNodeIndex, rightNodeIndex)
  }*/

  // передается плюс и по его позиции находят ноды вокруг и можно вызвать эту функцию когда есть антиматерия
  private fun findRepetitivePattern(plus: NodeAction): List<Pair<NodeElement, NodeElement>> {
    val plusPosition = nodes.indexOf(plus)
    val leftNodeIndex = findLeftIndex(plusPosition)
    val rightNodeIndex = findRightIndex(plusPosition)

    if (plus.action == Action.BLACK_PLUS) {
      return findRepetitivePattern(true, leftNodeIndex, rightNodeIndex)
    }
    if (plus.action == Action.ANTIMATTER) {
      return findAntimatterPattern(leftNodeIndex, rightNodeIndex)
    }
    return findRepetitivePattern(false, leftNodeIndex, rightNodeIndex)
  }

  private fun findAntimatterPattern(
    initialLeftNodeIndex: Int,
    initialRightNodeIndex: Int,
  ): List<Pair<NodeElement, NodeElement>> {
    var leftNodeIndex = initialLeftNodeIndex
    var rightNodeIndex = initialRightNodeIndex

    val patternSteps = mutableListOf<Pair<NodeElement, NodeElement>>()

    var size = nodes.size
    while (size > nodes.size / 2) {
      if (leftNodeIndex == rightNodeIndex) break

      val leftNode: NodeElement = nodes.getOrNull(leftNodeIndex) as? NodeElement ?: break
      val rightNode: NodeElement = nodes.getOrNull(rightNodeIndex) as? NodeElement ?: break

      // Проверить, не пытаемся ли мы добавить ноды в паттерн повторно. Если пытаемся, значит паттерн закончился.
      val nodesAreAlreadyInPattern = patternSteps.flatMap { listOf(it.first, it.second) }.any { it == leftNode || it == rightNode }
      if (nodesAreAlreadyInPattern) break

      patternSteps.add(leftNode to rightNode)
      rightNodeIndex = findRightIndex(rightNodeIndex)
      leftNodeIndex = findLeftIndex(leftNodeIndex)
      size -= 2
    }
    return patternSteps
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

  // overload for black plus
  private fun findRepetitivePattern(
    plusBlack: Boolean, // true = black / false = red
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

      // avoid in first step
      if (patternSteps.isNotEmpty() && plusBlack) {
        // Если атомные массы нод различаются, значит паттерн закончился
        if (leftNode.element.atomicMass != rightNode.element.atomicMass) break
      }
      if (!plusBlack) {
        if (leftNode.element.atomicMass != rightNode.element.atomicMass) break
      }

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
      initialActiveNodeMinus = gameState.prevActiveNodeMinus,
      initialRecordAtomicMass = gameState.recordAtomicMass,
      initialGameScore = gameState.gameScore
    )
  }

  override fun toString(): String {
    return "Nodes amount: ${nodes.size}, activeNode = $activeNode"
  }

  companion object {
    const val MAX_ELEM_COUNT = 20

    fun findMaxNode(nodes: List<Node>): NodeElement? {
      return nodes.filterIsInstance(NodeElement::class.java).maxByOrNull { nodeElement ->
        nodeElement.element.atomicMass
      }
    }

    fun createGame(): GameState {
      var id = 1
      val random = Random(System.currentTimeMillis())
      val nodes = (0 until 6).map {
        NodeElement(element = Element(random.nextInt(1, 5)), id++)
      }
      return GameState(
        nodes = nodes.toMutableList(),
        initialActiveNode = NodeAction(action = listOf(Action.PLUS, Action.MINUS).random(random), id++),
        initialId = id,
      )
    }
  }
}