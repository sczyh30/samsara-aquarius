package service

import javax.inject.{Singleton, Inject}

import entity.{Category, Article}
import mapper.Tables.{CategoryTable, ArticleTable}

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

  val LIMIT_PAGE = 10

  val articles = TableQuery[ArticleTable]
  val categories = TableQuery[CategoryTable]

  val withCategoryCompiled = Compiled {
    (dataId: Rep[Int]) =>
      for {
        a <- articles if a.id === dataId
        c <- categories if c.cid === a.cid
      } yield (a, c)
  }

  val withCategoryAll =
    for {
      a <- articles.sortBy(_.id.desc)
      c <- categories if c.cid === a.cid
    } yield (a, c)

  val queryLatestCompiled =
    withCategoryAll.take(LIMIT_PAGE)

  val withCategoryByPage =
    (offset: Int) => withCategoryAll.drop(offset).take(LIMIT_PAGE)

  def addInfo(info: Article): Future[Int] = {
    db.run(articles += info) recover {
      case ex: Exception => -1
    }
  }

  def fetch(id: Int): Future[Option[(Article, Category)]] = {
    db.run(withCategoryCompiled(id).result.headOption)
  }

  def fetchWithPage(offset: Int): Future[Seq[(Article, Category)]] = {
    db.run(withCategoryByPage(offset).result)
  }

  def fetchByCategory(cid: Int): Future[Seq[(Article, Category)]] = {
    db.run {
      (for {
        a <- articles if a.cid === cid
        c <- categories if c.cid === a.cid
      } yield a -> c).result
    }
  }

  def fetchByCAbbr(abbr: String): Future[Option[(Category, Seq[Article])]] = {
    db.run(categories.filter(_.abbr === abbr).result.headOption) flatMap {
      case Some(c) =>
        db.run(articles.filter(_.cid === c.cid).result) map { res =>
          Some(c -> res)
        }
      case None => Future(None)
    }
  }

  def fetchAll: Future[Seq[(Article, Category)]] = {
    db.run(withCategoryAll.result)
  }

  def latest: Future[Seq[(Article, Category)]] = {
    db.run(queryLatestCompiled.result)
  }

  def update(info: Article): Future[Int] = {
    db.run(articles.filter(_.id === info.id).update(info))
  }

  def remove(id: Int): Future[Int] = {
    db.run(articles.filter(_.id === id).delete)
  }

}
