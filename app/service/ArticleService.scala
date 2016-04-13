package service

import javax.inject.{Singleton, Inject}

import entity.{Category, Article}
import mapper.Tables.{ShareTable, CommentTable, CategoryTable, ArticleTable}
import base.Constants.{LIMIT_PAGE, IndexArticleRes, CCPT}

import play.api.db.slick.DatabaseConfigProvider

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.language.implicitConversions

/**
  * Samsara Aquarius
  * Article Service
  *
  * @author sczyh30
  */
@Singleton
class ArticleService @Inject() (protected val dbConfigProvider: DatabaseConfigProvider)
  extends WithPageProvider with BaseDao[Article, Int] {

  import driver.api._

  type AWC = (Article, Category) // Article with Category, typed `Tuple2`

  val articles = TableQuery[ArticleTable]
  val categories = TableQuery[CategoryTable]
  val comments = TableQuery[CommentTable]
  val pendings = TableQuery[ShareTable]

  // Queries and compiled queries
  // query result: AWC by id
  val withCategoryCompiled = Compiled {
    (dataId: Rep[Int]) => (
      for {
        a <- articles if a.id === dataId
        c <- categories if c.cid === a.cid
      } yield (a, c)) sortBy (x => x._1.id.desc)
  }

  // query result: AWC (all)
  val withCategory = (
    for {
      a <- articles.sortBy(_.id.desc)
      c <- categories if c.cid === a.cid
    } yield (a, c)) sortBy (x => x._1.id.desc)

  // query result: (Article, Category, Comment Num)
  val withCategoryComplicated = (for {
      a <- articles
      cn = comments.filter(_.dataId === a.id).length
      c <- categories if c.cid === a.cid
    } yield (a, c, cn)) sortBy (x => x._1.id.desc)

  // only for REST API
  val queryLatestCompiled =
    withCategory.take(LIMIT_PAGE)

  /** basic db process */

  def add(info: Article): Future[Int] = {
    db.run(articles += info) recover {
      case ex: Exception => -1
    }
  }

  /**
    * Fetch a certain article info by id
    * This is specific for REST API
    *
    * @param id article id
    * @return the result (Article with Category)
    */
  def fetch(id: Int): Future[Option[AWC]] = {
    db.run(withCategoryCompiled(id).result.headOption)
  }

  def fetchOnly(id: Int): Future[Article] = {
    db.run(articles.filter(_.id === id).result.head)
  }

  @deprecated(message = "Fetch-all process consumes too much resources", since = "0.3.0")
  def fetchAll: Future[Seq[IndexArticleRes]] = {
    db.run(withCategoryComplicated.result)
  }

  /**
    * Fetch the latest 10 article info
    * Only for REST API
    *
    * @return result(Article with Category)
    */
  def latest: Future[Seq[AWC]] = {
    db.run(queryLatestCompiled.result)
  }

  def update(info: Article): Future[Int] = {
    db.run(articles.filter(_.id === info.id).update(info)) recover {
      case _: Exception => -1
    }
  }

  def remove(id: Int): Future[Int] = {
    db.run(articles.filter(_.id === id).delete) recover {
      case _: Exception => -1
    }
  }

  // complicated db process

  /**
    * Retrieve length of the results and then calc the page number
    *
    * @return page number
    */
  def calcPage: Future[Int] = page(articles)

  def fetchWithPage(offset: Int): Future[Seq[IndexArticleRes]] = {
    super.fetchWithPage(withCategoryComplicated, offset)
  }

  /**
    * Fetch articles by the abbr of the category
    *
    * @param abbr the abbr of the category
    * @return article result (CCPT type)
    */
  def fetchByCAbbr(abbr: String): Future[Option[CCPT]] = {
    db.run(categories.filter(_.abbr === abbr).result.headOption) flatMap {
      case Some(c) =>
        val query = for {
          ars <- articles if ars.cid === c.cid
          count = comments.filter(_.dataId === ars.id).length
        } yield (ars, count)
        db.run(query.result) map { res =>
          Some(c, res)
        }
      case None => Future(None)
    }
  }

  def shareArticle(s: entity.Share): Future[Int] = {
    db.run(pendings += s) recover {
      case ex: Exception => -1
    }
  }

}
