package controllers

import java.util.UUID
import javax.inject.{Singleton, Inject}

import entity.{UserToken, User}
import entity.form.{RegisterForm, LoginForm}
import service.UserService
import utils.captcha.{GeetestConfig, GeetestLib}
import utils.FormConverter.registerConvert

import play.api.mvc._
import play.api.Logger
import play.api.libs.concurrent.Execution.Implicits.defaultContext

import scala.concurrent.Future
import scala.util.{Failure, Success}

/**
  * Samsara Aquarius Route
  * User Controller
  *
  * @author sczyh30
  */
@Singleton
class UserController @Inject() (service: UserService) extends Controller {

  lazy val gtSdk = new GeetestLib(GeetestConfig.getCaptchaId, GeetestConfig.getPrivateKey)

  implicit class UserConverter(user: User) {
    def toToken: UserToken =
      UserToken(user.uid, user.username, UUID.randomUUID().toString)

    def session: Session = {
      val session = Map("uid" -> user.uid.toString, "username" -> user.username,
        "aq_token" -> UUID.randomUUID().toString)
      Session(session)
    }
  }

  /**
    * Login Index Page Route
    * <code>GET /login.now </code>
    */
  def loginIndex = Action { implicit request => //TODO:(EM)FATAL ERROR ON CACHE, MUST BE FIXED!! USE GLOBAL CACHE CAREFULLY
    request.session.get("aq_token") match {
      case Some(user) => Redirect(routes.Application.index())
      case None => Ok(views.html.login(LoginForm.form))
    }
  }

  /**
    * Login Request Route
    * <code>POST /login</code>
    */
  def login() = Action.async { implicit request =>
    LoginForm.form.bindFromRequest().fold(
      errorForm => {
        Future.successful(Ok(views.html.login(errorForm)))
      }, data => {
        service.login(data.username, data.password) map {
          case Success(user) =>
            Logger.debug(s"Login OK:$user")
            //userCache.set(USER_CACHE_KEY, user.toToken, 30 minutes)
            Redirect(routes.Application.index()) withSession user.session //TODO: need session expired time
          case Failure(ex) =>
            Logger.debug(s"Login Fail:${ex.getMessage}")
            Redirect(routes.UserController.loginIndex()) //TODO: add text
        }
      })
  }

  /**
    * Register Index Page Route
    * <code>GET /register.now </code>
    */
  def regIndex = Action { implicit request =>
    Ok(views.html.register(RegisterForm.form))
  }

  /**
    * Register Request Route
    * <code>POST /register</code>
    */
  def register() = Action.async { implicit request =>
    request.session.get("aq_token") match {
      case Some(x) => Future.successful(Redirect(routes.UserController.loginIndex())) // if has logined
      case None =>
        RegisterForm.form.bindFromRequest.fold(
          errorForm => {
            Future.successful(Ok(views.html.register(errorForm))) //TODO: error form handler required
          },
          data => {
            // validate the captcha

            service.add(data) map { res =>
              if (res > 0)
                Redirect(routes.UserController.loginIndex())
              else
                Ok(views.html.register(RegisterForm.form)) //TODO: error form handler required
            }
          })
    }
  }

  /**
    * Logout Route
    */
  def logout = Action { implicit request =>
    //userCache.remove(USER_CACHE_KEY)
    Redirect(routes.Application.index()) withNewSession
  }

}
