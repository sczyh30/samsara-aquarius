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

  val articles = TableQuery[ArticleTable]
  val categories = TableQuery[CategoryTable]

  val withCategoryCompiled = Compiled {
    (dataId: Rep[Int]) =>
      for {
        a <- articles if a.id === dataId
        c <- categories if c.cid === a.cid
      } yield (a, c)
  }

  val withCategoryAll = //TODO: duplicate code
    for {
      a <- articles
      c <- categories if c.cid === a.cid
    } yield (a, c)

  def addInfo(info: Article): Future[String] = {
    db.run(articles += info) map { res =>
      "article_add_success"
    } recover {
      case ex: Exception => ex.getCause.getMessage
    }
  }

  def fetch(id: Int): Future[Option[(Article, Category)]] = {
    db.run(withCategoryCompiled(id).result.headOption)
  }

  def fetchByCategory(cid: Int): Future[Seq[(Article, Category)]] = {
    db.run {
      (for {
        a <- articles if a.cid === cid
        c <- categories if c.cid === a.cid
      } yield (a, c)).result
    }
  }

  def fetchAll: Future[Seq[(Article, Category)]] = {
    db.run(withCategoryAll.result)
  }

  def update(info: Article): Future[Int] = {
    db.run(articles.filter(_.id === info.id).update(info))
  }

  def remove(id: Int): Future[Int] = {
    db.run(articles.filter(_.id === id).delete)
  }

}
