package mapper

import javax.inject.Inject

import play.api.db.slick.DatabaseConfigProvider

import scala.concurrent.ExecutionContext

/**
  * Admin Table Mapper
  */
class AdminMapper @Inject() (dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {

  import entity.Admin

  import slick.driver.JdbcProfile
  import slick.lifted.{Rep, Tag}
  import slick.driver.MySQLDriver.api._

  private val dbConfig = dbConfigProvider.get[JdbcProfile]


}