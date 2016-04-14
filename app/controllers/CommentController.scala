package controllers

import javax.inject.{Inject, Singleton}

import entity.Comment
import service.{ArticleService, CommentService, FavoriteService, UserService}
import play.api.mvc._
import play.api.libs.concurrent.Execution.Implicits.defaultContext

import scala.concurrent.Future

/**
  * Samsara Aquarius Route
  * Comment Controller
  *
  * @author sczyh30
  */
@Singleton
class CommentController @Inject() (service: CommentService, us: UserService, ars: ArticleService, fvs: FavoriteService) extends Controller {

  /**
    * List comments of certain article
    * NOTE: takes 2 statement query, need to be optimized
    *
    * @param aid article id
    */
  def list(aid: Int) = Action.async { implicit request => // 2 queries
    service.fetchByArticle(aid) flatMap { data =>
      val uf = for(c <- data) yield us.fetchCommentInfo(c.uid)
      ars.fetchOnly(aid) flatMap { article =>
        Future.sequence(uf) flatMap { users =>
          fvs countA aid flatMap { count => // maybe too complicated?
            request.session.get("uid") match {
              case Some(uid) =>
                fvs ifLike(aid, uid.toInt) map {
                  case true  => Ok(views.html.comment(data.zip(users), article, count, 1))
                  case false => Ok(views.html.comment(data.zip(users), article, count, 0))
                }
              case None =>
                Future.successful(Ok(views.html.comment(data.zip(users), article, count, 4)))
            }
          }
        }
      }
    }
  }

  def publish(aid: Int) = Action.async { implicit request =>
    case class C(comment: String)
    object CForm {
      import play.api.data.Form
      import play.api.data.Forms._

      val form = Form(
        mapping(
          "comment" → nonEmptyText(maxLength = 140)
        )(C.apply)(C.unapply)
      )
    }

    utils.DateUtils.ensureSession
    request.session.get("aq_token") match {
      case Some(user) =>
        CForm.form.bindFromRequest().fold( errorForm => {
          Future.successful(BadRequest(views.html.error.ServerError("Oops...", "出了一点小问题！")))
        }, comment => {
          val uid = request.session.get("uid").getOrElse("-1").toInt // may cause error
          val c = Comment(0, uid, aid, comment.comment, java.sql.Timestamp.valueOf(java.time.LocalDateTime.now()))
          service.add(c) map { res =>
            if (res > 0) Redirect(routes.CommentController.list(aid))
            else BadRequest(views.html.error.ServerError("Oops...", "出了一点小问题！"))
          }
        })
      case None =>
        Future.successful(Redirect(routes.UserController.loginIndex()))
    }
  }

}
