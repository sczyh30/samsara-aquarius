package controllers

import javax.inject.{Singleton, Inject}

import service.ArticleService

import play.api.mvc._

import play.api.libs.concurrent.Execution.Implicits.defaultContext

@Singleton
class Application @Inject() (articleService: ArticleService) extends Controller {

  def index = Action.async { implicit request =>
    articleService.fetchAll map { data =>
      Ok(views.html.index(data))
    }

  }

}
