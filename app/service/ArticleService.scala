package service

import javax.inject.{Singleton, Inject}

import entity.{Category, Article}
import mapper.Tables.{CommentTable, CategoryTable, ArticleTable}
import base.Constants.{LIMIT_PAGE, IndexArticleRes, CCPT}

import play.api.db.slick.{HasDatabaseConfigProvider, DatabaseConfigProvider}
import slick.driver.JdbcProfile

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.language.implicitConversions

/**
  * Samsara Aquarius
  * Info Data Service
  *
  * @author sczyh30
  */
@Singleton
class ArticleService @Inject()(protected val dbConfigProvider: DatabaseConfigProvider) extends HasDatabaseConfigProvider[JdbcProfile] {

  import driver.api._

  type AWC = (Article, Category)

  val articles = TableQuery[ArticleTable]
  val categories = TableQuery[CategoryTable]
  val comments = TableQuery[CommentTable]

  // Queries and compiled queries
  val withCategoryCompiled = Compiled {
    (dataId: Rep[Int]) =>
      for {
        a <- articles if a.id === dataId
        c <- categories if c.cid === a.cid
      } yield (a, c)
  }

  val withCategory =
    for {
      a <- articles.sortBy(_.id.desc)
      c <- categories if c.cid === a.cid
    } yield (a, c)

  val withCategoryComplicated =
    for {
      a <- articles.sortBy(_.id.desc)
      cn = comments.filter(_.dataId === a.id).length
      c <- categories if c.cid === a.cid
    } yield (a, c, cn)

  val queryLatestCompiled =
    withCategory.take(LIMIT_PAGE)

  val withCategoryByPage =
    (offset: Int) => withCategoryComplicated.drop(offset).take(LIMIT_PAGE)

  // basic db process

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

  @deprecated def fetchAll: Future[Seq[IndexArticleRes]] = {
    db.run(withCategoryComplicated.result)
  }

  /**
    * Fetch the latest 10 article info
    * Only for REST API
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
    * @return page number
    */
  def calcPage: Future[Int] = {
    db.run(articles.length.result) map { all =>
      val p = all % LIMIT_PAGE == 0
      if (p) all / LIMIT_PAGE
      else (all / LIMIT_PAGE) + 1
    } recover {
      case ex: Exception => 0
    }
  }

  def fetchWithPage(offset: Int): Future[Seq[IndexArticleRes]] = {
    db.run(withCategoryByPage(offset).result)
  }

  /**
    * Fetch articles by the abbr of the category
    * @param abbr the abbr of the category
    * @return article result (CCPT type)
    */
  def fetchByCAbbr(abbr: String): Future[Option[CCPT]] = {
    db.run(categories.filter(_.abbr === abbr).result.headOption) flatMap {
      case Some(c) =>
        db.run(articles.filter(_.cid === c.cid).result) map { res =>
          Some(c -> res)
        }
      case None => Future(None)
    }
  }

}
