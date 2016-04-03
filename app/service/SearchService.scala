package service

import javax.inject.{Inject, Singleton}

import mapper.Tables.ArticleTable

import play.api.db.slick.{HasDatabaseConfigProvider, DatabaseConfigProvider}
import slick.driver.JdbcProfile

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

  def byName(name: String) = ???

}
