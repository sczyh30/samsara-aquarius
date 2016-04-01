package service

import java.util.NoSuchElementException
import javax.inject.{Singleton, Inject}

import entity.User
import entity.form.LoginFormData
import mapper.Tables.UserTable
import security.Encryptor.ImplicitEc

import play.api.Logger
import play.api.db.slick.{HasDatabaseConfigProvider, DatabaseConfigProvider}
import service.exception.ValidateWrong
import slick.driver.JdbcProfile

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success, Try}
import scala.async.Async._

/**
  * Samsara Aquarius
  * User Service
  *
  * @author sczyh30
  */
@Singleton
class UserService @Inject()(protected val dbConfigProvider: DatabaseConfigProvider) extends HasDatabaseConfigProvider[JdbcProfile] {

  import driver.api._

  private val Users = TableQuery[UserTable]

  private val queryByUid = Compiled(
    (uid: Rep[Int]) =>
      Users.filter(_.uid === uid))

  private val queryByName = Compiled(
    (username: Rep[String]) =>
      Users.filter(_.username === username))

  private val queryLogin = Compiled {
    (username: Rep[String], password: Rep[String]) =>
      Users.filter(_.username === username)
        .map(_.password === password)
  }

  /**
    * Process the login
    * @param form the login form data
    * @return the login result
    */
  def login(form: LoginFormData): Future[Try[User]] = { // needn't judge if the user exists
    db.run(queryLogin(form.username, form.password.encrypt()).result.head) map (r => Success(r)) recover {
      case ex: Exception => Failure(ex)
    } flatMap {
      case Success(res) =>
        if(res)
          db.run(queryByName(form.username).result.head) map (u => Success(u))
        else
          Future(Failure(new ValidateWrong("Login Validate Wrong")))
      case e@Failure(ex) => Future(Failure(ex))
    }
  }

  /**
    * Add a user to database
    *
    * @param user a user entity
    * @return the async status
    */
  def add(user: User): Future[String] = {
    db.run(Users += user) map { res =>
      Logger.debug("user_reg:" + user)
      "user_add_success"
    } recover {
      case ex: Exception =>
        Logger.info(ex.getCause.getMessage)
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