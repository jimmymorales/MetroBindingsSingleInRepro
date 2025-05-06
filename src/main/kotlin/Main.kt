package com.example

import dev.zacsweers.metro.createGraph

class Platform {
    val name: String = "Java ${System.getProperty("java.version")}"
}

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
fun main() {
    val graph = createGraph<ExampleDependencyGraph>()
    val platform1 = graph.controller.platform
    val platform2 = (graph.controller.authFactory.create(Authenticator.Type.PhoneNumber) as PhoneAuthenticator)
        .dep.platform
    println(platform1)
    println(platform2)

    check(platform1 === platform2)
}