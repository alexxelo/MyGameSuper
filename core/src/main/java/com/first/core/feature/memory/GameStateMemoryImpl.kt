package com.first.core.feature.memory

import android.content.Context
import com.first.engine2.game.Action
import com.first.engine2.game.Element
import com.first.engine2.game.state.GameState
import com.first.engine2.node.Node
import com.first.engine2.node.NodeAction
import com.first.engine2.node.NodeElement
import com.ilyin.tools_android.crud.PreferencesWrapper
import com.ilyin.tools_android.util.map
import com.ilyin.tools_android.util.prefs
import org.json.JSONArray
import org.json.JSONObject

class GameStateMemoryImpl(context: Context) : GameStateMemory, PreferencesWrapper(context.prefs("GameStateMemoryImpl")) {
  override var gameState: GameState?
    get() = getState()
    set(value) = saveState(value)


  private fun getState(): GameState? {
    val jsonAsString = getStringNullable(PREF_GAME_STATE)
    return if (jsonAsString != null) {
      val json = JSONObject(jsonAsString)
      fromJSON(json)
    } else {
      null
    }
  }

  private fun saveState(gameState: GameState?) {
    val gs = gameState?.toJSON()
    val value = gs?.toString()
    save(PREF_GAME_STATE, value)
  }

  private fun GameState.toJSON(): JSONObject {
    val jsArr = JSONArray()
    nodes.forEach {
      val node = it.toJSON()
      jsArr.put(node)
    }
    return JSONObject().apply {
      put(KEY_LAST_ID, nextId)
      put(KEY_PREV_ACTIVE_NODE_MINUS, prevActiveNodeMinus)
      put(KEY_MAX_NODE, recordAtomicMass)
      put(KEY_GAME_SCORE, gameScore)
      put(KEY_ACTIVE_NODE, activeNode.toJSON())
      put(KEY_NODES, jsArr)

    }
  }

  private fun Node.toJSON(): JSONObject {
    return when (this) {
      is NodeAction -> toJSON()
      is NodeElement -> toJSON()
    }
  }

  private fun NodeElement.toJSON(): JSONObject {
    return JSONObject().apply {
      put(KEY_ATOMIC_MASS, element.atomicMass)
      put(KEY_ID, id)
    }
  }

  private fun NodeAction.toJSON(): JSONObject {
    return JSONObject().apply {
      put(KEY_ACTION, action.machineName)
      put(KEY_ID, id)

    }
  }

  private fun fromJSON(gameStateAsJSON: JSONObject): GameState? {
    val lastId = gameStateAsJSON.optInt(KEY_LAST_ID)
    val prevActiveNodeMinus = gameStateAsJSON.optBoolean(KEY_PREV_ACTIVE_NODE_MINUS)
    val maxNode = gameStateAsJSON.optInt(KEY_MAX_NODE)
    val gameScore = gameStateAsJSON.optInt(KEY_GAME_SCORE)
    val activeNode = gameStateAsJSON.optJSONObject(KEY_ACTIVE_NODE)?.let {
      fromJSONNode(it)
    }

    val allNodes = gameStateAsJSON.optJSONArray(KEY_NODES)?.map { i, jsonArray ->
      fromJSONNode(jsonArray.getJSONObject(i))
    } ?: listOf()

    return if (activeNode != null) {
      GameState(
        nodes = allNodes.toMutableList(),
        initialActiveNode = activeNode,
        initialId = lastId,
        initialActiveNodeMinus = prevActiveNodeMinus,
        initialRecordAtomicMass = maxNode,
        initialGameScore = gameScore
      )
    } else {
      null
    }
  }

  private fun fromJSONNode(nodeStateAsJSON: JSONObject): Node {
    return if (nodeStateAsJSON.has(KEY_ATOMIC_MASS)) {
      NodeElement(
        element = Element(
          nodeStateAsJSON.getInt(KEY_ATOMIC_MASS)
        ),
        id = nodeStateAsJSON.getInt(KEY_ID)
      )
    } else {
      val machineName = nodeStateAsJSON.optString(KEY_ACTION)
      NodeAction(
        action = Action.from(machineName)!!,
        id = nodeStateAsJSON.getInt(KEY_ID)
      )
    }
  }

  companion object {
    const val KEY_LAST_ID = "KEY_LAST_ID"
    const val PREF_GAME_STATE = "PREF_GAME_STATE"
    const val KEY_PREV_ACTIVE_NODE_MINUS = "KEY_PREV_ACTIVE_NODE_MINUS"
    const val KEY_ATOMIC_MASS = "KEY_ATOMIC_MASS"
    const val KEY_ID = "KEY_ID"
    const val KEY_ACTION = "KEY_ACTION"
    const val KEY_ACTIVE_NODE = "KEY_ACTIVE_NODE"
    const val KEY_NODES = "KEY_NODES"
    const val KEY_MAX_NODE = "KEY_MAX_NODE"
    const val KEY_GAME_SCORE = "KEY_GAME_SCORE"
  }

}