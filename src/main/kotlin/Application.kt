package dev.hennie

import io.ktor.server.application.*

fun main(args: Array<String>) {
    io.ktor.server.cio.EngineMain.main(args)
}

fun Application.module() {
    configureHTTP()
    configureSecurity()
    configureMonitoring()
    configureSerialization()
    configureDatabases(environment.config)
    configureFrameworks()
    configureSockets()
    configureAdministration()
    configureRouting()
}
