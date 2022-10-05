package com.example.engine2.game

enum class Action constructor(val machineName:String){
    PLUS("plus"),
    MINUS("minus"),
    BLACK_PLUS("black_plus"),
    SPHERE("sphere");

    companion object{
        fun from(machineName: String):Action?{
            return Action.values().find {
                it.machineName == machineName
            }
        }
    }
}