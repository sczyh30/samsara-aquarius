package controllers

import javax.inject.Inject

import com.google.inject.Singleton
import play.api.mvc.Controller
import service.CategoryService

/**
  * Category Controller
  */
@Singleton
class CategoryController @Inject() (service: CategoryService) extends Controller {

  def index = TODO

  def certainId(cid: Int) = TODO

  def certain(name: String) = TODO

}
