package com.du4

import com.du4.db.DB
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.{BeforeAndAfterEach, Matchers}
import slick.lifted.AbstractTable

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

trait DatasetRunner {
  this: DB =>

  import config.api._

  def runActions[E <: Effect](actions: DBIOAction[_, NoStream, E]*): Unit = {
    val dbio = DBIO.seq(actions: _*)
    Await.result(db.run(dbio), 20.seconds)
  }
}

trait TableImplicits {
  this: DB with Matchers with ScalaFutures =>

  import config.api._

  implicit class TableOps[A <: AbstractTable[_]](table: TableQuery[A]) {
    def containsAll(expected: List[A#TableElementType]) = {
      val actual =
        db.run {
          table.result
        }.futureValue.toList

      actual should contain theSameElementsAs expected
    }

    def contains(expected: A#TableElementType) = {
      val actual =
        db.run {
          table.result
        }.futureValue.toList

      actual should contain(expected)
    }

    def containsOnly(expected: A#TableElementType) = {
      val actual =
        db.run {
          table.result
        }.futureValue.toList

      actual should contain only expected
    }

    def containsOnlyAll(expected: List[A#TableElementType]) = {
      val actual =
        db.run {
          table.result
        }.futureValue.toList

      actual should contain only (expected: _*)
    }

    def notContains(expected: A#TableElementType) = {
      val actual =
        db.run {
          table.result
        }.futureValue.toList

      actual shouldNot contain(expected)
    }

    def notContainsAll(expected: List[A#TableElementType]) = {
      val actual =
        db.run {
          table.result
        }.futureValue.toList

      actual shouldNot contain theSameElementsAs expected
    }

    def size(expected: Int) = {
      val actual =
        db.run {
          table.result
        }.futureValue.size

      actual shouldBe expected
    }
  }
}
