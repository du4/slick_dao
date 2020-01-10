package com.du4.db

import org.scalatest.BeforeAndAfterEach

import scala.concurrent.Await
import scala.concurrent.duration._

trait DatabaseOnEachTestCleaner {
  this: BeforeAndAfterEach with DB =>

  import config.api._

  def schema: List[config.DDL]

  override protected def beforeEach(): Unit = {
    dropTables()
    createTables()
  }

  override protected def afterEach(): Unit = {
    dropTables()
  }

  private def createTables(): Unit = {
    schema.foreach { s =>
      try Await.result(db.run(s.createIfNotExists), 5.minutes) catch {
        case _: Throwable =>
      }
    }
  }

  private def dropTables(): Unit = {
    schema.foreach { s =>
      try Await.result(db.run(s.dropIfExists), 5.minutes) catch {
        case _: Throwable =>
      }
    }
  }
}
