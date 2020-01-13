package com.du4.main.dao

import com.du4.db.DB
import com.du4.tables.slick.generated.Tables._

import scala.concurrent.{ExecutionContext, Future}

trait DeviceInfoRepository extends DB {

  import config.api._

  implicit val ec: ExecutionContext

  def getAll(): Future[List[DeviceinfoRow]] = db.run(Deviceinfo.result).map(_.toList)

  def getOrInsert(deviceInfo: DeviceinfoRow): Future[DeviceinfoRow] = {
    val maybeInfo = Deviceinfo.filter(_.id === deviceInfo.id).result.headOption
    val insertInfo = Deviceinfo += deviceInfo

    val dbioDI: DBIO[DeviceinfoRow] = maybeInfo.flatMap {
      case Some(di) => DBIO.successful(di)
      case None => insertInfo.map(_ => deviceInfo)
    }

    db.run(dbioDI)
  }

}
