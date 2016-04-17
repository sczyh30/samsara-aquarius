package controllers

import javax.inject.{Singleton, Inject}

import entity.Page
import entity.form.ShareForm
import service.ArticleService
import base.Constants.{WRAP_PAGE, IS_VALIDATE_PAGE}

import play.api.mvc._
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import utils.captcha.{GeetestConfig, GeetestLib}

import scala.concurrent.Future

/**
  * Samsara Aquarius
  * Main Page Controller
  *
  * @author sczyh30
  */
@Singleton
class Application @Inject()(articleService: ArticleService) extends Controller {

  /**
    * Samsara Aquarius Application Index page (Articles)
    */
  def index() = Action.async { implicit request =>
    for {
      data <- articleService.fetchWithPage(0)
      pages <- articleService.calcPage
    } yield Ok(views.html.index(data, Page(1, pages, "/p")))
  }

  /**
    * Samsara Aquarius Application (Articles with page)
    */
  def page(page: Int) = Action.async { implicit request =>
    articleService.calcPage flatMap { pages =>
      if (IS_VALIDATE_PAGE(page, pages)) {
        articleService.fetchWithPage(WRAP_PAGE(page)) map { data =>
          Ok(views.html.index(data, Page(page, pages, "/p")))
        }
      }
      else
        Future.successful(Redirect(routes.Application.notFound()))
    }
  }

  /**
    * Samsara Aquarius Application
    * Share Page
    */
  def share = Action { implicit request =>
    Ok(views.html.share(ShareForm.form))
  }

  /**
    * Samsara Aquarius Application
    * Share Process
    */
  def publishShare = Action.async { implicit request =>
    utils.DateUtils.ensureSession
    lazy val gtSdk = new GeetestLib(GeetestConfig.getCaptchaId, GeetestConfig.getPrivateKey)

    ShareForm.form.bindFromRequest().fold(
      errorForm => {
        Future.successful(Redirect(routes.Application.share()) flashing "share_error" -> "表单格式错误，请检查表单。")
      }, data => {
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
          case 1 => // validate success
            val wrapped = entity.Share(0, data.title, data.url, request.session.get("uid").map(_.toInt))
            articleService.shareArticle(wrapped) map { res =>
              if (res >= 0)
                Ok(views.html.processOk("提交成功", "您的分享已提交成功，管理员将进行评估。"))
              else
                Ok(views.html.error.ServerError("?!?", "服务器似乎出了点问题。。。请重试。。。"))
            }
          case _ =>
            Future.successful(Redirect(routes.Application.share()) flashing "share_error" -> "安全验证失败，请检查验证码。")
        }
      }
    )
  }

  /**
    * Samsara Aquarius Application
    * About Page
    */
  def about = Action { implicit request =>
    Ok(views.html.about())
  }

  /**
    * Samsara Aquarius Application
    * Not Found Page
    */
  def notFound = Action { implicit request =>
    NotFound(views.html.error.NotFound())
  }

}
