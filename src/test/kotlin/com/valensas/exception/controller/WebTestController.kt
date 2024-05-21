package com.valensas.exception.controller

import com.valensas.exception.model.TestModel
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/web")
class WebTestController {
    @GetMapping("/{pathVariable}")
    fun getWithHeaderAndPathVariable(
        @RequestHeader headerNumber: Long,
        @PathVariable pathVariable: UUID
    ) {}

    @GetMapping
    fun getWithRequestParam(
        @RequestParam param: UUID
    ) {}

    @PostMapping
    fun post(
        @RequestBody testModel: TestModel
    ) {}
}
