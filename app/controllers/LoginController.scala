package controllers

import javax.inject.{Singleton, Inject}

import entity.form.LoginForm
import play.api.Logger
import service.UserService

import play.api.mvc._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success}

/**
  * Samsara Aquarius Route
  * Login Controller
  *
  * @author sczyh30
  */
@Singleton
class LoginController @Inject() (service: UserService) extends Controller {

  def index = Action { implicit request =>
    Ok(views.html.login(LoginForm.form))
  }

  def login() = Action.async { implicit request =>
    LoginForm.form.bindFromRequest().fold(
      errorForm => {
        Future.successful(Ok(views.html.login(errorForm)))
      }, data => {
        service.login(data) map {
          case Success(x) =>
            println("Login OK:" + data)
            Redirect(routes.Application.index()) //TODO: add login state
          case Failure(ex) =>
            println("Login Fail:" + ex.getMessage)
            Redirect(routes.LoginController.index()) //TODO: add text
        }
      })
  }

}
