package base

import entity.{Comment, Category, Article}

/**
  * Samsara Aquarius
  * Constants
  */
object Constants {
  // Samsara Aquarius Dev Version
  val AQUARIUS_DEV_VERSION = "0.3.x"
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

}
