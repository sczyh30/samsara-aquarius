package service

import javax.inject.{Singleton, Inject}

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
@Singleton
class InfoDataService @Inject()(protected val dbConfigProvider: DatabaseConfigProvider) extends HasDatabaseConfigProvider[JdbcProfile] {

  import driver.api._

  val InfoAll = TableQuery[InfoDataTable]

  def addInfo(info: InfoData): Future[String] = {
    db.run(InfoAll += info) map { res =>
      "info_data_add_success"
    } recover {
      case ex: Exception => ex.getCause.getMessage
    }
  }

  def fetch(id: Int): Future[Option[InfoData]] = {
    db.run(InfoAll.filter(_.id === id).result.headOption)
  }

  def fetchAll: Future[Seq[InfoData]] = {
    db.run(InfoAll.result)
  }

  def update(info: InfoData): Future[Int] = {
    db.run(InfoAll.filter(_.id === info.id).update(info))
  }

  def remove(id: Int): Future[Int] = {
    db.run(InfoAll.filter(_.id === id).delete)
  }

}
