package mapper

import javax.inject.Inject

import entity.User
import mapper.Tables.UserTable
import play.api.Play
import play.api.db.slick.DatabaseConfigProvider
import slick.driver.JdbcProfile
import slick.lifted.TableQuery

import scala.concurrent.{Future, ExecutionContext}

/**
  * User Mapper
  *
  * @author sczyh30
  */
object UserMapper {

  val users = TableQuery[UserTable]

  private val dbConfig = DatabaseConfigProvider.get[JdbcProfile]((Play.current))

  import dbConfig._
  import driver.api._

  /*def add(user: User): Future[String] = {
    dbConfig.db.run(users += user) map { res =>
      "User successfully added~"
    } recover {
      case ex: Exception => ex.getCause.getMessage
    }
  }*/

  /*def create(uid: Int, username: String, password: String,
             joinDate: java.sql.Date, avatar: Option[String] = Some("default"),
             tips: Option[String] = Some("None~"), website: String): Future[entity.User] = db.run {
    (User.map {
      u => (u.uid, u.username, u.password, u.joinDate, u.avatar, u.tips, u.website)
    } returning User.map(_.uid)
      into())
  }*/
}
