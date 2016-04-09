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

  def index() = Action.async { implicit request =>
    for {
      data <- articleService.fetchWithPage(0)
      pages <- articleService.calcPage
    } yield Ok(views.html.index(data, Page(1, pages, "/p")))
  }

  def page(page: Int) = Action.async { implicit request =>
    articleService.calcPage flatMap { pages =>
      if (IS_VALIDATE_PAGE(page, pages)) {
        articleService.fetchWithPage(WRAP_PAGE(page)) map { data =>
          Ok(views.html.index(data, Page(page, pages, "/p")))
        }
      }
      else
        Future.successful(NotFound(views.html.error.NotFound()))
    }
  }

  def share = Action { implicit request =>
    Ok(views.html.share(ShareForm.form))
  }

  def publishShare = Action.async { implicit request =>
    lazy val gtSdk = new GeetestLib(GeetestConfig.getCaptchaId, GeetestConfig.getPrivateKey)

    ShareForm.form.bindFromRequest().fold(
      errorForm => {
        Future.successful(Ok(views.html.share(errorForm)))
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
                Ok(views.html.share(ShareForm.form))
              else
                Ok(views.html.share(ShareForm.form)) // TODO: error info
            }
          case _ =>
            Future.successful(Ok(views.html.share(ShareForm.form))) // TODO: error info
        }
      }
    )
  }

  def about = Action { implicit request =>
    Ok(views.html.about())
  }

  def notFound = Action { implicit request =>
    NotFound(views.html.error.NotFound())
  }

}
