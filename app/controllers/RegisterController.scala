package controllers

import javax.inject.{Singleton, Inject}

import entity.form.RegisterForm
import service.UserService
import utils.FormConverter.registerConvert

import play.api.mvc._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  * Samsara Aquarius Route
  * Register Controller
  *
  * @author sczyh30
  */
@Singleton
class RegisterController @Inject() (userService: UserService) extends Controller{

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
    RegisterForm.form.bindFromRequest.fold(
      errorForm => {
        Future.successful(Ok(views.html.register(errorForm)))
      },
      data => {
        userService.add(data) map { res =>
          Redirect(routes.Application.index())
        }
      })
  }

}
