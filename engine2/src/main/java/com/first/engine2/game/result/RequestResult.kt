package com.first.engine2.game.result

data class RequestResult constructor(val parts: List<RequestResultPart>) {

    constructor(resultPart: RequestResultPart): this(listOf(resultPart))
}