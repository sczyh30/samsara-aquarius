package service

import javax.inject.{Singleton, Inject}

import entity.User
import mapper.Tables.UserTable
import play.api.Logger

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
@Singleton
class UserService @Inject()(protected val dbConfigProvider: DatabaseConfigProvider) extends HasDatabaseConfigProvider[JdbcProfile] {

  private val logger = org.slf4j.LoggerFactory.getLogger(this.getClass)

  import driver.api._

  private val Users = TableQuery[UserTable]

  private val queryByUid = Compiled(
    (uid: Rep[Int]) => Users.filter(_.uid === uid))

  /**
    * Add a user to database
    *
    * @param user a user entity
    * @return the async status
    */
  def add(user: User): Future[String] = {
    db.run(Users += user) map { res =>
      "user_add_success"
    } recover {
      case ex: Exception =>
        logger.info(ex.getCause.getMessage)
        "user_add_fail"
    }
  }

  def fetchAll: Future[Seq[User]] = {
    db.run(Users.result)
  }

  def fetch(uid: Int): Future[Option[User]] = {
    db.run(Users.filter(_.uid === uid).result.headOption)
  }

  def update(user: User): Future[Int] = {
    db.run(Users.filter(_.uid === user.uid).update(user))
  }

  /**
    * Remove the user from the `user` table
    * Notice: The comment that owed by the user will not be removed
 *
    * @param uid user id
    * @return the async status
    */
  def remove(uid: Int): Future[Int] = {
    db.run(Users.filter(_.uid === uid).delete)
  }
}