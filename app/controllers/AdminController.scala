package controllers

import java.time.LocalDateTime
import java.util.UUID
import javax.inject.{Inject, Singleton}

import entity.form.{InfoForm, LoginForm}
import service.{AdminService, CategoryService}
import utils.FormConverter.infoConvert
import base.Constants._
import base.action.MustBeAdminGo
import entity.Admin
import play.api.mvc._
import play.api.libs.concurrent.Execution.Implicits.defaultContext

import scala.concurrent.Future
import scala.language.postfixOps
import scala.util.{Failure, Success}

/**
  * Samsara Aquarius Route
  * Admin Controller
  *
  * @author sczyh30
  */
@Singleton
class AdminController @Inject() (admin: AdminService, cs: CategoryService) extends Controller {

  type PageMessage = (String, String)

  private def convertResult(code: Int): PageMessage = code match {
    case ADMIN_ADD_ARTICLE_SUCCESS => ("操作成功", "/adm1n6q9/add-article")
    case ADMIN_PROCESS_FAIL_UNKNOWN => ("操作失败", "/adm1n6q9")
    case ADMIN_ADD_ARTICLE_FAIL => ("添加文章失败", "/adm1n6q9/add-article")
  }

  def processOkResult(tp: Int)(implicit request: Request[AnyContent]): Result = {
    val msg = convertResult(tp)
    Ok(views.html.admin.processOK(msg._1, msg._2))
  }

  def processFailResult(tp: Int)(implicit request: Request[AnyContent]): Result = {
    val msg = convertResult(if (tp >= ADMIN_PROCESS_FAIL_UNKNOWN) tp
      else ADMIN_PROCESS_FAIL_UNKNOWN)
    Ok(views.html.admin.processFail(msg._1, msg._2))
  }

  def dashboard() = TODO

  def goIndex() = Action { implicit request =>
    request.session.get("adm1n_go_token") match {
      case Some(u) =>
        Redirect(routes.AdminController.dashboard())
      case None =>
        Ok(views.html.admin.go(LoginForm.form))
    }
  }

  implicit class Converter(admin: Admin) {
    def session: Session = {
      val session = Map("adm1n_go_aid" -> admin.id.toString, "adm1n_go_name" -> admin.name,
        "adm1n_go_token" -> UUID.randomUUID().toString,
        "aq_go_timestamp" -> LocalDateTime.now().toString)
      Session(session)
    }
  }

  def goAhead() = Action.async { implicit request =>
    LoginForm.form.bindFromRequest().fold(
      errorForm => {
        Future.successful(Redirect(routes.AdminController.goIndex()) flashing "adm1n_auth_error" -> "错误！错误！")
      }, data => {
        admin.login(data.username, data.password) map {
          case Success(a) =>
            Redirect(routes.AdminController.dashboard()) withSession a.session // will clean common user session!
          case Failure(ex) =>
            Redirect(routes.AdminController.goIndex()) flashing "adm1n_auth_error" -> "错误！错误！怎么搞的？"
        }
      })
  }

  def goAway() = Action { implicit request =>
    Redirect(routes.Application.index()) withNewSession
  }

  def addInfoPage() = MustBeAdminGo.async { implicit request =>
    cs.fetchAll map { categories =>
      Ok(views.html.admin.articles.addInfo(InfoForm.form, categories))
    }
  }

  def addInfoProcess() = MustBeAdminGo.async { implicit request =>
    InfoForm.form.bindFromRequest.fold(
      errorForm => {
        Future.successful(Redirect(routes.AdminController.addInfoPage()) flashing "add_article__error" -> "表单格式错误，请检查表单！")
      },
      data => {
        admin.addArticle(data) map { res =>
          if (res >= 0)
            processOkResult(ADMIN_ADD_ARTICLE_SUCCESS)
          else
            processFailResult(ADMIN_ADD_ARTICLE_FAIL)
        }
      })
  }

}
