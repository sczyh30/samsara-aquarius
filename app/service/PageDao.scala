package service

import scala.concurrent.Future

/**
  * Samsara Aquarius
  * Page Dao
  */
trait PageDao {

  def page(query: slick.lifted.Query[_, _, Seq]): Future[Int]

}
