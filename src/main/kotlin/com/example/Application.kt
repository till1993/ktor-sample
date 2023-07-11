package com.example

import com.example.plugins.*
import com.github.victools.jsonschema.generator.*
import io.github.smiley4.ktorswaggerui.SwaggerUI
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import kotlinx.serialization.json.Json
import kotlin.reflect.javaType

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    configureRouting()
    configureSwagger()
    configureContentNegotiation()

}

fun Application.configureContentNegotiation() {
    install(ContentNegotiation) {
        json(Json {
            ignoreUnknownKeys = true
        })
    }
}

private val config: SchemaGeneratorConfig =
    SchemaGeneratorConfigBuilder(SchemaVersion.DRAFT_2019_09, OptionPreset.PLAIN_JSON)
        .without(Option.DEFINITIONS_FOR_ALL_OBJECTS)
        .with(Option.INLINE_ALL_SCHEMAS)
        .with(Option.EXTRA_OPEN_API_FORMAT_VALUES)
        .with(Option.ALLOF_CLEANUP_AT_THE_END)
        .build()
private val generator = SchemaGenerator(config)

@OptIn(ExperimentalStdlibApi::class)
fun Application.configureSwagger() {
    install(SwaggerUI) {
        encoding {
            schemaEncoder { type ->
                generator.generateSchema(type.javaType).toString()
            }
        }

        server {
            url = "/"
            description = "Actual Server"
        }
    }
}


