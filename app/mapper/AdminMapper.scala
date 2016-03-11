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

  private val dbConfig = dbConfigProvider.get[JdbcProfile]


}