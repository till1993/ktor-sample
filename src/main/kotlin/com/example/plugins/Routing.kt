package com.example.plugins

import io.ktor.http.*
import io.ktor.server.routing.*
import io.ktor.server.response.*
import io.ktor.server.application.*
import kotlinx.serialization.Serializable
import io.github.smiley4.ktorswaggerui.dsl.*

@Serializable
data class Cat(
    val id: Long,
    val name: String
)

fun Application.configureRouting() {
    routing {
        route(path = "/v1/cats", {
            tags = listOf("cat")
        }) {
            get({
                request {
                }
                response {
                    HttpStatusCode.OK to { description = ""; body<List<Cat>> { } }
                }
            }) {
                val cats = listOf(
                    Cat(1, "Felix"),
                    Cat(2, "Garfield")
                )
                call.respond(status = HttpStatusCode.OK, message = cats)
            }
        }
    }
}
