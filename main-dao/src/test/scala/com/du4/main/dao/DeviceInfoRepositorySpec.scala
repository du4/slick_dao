package com.du4.main.dao

import com.du4.db.{DatabaseOnEachTestCleaner, DatasetRunner, H2DB, TableImplicits}
import com.du4.tables.slick.generated.Tables._
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.{BeforeAndAfterEach, FreeSpecLike, Matchers}
import concurrent.duration._
import language.postfixOps

class DeviceInfoRepositorySpec extends Matchers
  with FreeSpecLike
  with ScalaFutures
  with H2DB
  with BeforeAndAfterEach
  with DatabaseOnEachTestCleaner
  with DatasetRunner
  with TableImplicits {

  import config.api._

  override implicit def patienceConfig: PatienceConfig = PatienceConfig(10 seconds, 50 millis)

  override def schema: List[config.DDL] = List(Deviceinfo.schema)

  sealed trait Fixtures {
    val deviceInfo1 = DeviceinfoRow(1L, Some(345), None, Some("ip1"), Some(2.5f), Some(30.5f))
    val deviceInfo2 = DeviceinfoRow(2L, Some(345), None, Some("ip2"), Some(3.5f), Some(40.5f))
    val deviceInfo3 = DeviceinfoRow(3L, Some(347), None, Some("ip3"), Some(4.5f), Some(50.5f))

    runActions(
      Deviceinfo.forceInsertAll(Seq(
        deviceInfo1, deviceInfo2, deviceInfo3
      ))
    )
  }

  sealed trait TestContext {
    val repo = new DeviceInfoRepository with H2DB
  }

  "DeviceInfo must" - {
    "getAll infos" in new TestContext with Fixtures {
      repo.getAll().futureValue shouldBe List(
        deviceInfo1,
        deviceInfo2,
        deviceInfo3
      )

      runActions(Deviceinfo.delete)

      repo.getAll().futureValue shouldBe List()
    }

    "getOrInsert deviceInfo by returning existing info if it is already exits" in new TestContext with Fixtures {
      val di = DeviceinfoRow(3L, Some(500))

      repo.getOrInsert(di).futureValue shouldBe deviceInfo3

      Deviceinfo.containsOnlyAll(List(deviceInfo1, deviceInfo2, deviceInfo3))
    }

    "getOrInsert deviceInfo by inserting passed info if it does not exist yet" in new TestContext with Fixtures {
      val di = DeviceinfoRow(4L, Some(500))

      repo.getOrInsert(di).futureValue shouldBe DeviceinfoRow(4L, Some(500))

      Deviceinfo.containsOnlyAll(List(deviceInfo1, deviceInfo2, deviceInfo3, di))
    }
  }

}
