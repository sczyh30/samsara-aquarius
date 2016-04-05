package entity

/**
  * Samsara Aquarius
  * Process Result Entity
  *
  * @param code result code
  * @param msg result message
  */
case class ProcessResult(code: Int, msg: String)

/**
  * Results Factory
  */
object Results {
  val TOKEN_VALIDATE_WRONG = ProcessResult(4444, "token_validate_wrong")

  val ARTICLE_NOT_FOUND = ProcessResult(4001, "article_not_found")
  val USER_NOT_FOUND = ProcessResult(4204, "user_not_found")
  val LOGIN_NOT_CORRECT = ProcessResult(4201, "user_login_not_correct")
  val USER_LOGOUT_SUCCESS = ProcessResult(2211, "user_logout_success")
}
