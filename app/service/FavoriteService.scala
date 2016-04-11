package service

import javax.inject.{Singleton, Inject}

import base.Constants._
import entity.Favorite
import mapper.Tables.FavoriteTable

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

  val queryByArticleCompiled = Compiled {
    (aid: Rep[Int]) =>
      favorites.filter(_.articleId === aid).length
  }

  /**
    * On Favorite an article
    *
    * @return async result; if less than 0, there is something wrong
    */
  def ❤(favorite: Favorite): Future[Int] = {
    db.run(favorites += favorite) recover {
      case duplicate: com.mysql.jdbc.exceptions.MySQLIntegrityConstraintViolationException => DB_ADD_DUPLICATE
      case _: Exception => -2
    }
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

  def ifLike(aid: Int, uid: Int): Future[Boolean] = {
    db.run(favorites.filter(x => x.articleId === aid && x.likeUid === uid).exists.result)
  }

}
