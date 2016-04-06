package service

import scala.concurrent.Future

/**
  * Page Dao
  */
trait PageDao {

  def page(query: slick.lifted.Query[_, _, Seq]): Future[Int]

}
