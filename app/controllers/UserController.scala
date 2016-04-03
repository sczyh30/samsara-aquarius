package controllers

import java.util.UUID
import javax.inject.{Singleton, Inject}

import base.Constants.USER_CACHE_KEY
import entity.{UserToken, User}
import entity.form.LoginForm
import service.UserService

import play.api.mvc._
import play.api.Logger
import play.api.cache.{CacheApi, NamedCache}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.concurrent.duration._
import scala.language.postfixOps
import scala.util.{Failure, Success}

/**
  * Samsara Aquarius Route
  * Login Controller
  *
  * @author sczyh30
  */
@Singleton
class UserController @Inject() (@NamedCache("user-cache") userCache: CacheApi, service: UserService) extends Controller {

  implicit class TokenConverter(user: User) {
    def toToken: UserToken =
      UserToken(user.uid, user.username, UUID.randomUUID().toString)
  }

  def loginIndex = Action { implicit request =>
    userCache.get[UserToken](USER_CACHE_KEY) match {
      case Some(user) => Redirect(routes.Application.index())
      case None => Ok(views.html.login(LoginForm.form))
    }
  }

  def login() = Action.async { implicit request =>
    LoginForm.form.bindFromRequest().fold(
      errorForm => {
        Future.successful(Ok(views.html.login(errorForm)))
      }, data => {
        service.login(data) map {
          case Success(user) =>
            Logger.debug("Login OK:" + user)
            userCache.set(USER_CACHE_KEY, user.toToken, 30 minutes)
            Redirect(routes.Application.index())
          case Failure(ex) =>
            Logger.debug("Login Fail:" + ex.getMessage)
            Redirect(routes.UserController.loginIndex()) //TODO: add text
        }
      })
  }

  def logout = Action { implicit request =>
    userCache.remove(USER_CACHE_KEY)
    Redirect(routes.Application.index())
  }

}
