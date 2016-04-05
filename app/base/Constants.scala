package base

/**
  * Constants
  */
object Constants {
  private val USER_CACHE_KEY_PREFIX = "user_"

  val USER_CACHE_KEY = "user"

  val WRAP_USER_KEY = (uid: Int) => USER_CACHE_KEY_PREFIX + uid

  val AQUARIUS_API_VERSION = "v1"
}
