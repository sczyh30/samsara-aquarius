package service

import javax.inject.{Singleton, Inject}

import entity.Category
import mapper.Tables.{ArticleTable, CategoryTable}

import play.api.db.slick.{HasDatabaseConfigProvider, DatabaseConfigProvider}
import slick.driver.JdbcProfile

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Samsara Aquarius
  * Category Service
  *
  * @author sczyh30
  */
@Singleton
class CategoryService @Inject()(protected val dbConfigProvider: DatabaseConfigProvider) extends HasDatabaseConfigProvider[JdbcProfile] {

  import driver.api._

  val articles = TableQuery[ArticleTable]
  val categories = TableQuery[CategoryTable]

  protected val queryByCid = Compiled(
    (cid: Rep[Int]) => categories.filter(_.cid === cid))

  protected val categoriesCompiled = Compiled {
    categories.map { c =>
      (c, articles.filter(_.cid === c.cid).length)
    }
  }

  def add(c: Category): Future[Int] = {
    db.run(categories += c) recover {
      case ex: Exception => -1
    }
  }

  def fetch(cid: Int): Future[Option[Category]] = {
    db.run(queryByCid(cid).result.headOption)
  }

  def fetchAll: Future[Seq[Category]] = {
    db.run(categories.result)
  }

  def fetchAllWithCount: Future[Seq[(Category, Int)]] = {
    db.run(categoriesCompiled.result)
  }

  def update(c: Category): Future[Int] = {
    db.run(queryByCid(c.cid).update(c))
  }

  def remove(cid: Int): Future[Int] = {
    db.run(queryByCid(cid).delete)
  }
}
