package com.example.hello.impl

import com.lightbend.lagom.scaladsl.server.LocalServiceLocator
import com.lightbend.lagom.scaladsl.testkit.ServiceTest
import com.example.hello.api._
import utest._

object HelloServiceTests extends TestSuite {

  private val server = ServiceTest.startServer(
    ServiceTest.defaultSetup
      .withCassandra()
  ) { ctx =>
    new HelloApplication(ctx) with LocalServiceLocator
  }

  val client = server.serviceClient.implement[HelloService]

  override def utestAfterAll(): Unit = server.stop()

  import server.executionContext

  override def tests = Tests {
    "say hello" - {
      client.hello("Alice").invoke().map { answer =>
        assert(answer == "Hello, Alice!")
      }
    }
  }
}
