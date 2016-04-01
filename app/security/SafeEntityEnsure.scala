package security

import entity.Comment
import utils.StringUtils.StringImplicit

/**
  * Safe Entity Ensure
  */
object SafeEntityEnsure {

  implicit class SafeCommentEnsure(c: Comment) extends SafeEnsure[Comment] {
    /**
      * Escape html special chars from the comment the text
      * Prevent basic XSS
      *
      * @return the escaped text
      */
    def safe = c.copy(text = c.text.escapeHtml())
  }

}
