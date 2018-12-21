package com.hexagonkt.http.server

import com.hexagonkt.http.client.Client
import com.hexagonkt.settings.SettingsManager
import java.net.InetAddress.getByName as address

abstract class EngineTest(serverAdapter: ServerPort) {
    protected val server: Server = Server(serverAdapter, Router(), SettingsManager.settings)
    protected val client by lazy { Client("http://localhost:${server.runtimePort}") }

    private val modules: List<TestModule> by lazy {
        listOf(
            BooksModule(),
            CookiesModule(),
            GenericModule(),
            HexagonModule(),
            SessionModule()
        )
    }

    fun startServers () {
        modules.map { server.router.path(it.initialize()) }
        server.run ()
    }

    fun stopServers () {
        server.stop()
    }

    fun validate() {
        modules.forEach { it.validate(client) }
    }
}
