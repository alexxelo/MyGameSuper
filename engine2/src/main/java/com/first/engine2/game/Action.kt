package com.first.engine2.game

enum class Action constructor(val machineName:String){
    PLUS("plus"),
    MINUS("minus"),
    BLACK_PLUS("black_plus"),
    SPHERE("sphere"),
    ANTIMATTER("Antimatter");

    companion object{
        fun from(machineName: String):Action?{
            return values().find {
                it.machineName == machineName
            }
        }
    }
}