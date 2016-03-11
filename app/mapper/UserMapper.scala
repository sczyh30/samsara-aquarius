package mapper

import javax.inject.Inject

import play.api.db.slick.DatabaseConfigProvider
import slick.driver.JdbcProfile

import scala.concurrent.{Future, ExecutionContext}

/**
  * User Mapper
  *
  * @author sczyh30
  */
class UserMapper @Inject() (dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {

  import Tables.User

  private val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import driver.api._

  /*def create(uid: Int, username: String, password: String,
             joinDate: java.sql.Date, avatar: Option[String] = Some("default"),
             tips: Option[String] = Some("None~"), website: String): Future[entity.User] = db.run {
    (User.map {
      u => (u.uid, u.username, u.password, u.joinDate, u.avatar, u.tips, u.website)
    } returning User.map(_.uid)
      into())
  }*/
}
