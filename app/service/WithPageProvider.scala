package service

import play.api.db.slick.HasDatabaseConfigProvider
import slick.driver.JdbcProfile

import base.Constants.LIMIT_PAGE

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Samsara Aquarius
  * Slick DatabaseConfigProvider class with basic page functions
  *
  * @author sczyh30
  * @since 0.2.0
  */
trait WithPageProvider extends HasDatabaseConfigProvider[JdbcProfile] with PageDao {

  import driver.api._

  /**
    * Generate the page query
    *
    * @param query a Slick Query
    * @param offset query offset. The first offset entries will be dropped.
    * @tparam R The expected query result is Seq[R], corresponding to Slick Query's C[U]
    */
  def withQueryByPage[R](query: slick.lifted.Query[_, R, Seq], offset: Int): Query[_, R, Seq] =
      query.drop(offset).take(LIMIT_PAGE)

  /**
    * Calc the page number of a certain query
    *
    * @param query a Slick Query
    * @return async result of the page number
    */
  def page(query: slick.lifted.Query[_, _, Seq]): Future[Int] = {
    db.run(query.length.result) map { all =>
      val p = all % LIMIT_PAGE == 0
      if (p) all / LIMIT_PAGE
      else (all / LIMIT_PAGE) + 1
    } recover {
      case ex: Exception => 0
    }
  }

  /**
    * Fetch certain entries by page
    *
    * @param query a Slick Query
    * @param offset query offset. The first offset entries will be dropped.
    * @tparam R The expected query result type is Seq[R], corresponding to Slick Query's C[U]
    * @return the async result of the entries, typed `Future Seq R`
    */
  def fetchWithPage[R](query: slick.lifted.Query[_, R, Seq], offset: Int): Future[Seq[R]] = {
    db.run(withQueryByPage(query, offset).result) // maybe query being implicit is a good idea?
  }
}
