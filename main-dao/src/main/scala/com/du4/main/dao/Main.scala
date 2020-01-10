package com.du4.main.dao

import java.util.concurrent.Executors

import com.du4.db.PostgresDB

import scala.concurrent.{Await, ExecutionContext}
import concurrent.duration._
import language.postfixOps

object Main extends App {

  val deviceInfoRepo = new DeviceInfoRepository with PostgresDB {
    override implicit val ec: ExecutionContext = ExecutionContext.fromExecutor(Executors.newWorkStealingPool())
  }

  val resF = deviceInfoRepo.getAll();

  val res = Await.result(resF, 10 seconds)

  res.foreach(println)
}
