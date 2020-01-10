
package com.du4.db

import scala.concurrent.Await
import concurrent.duration._

trait DatasetRunner {
  this: DB =>

  import config.api._

  def runActions[E <: Effect](actions: DBIOAction[_, NoStream, E]*): Unit = {
    val dbio = DBIO.seq(actions: _*)
    Await.result(db.run(dbio), 20.seconds)
  }
}
