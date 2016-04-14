package controllers.api

import java.util.UUID
import javax.inject.{Inject, Singleton}

import base.Constants.WRAP_USER_KEY
import entity.{UserToken, User}
import entity.Results._
import service.UserService
import RestConverter.{userInfoFormat, resultFormat, userTokenFormat, ApiUserConverter}

import play.api.cache._
import play.api.libs.json.Json
import play.api.mvc.{Action, Controller}
import play.api.libs.concurrent.Execution.Implicits.defaultContext

import scala.util.{Failure, Success}
import scala.concurrent.duration._
import scala.language.postfixOps

/**
  * Samsara Aquarius API v1
  * User Controller
  *
  * @author sczyh30
  */
@Singleton
class ApiUserController @Inject() (@NamedCache("user-token-cache") tokenCache: CacheApi, service: UserService) extends Controller {

  implicit class TokenConverter(user: User) {
    def toToken: UserToken =
      UserToken(user.uid, user.username, UUID.randomUUID().toString)
  }

  def register() = TODO // control fuck request

  def login(username: String, password: String) = Action.async { implicit request =>
    service.login(username, password) map {
      case Success(user) =>
        val token = user.toToken
        tokenCache.set(WRAP_USER_KEY(token.token), token, 3 days)
        Ok(Json.toJson(token))
      case Failure(ex) =>
        Ok(Json.toJson(LOGIN_NOT_CORRECT))
    }
  }

  def fetch(uid: Int) = Action.async { implicit request =>
    service.fetch(uid) map {
      case Some(u) => Ok(Json.toJson(u.fit))
      case None => NotFound(Json.toJson(USER_NOT_FOUND))
    }
  }

  def update() = TODO

  def logout(token: String) = Action { implicit request =>
    val key = WRAP_USER_KEY(token)
    tokenCache.get[UserToken](key) match {
      case Some(x) => tokenCache.remove(key)
      case None => BadRequest(Json.toJson(TOKEN_VALIDATE_WRONG))
    }
    Ok(Json.toJson(USER_LOGOUT_SUCCESS))
  }

}
