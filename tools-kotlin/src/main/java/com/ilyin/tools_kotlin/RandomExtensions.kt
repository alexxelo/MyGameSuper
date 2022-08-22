package com.ilyin.tools_kotlin

import kotlin.math.roundToInt
import kotlin.math.roundToLong
import kotlin.random.Random

//fun Random.betweenFloat(num1: Float, num2: Float): Float {
//  val min = minOf(num1, num2)
//  val max = maxOf(num1, num2)
//  return min + nextFloat() * (max - min)
//}
//
//fun Random.betweenFloat(num1: Double, num2: Double): Float {
//  val min = minOf(num1, num2)
//  val max = maxOf(num1, num2)
//  return (min + nextFloat() * (max - min)).toFloat()
//}
//
//fun Random.betweenFloat(num1: Int, num2: Int): Float {
//  val min = minOf(num1, num2)
//  val max = maxOf(num1, num2)
//  return min + nextFloat() * (max - min)
//}
//
//fun Random.betweenInt(num1: Int, num2: Int): Int {
//  val min = minOf(num1, num2)
//  val max = maxOf(num1, num2)
//  return (min + nextFloat() * (max - min)).roundToInt()
//}
//
//fun Random.betweenLong(num1: Int, num2: Int): Long {
//  val min = minOf(num1, num2)
//  val max = maxOf(num1, num2)
//  return (min + nextFloat() * (max - min)).roundToLong()
//}
//
//fun Random.betweenLong(num1: Long, num2: Long): Long {
//  val min = minOf(num1, num2)
//  val max = maxOf(num1, num2)
//  return (min + nextFloat() * (max - min)).roundToLong()
//}
//
//fun Random.flipCoin(trueChance: Float = 0.5f): Boolean {
//  return nextFloat() < trueChance
//}