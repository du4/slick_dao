package com.du4.main.dao

import com.du4.db.DB
import com.du4.tables.slick.generated.Tables.DeviceinfoRow

import scala.concurrent.Future

trait DeviceInfoRepository extends DB {

  def getAll(): Future[List[DeviceinfoRow]] = ???

}
