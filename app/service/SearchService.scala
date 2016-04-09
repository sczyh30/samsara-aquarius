package service

import javax.inject.{Inject, Singleton}

import entity.{Category, Article}
import mapper.Tables.{CategoryTable, ArticleTable}

import play.api.db.slick.{HasDatabaseConfigProvider, DatabaseConfigProvider}
import slick.driver.JdbcProfile

import scala.concurrent.Future

/**
  * Samsara Aquarius
  * Search Service v1
  *
  * Note: In this version, we simply use SQL LIKE clause to implement search functions.
  * This could lead to bad performance. In next version, we may use full-index system to enhance it.
  *
  * @author sczyh30
  */
@Singleton
class SearchService @Inject()(protected val dbConfigProvider: DatabaseConfigProvider) extends WithPageProvider {

  import driver.api._

  type AWC = (Article, Category) // Article with Category, typed `Tuple2`

  val articles = TableQuery[ArticleTable]
  val categories = TableQuery[CategoryTable]

  val byNameComplied = Compiled {
    (name: Rep[String], pattern: Rep[String]) =>
      articles.filter(_.title like pattern) //TODO: BAD PERFORMANCE!
  }

  val byNameWithCategoryComplied = Compiled {
    (name: Rep[String], pattern: Rep[String]) => for {
      a <- articles.sortBy(_.id.desc).filter(_.title like pattern)
      c <- categories if c.cid === a.cid
    } yield (a, c)
  }

  /**
    * Search articles by name
    * for REST API until v0.3.0
    * @param name article partial name
    */
  def byName(name: String): Future[Seq[Article]] = {
    db.run(byNameComplied(name, s"%$name%").result)
  }

  /**
    * Search articles by name
    * For application service
    * @param name article partial name
    * @return async result, typed `AWS`
    */
  def byNameWithCategory(name: String): Future[Seq[AWC]] = {
    db.run(byNameWithCategoryComplied(name, s"%$name%").result)
  }

  //TODO: optimize this in v0.5.x
  def byNameWithCategoryPaged(name: String, offset: Int): Future[Seq[AWC]] = ???

}
