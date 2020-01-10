package com.du4.main.dao

import com.du4.db.DB
import com.du4.tables.slick.generated.Tables._

import scala.concurrent.{ExecutionContext, Future}

trait DeviceInfoRepository extends DB {

  import config.api._

  implicit val ec: ExecutionContext

  def getAll(): Future[List[DeviceinfoRow]] = db.run(Deviceinfo.result).map(_.toList)

}
