package entity.form

import play.api.data.Form
import play.api.data.Forms._

case class ChangeProfileFormData(tips: String, website: String, email: String)

/**
  * Change Profile Form Object
  */
object ChangeProfileForm extends FormTrait {

  override val form = Form(
    mapping(
      "tips" → nonEmptyText(minLength = 2),
      "website" → nonEmptyText(minLength = 4),
      "email" → email
    )(ChangeProfileFormData.apply)(ChangeProfileFormData.unapply)
  )
}
