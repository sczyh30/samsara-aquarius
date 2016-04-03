package controllers

import javax.inject.{Singleton, Inject}

import entity.form.RegisterForm
import base.Constants.USER_CACHE_KEY
import service.UserService
import utils.FormConverter.registerConvert

import play.api.mvc._
import play.api.cache.{CacheApi, NamedCache}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  * Samsara Aquarius Route
  * Register Controller
  *
  * @author sczyh30
  */
@Singleton
class RegisterController @Inject() (@NamedCache("user-cache") userCache: CacheApi, userService: UserService) extends Controller {

  /**
    * Register Index Page Route
    * <code>GET /register.now </code>
    */
  def index = Action { implicit request =>
    Ok(views.html.register(RegisterForm.form))
  }

  /**
    * Register Request Route
    * <code>POST /register</code>
    */
  def register() = Action.async { implicit request =>
    userCache.get(USER_CACHE_KEY) match {
      case Some(x) => Future.successful(Redirect(routes.UserController.index())) // if has logined
      case None =>
        RegisterForm.form.bindFromRequest.fold(
          errorForm => {
            Future.successful(Ok(views.html.register(errorForm))) //TODO: error form handler required
          },
          data => {
            userService.add(data) map { res =>
              Redirect(routes.UserController.index())
            }
          })
    }

  }

}
