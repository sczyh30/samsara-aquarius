package service

import scala.concurrent.Future

/**
  * Samsara Aquarius
  * Slick Page Dao Trait
  */
trait SlickPageDao {

  /**
    * Calc the page number of a certain query
    *
    * @param query a Slick Query
    * @return async result of the page number
    */
  def page(query: slick.lifted.Query[_, _, Seq]): Future[Int]

  /**
    * Fetch certain entries by page
    *
    * @param query a Slick Query
    * @param offset query offset. The first offset entries will be dropped.
    * @tparam R The expected query result type is Seq[R], corresponding to Slick Query's C[U]
    * @return the async result of the entries, typed `Future Seq R`
    */
  def fetchWithPage[R](query: slick.lifted.Query[_, R, Seq], offset: Int): Future[Seq[R]]

}
