package entity.form

import play.api.data.Form

/**
  * Form Trait
  * identifies that this entity is a kind of form
  */
trait FormTrait {
  val form: Form[_]
}
