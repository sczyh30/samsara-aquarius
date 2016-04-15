package service

import javax.inject.{Inject, Singleton}

import entity.{Article, Share}
import mapper.Tables._
import security.Encryptor.ImplicitEc
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import service.exception.ValidateWrong
import slick.driver.JdbcProfile

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success, Try}

/**
  * Samsara Aquarius
  * Admin Service
  *
  * @author sczyh30
  */
@Singleton
class AdminService @Inject() (protected val dbConfigProvider: DatabaseConfigProvider) extends HasDatabaseConfigProvider[JdbcProfile] {

  import driver.api._

  val bangs = TableQuery[AdminTable]
  val articles = TableQuery[ArticleTable]
  val categories = TableQuery[CategoryTable]
  val comments = TableQuery[CommentTable]
  val pendings = TableQuery[ShareTable]

  private[service] val queryLogin = Compiled {
    (username: Rep[String], password: Rep[String]) =>
      bangs.filter(_.name === username)
        .map(_.password === password)
  }

  def addArticle(article: Article): Future[Int] = {
    db.run(articles += article) recover {
      case ex: Exception => -1
    }
  }

  def getShare: Future[Seq[Share]] = {
    db.run(pendings.result)
  }

  def login(username: String, password: String): Future[Try[entity.Admin]] = {
    db.run(queryLogin(username, password.encryptSpecial).result.headOption) flatMap {
      case Some(ok) =>
        db.run(bangs.filter(_.name === username).result.head) map { res =>
          Success(res.copy(password = ""))
        }
      case None =>
        Future.successful(Failure(new ValidateWrong("Admin Go Validate Wrong")))
    }
  }


}
