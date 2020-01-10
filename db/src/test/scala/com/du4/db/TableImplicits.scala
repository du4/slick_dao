package com.du4.db

import org.scalatest.Matchers
import org.scalatest.concurrent.ScalaFutures
import slick.lifted.AbstractTable


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
