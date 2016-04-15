package controllers

import java.time.LocalDateTime
import java.util.UUID
import javax.inject.{Inject, Singleton}

import base.action.{AuthenticatedAction, RequireNotLogin}
import entity.{User, UserToken}
import entity.form.{LoginForm, RegisterForm, ChangeProfileForm}
import service.{FavoriteService, UserService}
import utils.captcha.{GeetestConfig, GeetestLib}
import utils.FormConverter.registerConvert
import play.api.mvc._
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
class UserController @Inject()(service: UserService, fvs: FavoriteService) extends Controller {

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
  def loginIndex = RequireNotLogin { implicit request =>
    Ok(views.html.login(LoginForm.form))
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
            Redirect(routes.Application.index()) withSession user.session
          case Failure(ex) =>
            Redirect(routes.UserController.loginIndex()) flashing "login_error" -> "用户名或密码不正确。"
        }
      })
  }

  /**
    * User Center
    */
  def userCenter = AuthenticatedAction.async { implicit request =>
    val uid = request.session.get("uid").getOrElse("-1").toInt
    val unknownError = NotFound(views.html.error.NotFound()) // maybe 400?
    service.fetch(uid) map {
      case Some(user) =>
        Ok(views.html.user.center(user))
      case None =>
        NotFound(views.html.error.NotFound())
    } recover {
      case _: Exception => unknownError
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
  def regIndex = RequireNotLogin { implicit request =>
    Ok(views.html.register(RegisterForm.form))
  }

  /**
    * Register Request Route
    * <code>POST /register</code>
    */
  def register() = RequireNotLogin.async { implicit request =>
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

  /**
    * Logout Route
    */
  def logout = Action { implicit request =>
    Redirect(routes.Application.index()) withNewSession
  }

  def changeProfileIndex = AuthenticatedAction.async { implicit request =>
    val uid = request.session.get("uid").getOrElse("-1").toInt
    service fetch uid map {
      case Some(user) =>
        Ok(views.html.user.changeProfile(user.copy(password = ""), ChangeProfileForm.form))
      case None =>
        BadRequest
    }
  }

  def changePwdIndex = AuthenticatedAction { implicit request =>
    Ok(views.html.user.changePassword())
  }

  def changeProfile() = AuthenticatedAction.async { implicit request =>
    val uid = request.session.get("uid").getOrElse("-1").toInt
    ChangeProfileForm.form.bindFromRequest.fold(
      errorForm => {
        Future.successful(Redirect(routes.UserController.changeProfileIndex()) flashing "profile_error" -> "表单验证失败。")
      },
      data => {
        service.fetch(uid) flatMap {
          case Some(user) =>
            val up = user.copy(tips = Some(data.tips), website = Some(data.website), email = data.email)
            service.update(up) map { res =>
              if (res > 0)
                Redirect(routes.UserController.userCenter()) flashing "upload_success" -> "个人资料修改成功！"
              else
                Redirect(routes.UserController.changeProfileIndex()) flashing "profile_error" -> "表单验证失败。"
            }
          case None =>
            Future.successful(Redirect(routes.UserController.loginIndex()))
        }
      })
  }

  def changePwd() = TODO

  /**
    * Upload the avatar
    */
  def uploadAvatar = AuthenticatedAction.async(parse.multipartFormData) { implicit request => // in present version we do not reserve previous avatar!
    val redirect = Redirect(routes.UserController.userCenter())
    request.body.file("avatar_upload").map { picture =>
      import java.io.File
      val filename = picture.filename
      if (filename.endsWith(".png") || filename.endsWith(".jpg") || filename.endsWith(".jpeg")) {
        //val contentType = picture.contentType
        picture.ref.moveTo(new File(s"public/images/avatar/$filename"))
        val uid = request.session.get("uid").getOrElse("-1").toInt
        service.updateAvatar(uid, filename) map { res =>
          if (res > 0) Redirect(routes.UserController.userCenter()) flashing "upload_success" -> "头像修改成功！"
          else redirect flashing "upload_error" -> "文件不合要求，请重试！"
        }
      } else {
        Future.successful(redirect flashing "upload_error" -> "文件不合要求，请重试！")
      }
    } getOrElse {
      Future.successful(redirect flashing "upload_error" -> "文件错误！")
    }
  }

}
