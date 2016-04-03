package controllers

import javax.inject.{Singleton, Inject}

import play.api.mvc.Controller

import service.SearchService

/**
  * Samsara Aquarius Route
  * Search Controller
  *
  * @author sczyh30
  */
@Singleton
class SearchController @Inject() (service: SearchService) extends Controller {

}
