package base

import entity.{Comment, Category, Article}

/**
  * Samsara Aquarius
  * Constants
  */
object Constants {
  // Samsara Aquarius Dev Version
  val AQUARIUS_DEV_VERSION = "0.7.6-B4"
  // API version
  val AQUARIUS_API_VERSION = "v1"
  // Dev mode flag(1: dev, 0: dist, 24: em)
  val AQUARIUS_DEBUG_MODE_FLAG = 1

  // cache key prefix
  private val USER_TOKEN_CACHE_KEY_PREFIX = "USER_"

  val WRAP_USER_KEY = (token: String) => USER_TOKEN_CACHE_KEY_PREFIX + token

  // page
  val LIMIT_PAGE = 10
  val WRAP_PAGE = (page: Int) => LIMIT_PAGE * (page - 1)
  val IS_VALIDATE_PAGE = (page: Int, all: Int) => {
    if (page > 0 && page <= all) true else false
  }

  // database process code
  val DB_ADD_DUPLICATE = -4

  // admin process code
  val ADMIN_ADD_ARTICLE_SUCCESS = 2611
  val ADMIN_PROCESS_FAIL_UNKNOWN = 2640
  val ADMIN_ADD_ARTICLE_FAIL = 2641

  // Custom Types
  type CCPT = (Category, Seq[(Article, Int)]) // CategoryCertainPageType
  type IndexArticleRes = (Article, Category, Int)
  type SCPT = (Seq[IndexArticleRes], String) // SearchCertainPageType
  type UserCommentInfo = (String, Option[String])
  type CommentPageInfo = Seq[(Comment, UserCommentInfo)]

  // message system
  val ARTICLE_LIKE_ACTOR_NAME = "article-favorite-actor"

  /**
    * Form Error Flag
    */
  object FormErrorFlags {
    val LOGIN_ERROR_FLAG = "login_error"
    val UPLOAD_ERROR_FLAG = "upload_error"
    val REG_ERROR_FLAG = "reg_error"
    val PROFILE_ERROR_FLAG = "profile_error"
    val CG_PWD_FLAG = "cg_pwd_error"
  }


  object FormMsgCHS {
    val FORM_UNKNOWN = "未知错误，请重试。"
    val FORM_ERROR = "表单错误，请检查表单。"
    val PWD_RECHECK_WRONG = "两次密码输入不一致。"
    val PWD_CHECK_ORIGIN_WRONG = "原密码错误！"
    val FILE_ERROR = "文件错误，请重试。"
    val FILE_NOT_SUITABLE = "文件不合要求，请重试！"
    val REG_ERR_EXIST = "注册失败：用户已存在。"
    val CAPTCHA_ERROR = "安全验证失败。"
    val LOGIN_FORM_LENGTH_ERR = "用户名或密码长度错误。"
    val LOGIN_FAIL_WR = "用户名或密码不正确。"
  }

}
