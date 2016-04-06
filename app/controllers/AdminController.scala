package controllers

import javax.inject.{Singleton, Inject}

import entity.form.InfoForm
import service.{CategoryService, ArticleService}
import utils.FormConverter.infoConvert
import base.Constants._

import play.api.mvc._


import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  * Samsara Aquarius Route
  * Admin Controller
  *
  * @author sczyh30
  */
@Singleton
class AdminController @Inject() (articleService: ArticleService, categoryService: CategoryService) extends Controller {

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

  def addInfoPage() = Action.async { implicit request =>
    categoryService.fetchAll map { categories =>
      Ok(views.html.admin.articles.addInfo(InfoForm.form, categories))
    }
  }

  def addInfoProcess() = Action.async { implicit request =>
    InfoForm.form.bindFromRequest.fold(
      errorForm => {
        Future.successful(BadRequest(views.html.admin.articles.addInfo(errorForm, Seq())))
      },
      data => {
        articleService.add(data) map { res =>
          if (res >= 0)
            processOkResult(ADMIN_ADD_ARTICLE_SUCCESS)
          else
            processFailResult(ADMIN_ADD_ARTICLE_FAIL)
        }
      })
  }

}
