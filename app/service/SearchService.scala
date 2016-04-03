package service

import javax.inject.{Inject, Singleton}

import entity.Article
import mapper.Tables.ArticleTable

import play.api.db.slick.{HasDatabaseConfigProvider, DatabaseConfigProvider}
import slick.driver.JdbcProfile

import scala.concurrent.Future

/**
  * Samsara Aquarius
  * Search Service
  *
  * @author sczyh30
  */
@Singleton
class SearchService @Inject()(protected val dbConfigProvider: DatabaseConfigProvider) extends HasDatabaseConfigProvider[JdbcProfile] {

  import driver.api._

  val articles = TableQuery[ArticleTable]

  val byNameComplied = Compiled {
    (name: Rep[String]) => articles.filter(_.title like ("%" + name + "%")) //TODO:BAD PERFORMANCE!
  }

  def byName(name: String): Future[Seq[Article]] = {
    db.run(byNameComplied(name).result)
  }

}
