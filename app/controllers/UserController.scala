package controllers

import java.time.LocalDateTime
import java.util.UUID
import javax.inject.{Inject, Singleton}

import base.action.{AuthenticatedAction, RequireNotLogin}
import base.Constants.FormErrorFlags._
import base.Constants.FormMsgCHS._
import entity.{User, UserToken}
import entity.form.{ChangeProfileForm, ChangePwdForm, LoginForm, RegisterForm}
import service.{FavoriteService, UserService}
import utils.captcha.{GeetestConfig, GeetestLib}
import security.Encryptor.ImplicitEc
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

  /**
    * Typeclass for user entity<br/>
    * This is a generator that generates user session and token
    */
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
    def wrong(msg: String) =
      Redirect(routes.UserController.loginIndex()) flashing LOGIN_ERROR_FLAG -> msg
    LoginForm.form.bindFromRequest().fold(
      errorForm => {
        Future.successful(wrong(LOGIN_FORM_LENGTH_ERR))
      }, data => {
        service.login(data.username, data.password) map {
          case Success(user) =>
            Redirect(routes.Application.index()) withSession user.session
          case Failure(ex) =>
            wrong(LOGIN_FAIL_WR)
        }
      })
  }

  /**
    * User Center Page
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
    def wrong(msg: String) =
      Redirect(routes.UserController.regIndex()) flashing REG_ERROR_FLAG -> msg

    RegisterForm.form.bindFromRequest.fold(
      errorForm => {
        Future.successful(wrong(FORM_ERROR))
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
                wrong(REG_ERR_EXIST)
            }
          case _ =>
            Future.successful(wrong(CAPTCHA_ERROR))
        }
      })
  }

  /**
    * Logout Route
    */
  def logout = Action { implicit request =>
    Redirect(routes.Application.index()) withNewSession
  }

  /**
    * User Profile Modify Page
    * <code>GET /u/profile</code>
    */
  def changeProfileIndex = AuthenticatedAction.async { implicit request =>
    val uid = request.session.get("uid").getOrElse("-1").toInt
    service fetch uid map {
      case Some(user) =>
        Ok(views.html.user.changeProfile(user.copy(password = ""), ChangeProfileForm.form))
      case None =>
        Redirect(routes.UserController.loginIndex())
    }
  }

  /**
    * User Password Modify Page
    * <code>GET /u/pwd</code>
    */
  def changePwdIndex = AuthenticatedAction { implicit request =>
    Ok(views.html.user.changePassword(ChangePwdForm.form)) //TODO: could be more safe(e.g. captcha)
  }

  /**
    * User Profile Modify Process
    * <code>POST /u/profile</code>
    */
  def changeProfile() = AuthenticatedAction.async { implicit request =>
    val uid = request.session.get("uid").getOrElse("-1").toInt
    def wrong(msg: String) =
      Redirect(routes.UserController.changeProfileIndex()) flashing PROFILE_ERROR_FLAG -> msg
    ChangeProfileForm.form.bindFromRequest.fold(
      errorForm => {
        Future.successful(wrong(FORM_ERROR))
      },
      data => {
        service.fetch(uid) flatMap {
          case Some(user) =>
            val up = user.copy(tips = Some(data.tips), website = Some(data.website), email = data.email)
            service.update(up) map { res =>
              if (res > 0)
                Redirect(routes.UserController.userCenter()) flashing "upload_success" -> "个人资料修改成功！"
              else
                wrong(FORM_UNKNOWN)
            }
          case None =>
            Future.successful(Redirect(routes.UserController.loginIndex()))
        }
      })
  }

  /**
    * User Password Modify Process
    * <code>POST /u/pwd</code>
    */
  def changePwd() = AuthenticatedAction.async { implicit request => //TODO: ugly code, not functional! TO-REFACTOR-REVIEW
    def wrong(msg: String) =
      Future.successful(Redirect(routes.UserController.changePwdIndex()) flashing CG_PWD_FLAG -> msg)

    val uid = request.session.get("uid").getOrElse("-1").toInt
    ChangePwdForm.form.bindFromRequest.fold(
      errorForm => {
        wrong(FORM_ERROR)
      },
      data => {
        if (!data.new_pwd.equals(data.new_pwd_r)) {
          wrong(PWD_RECHECK_WRONG)
        } else {
          service.fetch(uid) flatMap {
            case Some(user) =>
              if (data.old_pwd.encrypt().equals(user.password)) {
                val up = user.copy(password = data.new_pwd.encrypt())
                service.update(up) map { res =>
                  if (res > 0)
                    Redirect(routes.UserController.userCenter()) flashing "upload_success" -> "密码修改成功！"
                  else
                    Redirect(routes.UserController.changePwdIndex()) flashing CG_PWD_FLAG -> FORM_UNKNOWN
                }
              } else
                wrong(PWD_CHECK_ORIGIN_WRONG)
            case None =>
              Future.successful(Redirect(routes.UserController.loginIndex()))
          }
        }
      })
  }

  /**
    * User Avatar Upload Process
    * TODO: ugly code, not functional! TO-REFACTOR-REVIEW
    */
  def uploadAvatar = AuthenticatedAction.async(parse.multipartFormData) { implicit request => // in present version we do not reserve previous avatar!
    val redirect = Redirect(routes.UserController.userCenter())
    def wrong(msg: String) =
      Future.successful(redirect flashing UPLOAD_ERROR_FLAG -> msg)

    request.body.file("avatar_upload").map { picture =>
      import java.io.File
      val filename = picture.filename
      if (filename.endsWith(".png") || filename.endsWith(".jpg") || filename.endsWith(".jpeg")) {
        //val contentType = picture.contentType
        picture.ref.moveTo(new File(s"public/images/avatar/$filename"))
        val uid = request.session.get("uid").getOrElse("-1").toInt
        service.updateAvatar(uid, filename) map { res =>
          if (res > 0)
            Redirect(routes.UserController.userCenter()) flashing "upload_success" -> "头像修改成功！"
          else
            redirect flashing UPLOAD_ERROR_FLAG -> FILE_NOT_SUITABLE
        }
      } else {
        wrong(FILE_NOT_SUITABLE)
      }
    } getOrElse {
      wrong(FILE_ERROR)
    }
  }

}
