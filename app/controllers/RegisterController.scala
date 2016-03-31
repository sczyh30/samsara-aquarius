package controllers

import java.sql.Date
import javax.inject.{Singleton, Inject}

import entity.User
import entity.form.RegisterForm
import security.SHAImplicit.SHAUtils
import service.UserService

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
        val newUser = User(uid = 0, username = data.username, password = data.password.sha256(),
          joinDate = new Date(System.currentTimeMillis()), email = data.email)
        println(newUser)
        userService.add(newUser) map { res =>
          Redirect(routes.Application.index())
        }
      })
  }

}
