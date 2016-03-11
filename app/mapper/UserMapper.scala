package mapper

import entity.User
import mapper.Tables.UserTable

import play.api.Play
import play.api.db.slick.DatabaseConfigProvider
import slick.driver.JdbcProfile
import slick.lifted.TableQuery

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  * User Mapper
  *
  * @author sczyh30
  */
object UserMapper {

  val users = TableQuery[UserTable]

  private val dbConfig = DatabaseConfigProvider.get[JdbcProfile](Play.current)

  import dbConfig._
  import driver.api._

  def add(user: User): Future[String] = {
    dbConfig.db.run(users += user) map { res =>
      "User successfully added~"
    } recover {
      case ex: Exception => ex.getCause.getMessage
    }
  }
}
