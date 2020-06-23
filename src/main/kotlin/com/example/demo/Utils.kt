package com.example.demo

import org.springframework.http.ResponseEntity
import reactor.core.publisher.Mono

internal fun <T : Any, S : Number> Mono<Pair<T, S>>.toResponseEntity():
    Mono<ResponseEntity<T>> = map { it.toResponseEntity() }

internal fun <T : Any, S : Number> Pair<T, S>.toResponseEntity():
    ResponseEntity<T> = ResponseEntity
    .ok()
    .header("X-Total-Count", this.second.toString())
    .body(this.first)
