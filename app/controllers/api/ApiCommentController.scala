package controllers.api

import javax.inject.{Inject, Singleton}

import base.Constants._
import entity.{Comment, UserToken}
import play.api.cache._
import service.CommentService
import entity.Results._
import RestConverter.resultFormat

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
class ApiCommentController @Inject() (@NamedCache("user-token-cache") tokenCache: CacheApi, service: CommentService) extends Controller {

  def fetchByArticle(aid: Int) = Action.async { implicit request =>
    Future.successful(NotImplemented)
    /*service fetchByArticle aid map { data =>
      Ok(Json.toJson(data))
    }*/
  }

  def comment(aid: Int, comment: String, token: String) = Action.async { implicit request =>
    tokenCache.get[UserToken](WRAP_USER_KEY(token)) match {
      case Some(user) =>
        if (comment.length > 140)
          Future.successful(BadRequest(Json.toJson(API_COMMENT_FAILURE_TOO_LONG)))
        else {
          val cm = Comment(0, user.uid, aid, comment, java.sql.Timestamp.valueOf(java.time.LocalDateTime.now()))
          service add cm map { res =>
            if (res > 0) Ok(Json.toJson(API_COMMENT_SUCCESS))
            else  BadRequest(Json.toJson(API_COMMENT_FAILURE_UNKNOWN))
          }
        }
      case None =>
        Future.successful(BadRequest(Json.toJson(TOKEN_VALIDATE_WRONG)))
    }
  }

  def remove(aid: Int) = TODO

}
