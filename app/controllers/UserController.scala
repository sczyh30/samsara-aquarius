package controllers

import javax.inject.{Singleton, Inject}

import base.Constants.USER_CACHE_KEY
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

  def index = Action { implicit request =>
    Ok(views.html.login(LoginForm.form))
  }

  def login() = Action.async { implicit request =>
    LoginForm.form.bindFromRequest().fold(
      errorForm => {
        Future.successful(Ok(views.html.login(errorForm)))
      }, data => {
        service.login(data) map {
          case Success(user) =>
            println("Login OK:" + user)
            userCache.set(USER_CACHE_KEY, user, 30 minutes)
            Redirect(routes.Application.index())
          case Failure(ex) =>
            println("Login Fail:" + ex.getMessage)
            Redirect(routes.UserController.index()) //TODO: add text
        }
      })
  }

  def logout = Action { implicit request =>
    userCache.remove(USER_CACHE_KEY)
    Redirect(routes.Application.index())
  }

}
