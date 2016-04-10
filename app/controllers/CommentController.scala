package controllers

import javax.inject.{Inject, Singleton}

import service.{ArticleService, UserService, CommentService}

import play.api.mvc._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  * Samsara Aquarius Route
  * Comment Controller
  *
  * @author sczyh30
  */
@Singleton
class CommentController @Inject() (service: CommentService, us: UserService, ars: ArticleService) extends Controller {

  /**
    * List comments of certain article
    * NOTE: takes 2 statement query, need to be optimized
    * @param aid article id
    */
  def list(aid: Int) = Action.async { implicit request => // 2 queries
    service.fetchByArticle(aid) flatMap { data =>
      val uf = for(c <- data) yield us.fetchCommentInfo(c.uid)
      ars.fetchOnly(aid) flatMap { article =>
        Future.sequence(uf) map { users =>
          Ok(views.html.comment(data.zip(users), article))
        }
      }
    }
  }

  def publish(aid: Int) = TODO

}
