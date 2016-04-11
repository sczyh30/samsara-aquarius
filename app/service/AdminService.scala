package service

import javax.inject.{Inject, Singleton}

import entity.{Share, Article}
import mapper.Tables.{ShareTable, CommentTable, CategoryTable, ArticleTable}
import play.api.db.slick.{HasDatabaseConfigProvider, DatabaseConfigProvider}
import slick.driver.JdbcProfile

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Samsara Aquarius
  * Admin Service
  *
  * @author sczyh30
  */
@Singleton
class AdminService @Inject() (protected val dbConfigProvider: DatabaseConfigProvider) extends HasDatabaseConfigProvider[JdbcProfile] {

  import driver.api._

  val articles = TableQuery[ArticleTable]
  val categories = TableQuery[CategoryTable]
  val comments = TableQuery[CommentTable]
  val pendings = TableQuery[ShareTable]

  def addArticle(article: Article): Future[Int] = {
    db.run(articles += article) recover {
      case ex: Exception => -1
    }
  }

  def getShare: Future[Seq[Share]] = {
    db.run(pendings.result)
  }


}
