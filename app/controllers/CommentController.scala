package controllers

import javax.inject.{Inject, Singleton}

import service.CommentService

import play.api.mvc._

import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Samsara Aquarius Route
  * Comment Controller
  *
  * @author sczyh30
  */
@Singleton
class CommentController @Inject() (service: CommentService) extends Controller {

  def list(aid: Int) = Action.async { implicit request =>
    service.fetchByArticle(aid) map { data =>
      Ok(views.html.comment(data))
    }
  }

  def publish(aid: Int) = TODO

}
