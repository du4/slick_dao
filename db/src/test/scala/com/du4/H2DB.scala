package com.du4

import com.du4.db.DB
import com.typesafe.config.ConfigFactory
import slick.jdbc.JdbcProfile

import scala.concurrent.ExecutionContext


trait H2DB extends DB {
  override lazy val config: JdbcProfile = slick.jdbc.H2Profile
  override lazy val db: config.backend.Database = config.api.Database.forConfig("h2mem1", ConfigFactory.load("test.conf"))

  implicit val ec: ExecutionContext = ExecutionContext.global
}
