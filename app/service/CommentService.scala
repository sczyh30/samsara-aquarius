package service

import javax.inject.{Singleton, Inject}

import entity.Comment
import mapper.Tables.CommentTable
import security.SafeEntityEnsure.SafeCommentEnsure

import play.api.db.slick.{HasDatabaseConfigProvider, DatabaseConfigProvider}
import slick.driver.JdbcProfile

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Samsara Aquarius
  * Comment Service
  *
  * @author sczyh30
  */
@Singleton
class CommentService @Inject()(protected val dbConfigProvider: DatabaseConfigProvider) extends HasDatabaseConfigProvider[JdbcProfile] {

  import driver.api._

  val comments = TableQuery[CommentTable]

  protected val getByCidCompiled = Compiled(
    (cid: Rep[Int]) => comments.filter(_.cid === cid))

  protected val getByUidCompiled = Compiled(
    (uid: Rep[Int]) => comments.filter(_.uid === uid))

  protected val getByArticleCompiled = Compiled(
    (data_id: Rep[Int]) => comments.filter(_.dataId === data_id))

  protected val getCertainCompiled = Compiled{
    (data_id: Rep[Int], uid: Rep[Int]) => comments.filter(r =>
      r.uid === uid && r.dataId === data_id)
  }

  def add(comment: Comment): Future[Int] = {
    db.run(comments += comment.safe)
  }

  def fetch(cid: Int) = {
    db.run(getByCidCompiled(cid).result.headOption)
  }

  def fetchByArticle(data_id: Int) = {
    db.run(getByArticleCompiled(data_id).result)
  }

  def fetchByUser(uid: Int) = {
    db.run(getByUidCompiled(uid).result)
  }

  def fetchCertain(data_id: Int, uid: Int) = {
    db.run(getCertainCompiled(data_id, uid).result.headOption)
    //db.run(getByUidCompiled(uid).result.andThen(
    //  getByArticleCompiled(data_id).result.headOption))
  }

  def fetchAll = {
    db.run(comments.result)
  }

  def update(comment: Comment): Future[Int] = {
    db.run(getByCidCompiled(comment.cid).update(comment.safe))
  }

  def remove(cid: Int): Future[Int] = {
    db.run(getByCidCompiled(cid).delete)
  }

}
