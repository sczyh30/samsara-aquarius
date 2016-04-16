package service

import javax.inject.{Singleton, Inject}

import entity.{Article, Favorite}
import mapper.Tables.{ArticleTable, FavoriteTable}

import play.api.db.slick.{HasDatabaseConfigProvider, DatabaseConfigProvider}
import slick.driver.JdbcProfile

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Samsara Aquarius
  * Favorite Service (on feed)
  *
  * @author sczyh30
  * @since 0.3.12
  */
@Singleton
class FavoriteService @Inject() (protected val dbConfigProvider: DatabaseConfigProvider) extends HasDatabaseConfigProvider[JdbcProfile] {

  import driver.api._

  val favorites = TableQuery[FavoriteTable]
  val articles = TableQuery[ArticleTable]

  val queryByArticleCountCompiled = Compiled {
    (aid: Rep[Int]) =>
      favorites.filter(_.articleId === aid).length
  }

  val queryByUserCompiled = Compiled {
    (uid: Rep[Int]) => for {
      fv <- favorites.filter(_.likeUid === uid)
      arc <- articles.filter(_.id === fv.articleId)
    } yield arc
  }

  /**
    * On Favorite an article
    *
    * @return async result; if less than 0, there is something wrong
    */
  def ❤(favorite: Favorite): Future[Int] = {
      db.run(favorites += favorite) //TODO-NOTE: DO NOT RECOVER THIS, HANDLE ERROR IN THE ACTOR
  }

  /**
    * On cancel favorite an article
    *
    * @return async result;
    */
  def !♡!(favorite: Favorite): Future[Int] = {
    db.run(favorites.filter { x =>
      x.articleId === favorite.articleId && x.likeUid === favorite.likeUid
    }.delete)
  }

  def countA(aid: Int): Future[Int] = {
    db.run(queryByArticleCountCompiled(aid).result)
  }

  def ifLike(aid: Int, uid: Int): Future[Boolean] = {
    db.run(favorites.filter(x => x.articleId === aid && x.likeUid === uid).exists.result)
  }

  def fetchUserFavorite(uid: Int): Future[Seq[Article]] = {
    db.run(queryByUserCompiled(uid).result)
  }

}
