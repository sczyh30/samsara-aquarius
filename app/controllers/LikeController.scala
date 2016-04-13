package controllers

import javax.inject.{Singleton, Inject}

import akka.util.Timeout
import entity.ProcessResult
import message.FavoriteMessageActor
import FavoriteMessageActor.{FavoriteOn, FavoriteOff}
import entity.Results._
import service.{FavoriteService, ArticleService}

import akka.actor.ActorSystem
import akka.pattern.ask
import akka.actor._
import play.api.mvc._
import play.api.libs.json.Json
import play.api.libs.concurrent.Execution.Implicits.defaultContext

import scala.concurrent.Future
import scala.concurrent.duration._
import scala.language.postfixOps


/**
  * Samsara Aquarius
  * Like Controller
  *
  * @author sczyh30
  */
@Singleton
class LikeController @Inject()(articleService: ArticleService, system: ActorSystem, fvs: FavoriteService) extends Controller {

  val alActor = system.actorOf(Props(new FavoriteMessageActor(fvs)), base.Constants.ARTICLE_LIKE_ACTOR_NAME)
  implicit val replyFormat = Json.format[ProcessResult]
  implicit val timeout = Timeout(5 seconds) // tolerate 5 sec delay

  def like(aid: Int) = Action.async { implicit request =>
    utils.DateUtils.ensureSession
    request.session.get("uid") match {
      case Some(uid) =>
        (alActor ? FavoriteOn(aid, uid.toInt)).mapTo[ProcessResult] map { reply =>
          Ok(Json.toJson(reply))
        }
      case None =>
        Future.successful(BadRequest(Json.toJson(USER_NOT_LOGIN)))
    }
  }

  def cancelLike(aid: Int) = Action.async { implicit request =>
    utils.DateUtils.ensureSession
    request.session.get("uid") match {
      case Some(uid) =>
        (alActor ? FavoriteOff(aid, uid.toInt)).mapTo[ProcessResult] map { reply =>
          Ok(Json.toJson(reply))
        }
      case None =>
        Future.successful(BadRequest(Json.toJson(USER_NOT_LOGIN)))
    }
  }

  def likeCount(aid: Int) = Action.async { implicit request =>
    fvs countA aid map { res =>
      Ok(res.toString)
    }
  }

}
