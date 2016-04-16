package entity.form

import play.api.data.Form

/**
  * Form Trait</br>
  * This identifies that this entity is a kind of form
  */
trait FormTrait {
  val form: Form[_]
}
