package entity.form

import play.api.data.Form
import play.api.data.Forms._


case class ChangePwdFormData(old_pwd: String, new_pwd: String, new_pwd_r: String)

/**
  * Change Password Form Object
  */
object ChangePwdForm extends FormTrait {

  override val form = Form(
    mapping(
      "old_pwd" → nonEmptyText(minLength = 8, maxLength = 16),
      "new_pwd" → nonEmptyText(minLength = 8, maxLength = 16),
      "new_pwd_r" → nonEmptyText(minLength = 8, maxLength = 16)
    )(ChangePwdFormData.apply)(ChangePwdFormData.unapply)
  )
}
