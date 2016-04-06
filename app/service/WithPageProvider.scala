package service

import play.api.db.slick.HasDatabaseConfigProvider
import slick.driver.JdbcProfile

import base.Constants.LIMIT_PAGE

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * A HasDatabaseConfigProvider class with basic page function
  */
trait WithPageProvider extends HasDatabaseConfigProvider[JdbcProfile] with PageDao {

  import driver.api._

  val withQueryByPage =
    (query: slick.lifted.Query[_, _, Seq], offset: Int) =>
      query.drop(offset).take(LIMIT_PAGE)

  def page(query: slick.lifted.Query[_, _, Seq]): Future[Int] = {
    db.run(query.length.result) map { all =>
      val p = all % LIMIT_PAGE == 0
      if (p) all / LIMIT_PAGE
      else (all / LIMIT_PAGE) + 1
    } recover {
      case ex: Exception => 0
    }
  }

  def fetchWithPage(query: slick.lifted.Query[_, _, Seq], offset: Int): Future[Seq[_]] = {
    db.run(withQueryByPage(query, offset).result) // maybe query being implicit is a good idea
  } //TODO: fix this function's type parameter!
}
