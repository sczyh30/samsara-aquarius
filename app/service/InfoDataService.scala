package service

import entity.InfoData
import mapper.Tables.InfoDataTable
import play.api.Play
import play.api.db.slick.DatabaseConfigProvider
import slick.driver.JdbcProfile
import slick.lifted.TableQuery

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Samsara Aquarius
  * Info Data Service
  */
object InfoDataService {

  val infoAll = TableQuery[InfoDataTable]

  private val dbConfig = DatabaseConfigProvider.get[JdbcProfile](Play.current)

  import dbConfig._
  import driver.api._

  def addInfo(info: InfoData): Future[String] = {
    dbConfig.db.run(infoAll += info) map { res =>
      "info_data_add_success"
    } recover {
      case ex: Exception => ex.getCause.getMessage
    }
  }

  def getInfo(id: Int): Future[Option[InfoData]] = {
    dbConfig.db.run(infoAll.filter(_.id === id).result.headOption)
  }

  def getAll: Future[Seq[InfoData]] = {
    dbConfig.db.run(infoAll.result)
  }

  def remove(id: Int): Future[Int] = {
    dbConfig.db.run(infoAll.filter(_.id === id).delete)
  }

}
