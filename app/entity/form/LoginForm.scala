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
      "username" → nonEmptyText,
      "password" → nonEmptyText
    )(LoginFormData.apply)(LoginFormData.unapply)
  )
}
