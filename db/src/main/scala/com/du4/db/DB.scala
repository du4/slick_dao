package com.du4.db

import slick.jdbc.JdbcProfile


trait DB {
  val config: JdbcProfile
  def db: config.backend.Database
}

trait PostgresDB extends DB {
  override lazy val config: JdbcProfile = slick.jdbc.PostgresProfile
  override lazy val db: config.backend.Database = config.api.Database.forConfig("postgres")
}
