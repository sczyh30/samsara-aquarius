package controllers

import java.time.LocalDateTime
import java.util.UUID
import javax.inject.{Singleton, Inject}

import entity.{UserToken, User}
import entity.form.{RegisterForm, LoginForm}
import service.{FavoriteService, UserService}
import utils.captcha.{GeetestConfig, GeetestLib}
import utils.FormConverter.registerConvert

import play.api.mvc._
import play.api.Logger
import play.api.libs.concurrent.Execution.Implicits.defaultContext

import scala.concurrent.Future
import scala.language.postfixOps
import scala.util.{Failure, Success}

/**
  * Samsara Aquarius Route
  * User Controller
  *
  * @author sczyh30
  */
@Singleton
class UserController @Inject() (service: UserService, fvs: FavoriteService) extends Controller {

  lazy val gtSdk = new GeetestLib(GeetestConfig.getCaptchaId, GeetestConfig.getPrivateKey)

  implicit class UserConverter(user: User) {
    def toToken: UserToken =
      UserToken(user.uid, user.username, UUID.randomUUID().toString)

    def session: Session = {
      val session = Map("uid" -> user.uid.toString, "username" -> user.username,
        "aq_token" -> UUID.randomUUID().toString,
        "timestamp" -> LocalDateTime.now().toString)
      Session(session)
    }
  }

  /**
    * Login Index Page Route
    * <code>GET /login.now </code>
    */
  def loginIndex = Action { implicit request =>
    utils.DateUtils.ensureSession
    request.session.get("aq_token") match {
      case Some(user) =>
        Redirect(routes.Application.index())
      case None =>
        Ok(views.html.login(LoginForm.form))
    }
  }

  /**
    * Login Request Route
    * <code>POST /login</code>
    */
  def login() = Action.async { implicit request =>
    LoginForm.form.bindFromRequest().fold(
      errorForm => {
        Future.successful(Redirect(routes.UserController.loginIndex()) flashing "login_error" -> "用户名或密码长度错误。")
      }, data => {
        service.login(data.username, data.password) map {
          case Success(user) =>
            Logger.debug(s"Login OK:$user")
            Redirect(routes.Application.index()) withSession user.session
          case Failure(ex) =>
            Logger.debug(s"Login Fail:${ex.getMessage}")
            Redirect(routes.UserController.loginIndex()) flashing "login_error" -> "用户名或密码不正确。"
        }
      })
  }

  /**
    * User Center
    */
  def userCenter = Action.async { implicit request =>
    utils.DateUtils.ensureSession
    val userSession = request.session.get("uid")
    val unknownError = NotFound(views.html.error.NotFound()) // maybe 400?
    userSession match {
      case Some(uid) =>
        service.fetch(uid.toInt) map {
          case Some(user) =>
            Ok(views.html.user.center(user))
          case None =>
            NotFound(views.html.error.NotFound())
        } recover {
          case _: Exception => unknownError
        }
      case None =>
        Future.successful(Ok(views.html.login(LoginForm.form)))
    }
  }

  /**
    * User Info Page
    *
    * @param username username
    */
  def userInfo(username: String) = Action.async { implicit request =>
    service.fetchByName(username) flatMap {
      case Some(user) =>
        fvs fetchUserFavorite user.uid map { arcs =>
          Ok(views.html.user.user(user.copy(password = null), arcs))
        }
      case None =>
        Future.successful(NotFound(views.html.error.NotFound()))
    }
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
            Future.successful(Redirect(routes.UserController.regIndex()) flashing "reg_error" -> "表单安全验证失败。")
          },
          data => {
            // validate the captcha
            val gtResult = {
              request.session.get(gtSdk.gtServerStatusSessionKey).get.toInt match {
                case 1 =>
                  gtSdk.enhencedValidateRequest(data.geetest_challenge,
                    data.geetest_validate, data.geetest_seccode);
                case _ =>
                  gtSdk.failbackValidateRequest(data.geetest_challenge,
                    data.geetest_validate, data.geetest_seccode);
              }
            }
            gtResult match {
              case 1 =>
                service.add(data) map { res =>
                  if (res > 0)
                    Redirect(routes.UserController.loginIndex()) // may be more friendly
                  else
                    Redirect(routes.UserController.regIndex()) flashing "reg_error" -> "注册失败：用户已存在。"
                }
              case _ =>
                Future.successful(Redirect(routes.UserController.regIndex()) flashing "reg_error" -> "安全验证失败。")
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

  /**
    * Upload the avatar
    */
  def uploadAvatar = Action(parse.multipartFormData) { implicit request =>
    request.body.file("avatar_upload").map { picture => //TODO: NOT SAFE, MUST ONLY BE PIC. MUST FIX IN 0.5.x!
      import java.io.File
      val filename = picture.filename
      //val contentType = picture.contentType
      picture.ref.moveTo(new File(s"/assets/images/avatar/$filename"))
      Ok("OK")

    } getOrElse {
      Redirect(routes.UserController.userCenter()) flashing "error" -> "missing file"
    }
  }

}
