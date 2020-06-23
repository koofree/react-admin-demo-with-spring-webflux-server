package com.example.demo

import com.example.demo.domain.Hello
import com.example.demo.domain.HelloRepository
import org.springframework.data.domain.PageRequest
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono

@RestController
@RequestMapping("/hello")
class HelloController(val helloRepository: HelloRepository) {

    @GetMapping
    fun get(
        pageFilter: PageFilter
    ): Mono<ResponseEntity<List<Hello>>> {

        val page = helloRepository
            .findAll(PageRequest.of(pageFilter.offset / pageFilter.size, pageFilter.size))

        val size = page.totalElements
        val content = page.content

        return (content to size)
            .toResponseEntity()
            .toMono()
    }

    @PostMapping
    fun post(@RequestBody hello: Hello): Mono<Hello> {
        return helloRepository.save(hello)
            .toMono()
    }

    @GetMapping("{id}")
    fun getOne(@PathVariable("id") id: Long): Mono<Hello> {
        return helloRepository
            .getOne(id)
            .toMono()
    }

    @PutMapping(value = ["{id}"])
    fun put(@PathVariable("id") id: Long, @RequestBody hello: Hello): Mono<Hello> {
        val exists = helloRepository
            .getOne(id)
        exists.message = hello.message

        return helloRepository.save(exists)
            .toMono()
    }

    @DeleteMapping("{id}")
    fun delete(@PathVariable("id") id: Long): Mono<Boolean> {
        helloRepository.deleteById(id)
        return true.toMono()
    }
}