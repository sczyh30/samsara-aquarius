package service

import entity.User
import mapper.Tables.UserTable
import play.api.Play
import play.api.db.slick.DatabaseConfigProvider
import slick.driver.JdbcProfile
import slick.lifted.TableQuery

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Samsara Aquarius
  * User Service
  */
object UserService {

  val users = TableQuery[UserTable]

  private val dbConfig = DatabaseConfigProvider.get[JdbcProfile](Play.current)

  import dbConfig._
  import driver.api._

  def add(user: User): Future[String] = {
    dbConfig.db.run(users += user) map { res =>
      "user_add_success"
    } recover {
      case ex: Exception => ex.getCause.getMessage
    }
  }
}
