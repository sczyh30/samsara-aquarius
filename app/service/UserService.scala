package service

import javax.inject.{Singleton, Inject}

import entity.User
import mapper.Tables.UserTable
import security.Encryptor.ImplicitEc
import base.Constants.{DB_ADD_DUPLICATE, UserCommentInfo}
import service.exception.ValidateWrong

import play.api.db.slick.{HasDatabaseConfigProvider, DatabaseConfigProvider}
import slick.driver.JdbcProfile

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success, Try}

/**
  * Samsara Aquarius
  * User Service
  *
  * @author sczyh30
  */
@Singleton
class UserService @Inject() (protected val dbConfigProvider: DatabaseConfigProvider) extends HasDatabaseConfigProvider[JdbcProfile] {

  import driver.api._

  private val users = TableQuery[UserTable]

  private[service] val queryByUid = Compiled(
    (uid: Rep[Int]) =>
      users.filter(_.uid === uid))

  private[service] val queryByName = Compiled(
    (username: Rep[String]) =>
      users.filter(_.username === username))

  private[service] val queryLogin = Compiled {
    (username: Rep[String], password: Rep[String]) =>
      users.filter(_.username === username)
        .map(_.password === password)
  }

  private[service] val queryCommentInfo = Compiled {
    (uid: Rep[Int]) =>
      users.filter(_.uid === uid).map(x => (x.username, x.avatar))
  }

  /**
    * Process the login
    * @return the login result
    */
  def login(username: String, password: String): Future[Try[User]] = {
    db.run(queryLogin(username, password.encrypt()).result.head) map (r => Success(r)) recover {
      case ex: Exception => Failure(ex)
    } flatMap {
      case Success(res) =>
        if(res)
          db.run(queryByName(username).result.head) map (u => Success(u))
        else
          Future(Failure(new ValidateWrong("Login Validate Wrong")))
      case e@Failure(ex) => Future(Failure(ex))
    }
  }

  /**
    * Fetch the comment info about user
    * @param uid user id
    */
  def fetchCommentInfo(uid: Int): Future[UserCommentInfo] = { // note: do not return Future[Option[UserCommentInfo]]
    db.run(queryCommentInfo(uid).result.head) // hazardous
  }

  /**
    * Add a user to database, represent register logic
    *
    * @param user a user entity
    * @return the async status
    */
  def add(user: User): Future[Int] = {
    db.run(users += user) recover {
      case duplicate: java.sql.SQLIntegrityConstraintViolationException => DB_ADD_DUPLICATE
      case _: Exception => -2
    }
  }

  def fetchAll: Future[Seq[User]] = {
    db.run(users.result)
  }

  def fetch(uid: Int): Future[Option[User]] = {
    db.run(queryByUid(uid).result.headOption)
  }

  def fetchByName(username: String): Future[Option[User]] = {
    db.run(queryByName(username).result.headOption)
  }

  def update(user: User): Future[Int] = {
    db.run(queryByUid(user.uid).update(user))
  }

  def updateAvatar(uid: Int, avatar: String): Future[Int] = {
    val q = users.filter(_.uid === uid).map(_.avatar)
    db.run(q.update(Some(avatar))) recover {
      case _: Exception => -1
    }
  }

  /**
    * Remove the user from the `user` table
    * Notice: The comment that owed by the user will not be removed immediately
    *
    * @param uid user id
    * @return the async status
    */
  def remove(uid: Int): Future[Int] = {
    db.run(queryByUid(uid).delete)
  }
}