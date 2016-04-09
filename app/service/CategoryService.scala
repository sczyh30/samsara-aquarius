package service

import javax.inject.{Singleton, Inject}

import entity.Category
import mapper.Tables.{ArticleTable, CategoryTable}

import play.api.db.slick.DatabaseConfigProvider

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Samsara Aquarius
  * Category Service
  *
  * @author sczyh30
  */
@Singleton
class CategoryService @Inject() (protected val dbConfigProvider: DatabaseConfigProvider)
  extends WithPageProvider with BaseDao[Category, Int] {

  import driver.api._

  val articles = TableQuery[ArticleTable]
  val categories = TableQuery[CategoryTable]

  protected val queryByCid = Compiled(
    (cid: Rep[Int]) => categories.filter(_.cid === cid))

  protected val categoriesCompiled = // used in categories page
    categories.map { c =>
      (c, articles.filter(_.cid === c.cid).length)
    }

  /**
    * Calc the page number
    * @return the async result of page number
    */
  def calcPage: Future[Int] =
    page(categories)

  /**
    * Fetch the categories with page
    * @param offset query offset. The first offset entries will be dropped.
    * @return the async result of the entries
    */
  def fetchWithPage(offset: Int): Future[Seq[(Category, Int)]] =
    super.fetchWithPage(categoriesCompiled, offset)

  /**
    * Fetch all categories with article number in this category
    * @return the async result
    */
  def fetchAllWithCount: Future[Seq[(Category, Int)]] = {
    db.run(categoriesCompiled.result)
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

  def update(c: Category): Future[Int] = {
    db.run(queryByCid(c.cid).update(c))
  }

  def remove(cid: Int): Future[Int] = {
    db.run(queryByCid(cid).delete)
  }
}
