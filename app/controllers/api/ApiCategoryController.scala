package controllers.api

import javax.inject.Inject

import com.google.inject.Singleton
import play.api.mvc.Controller
import service.CategoryService

/**
  * Api Category Controller
  */
@Singleton
class ApiCategoryController @Inject() (service: CategoryService) extends Controller {

  def fetchAll = TODO

}
