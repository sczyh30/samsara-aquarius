package security

/**
  * Samsara Aquarius Utils
  * Encryptor
  */
object Encryptor {

  implicit class ImplicitEc(str: String) {

    import security.SHAImplicit.SHAUtils

    def encrypt() = {
      str.sha256()
    }

    def encryptSpecial = str.sha256()
  }

}
