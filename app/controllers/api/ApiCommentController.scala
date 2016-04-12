package controllers.api

import javax.inject.{Inject, Singleton}

import service.CommentService

import scala.concurrent.Future

//import RestConverter.commentFormat

import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json.Json
import play.api.mvc.{Action, Controller}


/**
  * Samsara Aquarius API v1
  * Comment API Controller
  *
  * @author sczyh30
  */
@Singleton
class ApiCommentController @Inject() (service: CommentService) extends Controller {

  def fetchByArticle(aid: Int) = Action.async { implicit request =>
    Future.successful(NotImplemented)
    /*service fetchByArticle aid map { data =>
      Ok(Json.toJson(data))
    }*/
  }

  def comment(aid: Int) = TODO

  def remove(aid: Int) = TODO

}
