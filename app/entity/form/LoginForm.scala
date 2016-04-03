package entity.form

import play.api.data.Form
import play.api.data.Forms._

case class LoginFormData(username: String, password: String)

/**
  * Login Form Object
  */
object LoginForm extends FormTrait {

  val form = Form(
    mapping(
      "username" → nonEmptyText(minLength = 4, maxLength = 14),
      "password" → nonEmptyText(minLength = 8, maxLength = 16)
    )(LoginFormData.apply)(LoginFormData.unapply)
  )
}
