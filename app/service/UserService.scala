package service

import javax.inject.Inject

import entity.User
import mapper.Tables.UserTable

import play.api.Play
import play.api.db.slick.{HasDatabaseConfigProvider, DatabaseConfigProvider}
import slick.driver.JdbcProfile

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Samsara Aquarius
  * User Service
  *
  * @author sczyh30
  */
class UserService @Inject()(protected val dbConfigProvider: DatabaseConfigProvider) extends HasDatabaseConfigProvider[JdbcProfile] {

  import driver.api._

  private val users = TableQuery[UserTable]

  /**
    * Add a user to database
    * @param user a user entity
    * @return the async status
    */
  def add(user: User): Future[String] = {
    db.run(users += user) map { res =>
      "user_add_success"
    } recover {
      case ex: Exception => ex.getCause.getMessage
    }
  }

  def getUser(uid: Int): Future[Option[User]] =
    dbConfig.db.run(users.filter(_.uid === uid).result.headOption)
}