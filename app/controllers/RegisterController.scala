package controllers

import java.sql.Date
import javax.inject.Inject

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
class RegisterController @Inject() extends Controller{

  def index = Action {
    Ok(views.html.register(RegisterForm.form))
  }

  def register() = Action.async { implicit request =>
    RegisterForm.form.bindFromRequest.fold(
      errorForm => Future.successful(Ok(views.html.register(errorForm))),
      data => {
        val newUser = User(uid = 0, username = data.username, password = data.password.sha256(),
          joinDate = new Date(System.currentTimeMillis()), email = data.email)
        UserService.add(newUser) map { res =>
          Redirect(routes.Application.index())
        }
      }
    )
  }

}
