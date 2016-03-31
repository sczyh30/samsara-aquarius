package service

import scala.concurrent.Future

/**
  * Base Dao
  */
trait BaseDao[T, K] {

  def add(x: T): Future[_]

  def fetch(k: K): Future[Option[_]]

  def fetchAll: Future[Iterable[_]]

  def update(x: T): Future[_]

  def remove(k: K): Future[_]
}
