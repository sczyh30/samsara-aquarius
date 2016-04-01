package utils

import java.sql.Date

import entity.{User, Article}
import entity.form._

import security.Encryptor.ImplicitEc

import scala.language.implicitConversions

/**
  * Samsara Aquarius Utils
  * Implicit Form Converter
  */
object FormConverter {

  implicit def infoConvert(data: InfoFormData): Article = {
    Article(0, data.title, url = data.url, cid = data.cid, updateDate = data.updateDate)
  }

  implicit def registerConvert(data: RegisterFormData): User = {
    User(uid = 0, username = data.username, password = data.password.encrypt(),
      joinDate = new Date(System.currentTimeMillis()), email = data.email)
  }

}
