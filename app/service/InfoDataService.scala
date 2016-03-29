package service

import javax.inject.Inject

import entity.InfoData
import mapper.Tables.InfoDataTable

import play.api.db.slick.{HasDatabaseConfigProvider, DatabaseConfigProvider}
import slick.driver.JdbcProfile

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Samsara Aquarius
  * Info Data Service
  * @author sczyh30
  */
class InfoDataService @Inject()(protected val dbConfigProvider: DatabaseConfigProvider) extends HasDatabaseConfigProvider[JdbcProfile] {

  import driver.api._

  val infoAll = TableQuery[InfoDataTable]

  def addInfo(info: InfoData): Future[String] = {
    db.run(infoAll += info) map { res =>
      "info_data_add_success"
    } recover {
      case ex: Exception => ex.getCause.getMessage
    }
  }

  def getInfo(id: Int): Future[Option[InfoData]] = {
    db.run(infoAll.filter(_.id === id).result.headOption)
  }

  def getAll: Future[Seq[InfoData]] = {
    db.run(infoAll.result)
  }

  def remove(id: Int): Future[Int] = {
    db.run(infoAll.filter(_.id === id).delete)
  }

}
