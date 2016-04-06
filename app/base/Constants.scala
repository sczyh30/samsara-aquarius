package base

/**
  * Samsara Aquarius
  * Constants
  */
object Constants {
  // cache key prefix
  private val USER_TOKEN_CACHE_KEY_PREFIX = "USER_"

  val WRAP_USER_KEY = (token: String) => USER_TOKEN_CACHE_KEY_PREFIX + token

  // API version
  val AQUARIUS_API_VERSION = "v1"

  // database process code
  val DB_ADD_DUPLICATE = -4

  // admin process code
  val ADMIN_ADD_ARTICLE_SUCCESS = 2611
  val ADMIN_PROCESS_FAIL_UNKNOWN = 2640
  val ADMIN_ADD_ARTICLE_FAIL = 2641

}
