package security

import java.security.{NoSuchAlgorithmException, MessageDigest}

/**
  * Samsara Aquarius Utils
  * SHA Encryption Util Object
  * contains implicit SHA1,SHA256,SHA512 encrypt
  *
  * @author sczyh30
  */
object SHAImplicit {

  /**
    * SHA Utils Typeclass
    * @param str string
    */
  implicit class SHAUtils(str: String) {

    private val SHA1 = "SHA-1"
    private val SHA256 = "SHA-256"
    private val SHA512 = "SHA-512"

    private def hash(str: Option[String], `type`: String): String = {
      import utils.StringUtilImplicit.ByteToString
      if (str.isDefined) {
        try {
          val digest: MessageDigest = MessageDigest.getInstance(`type`)
          digest.update(str.get.getBytes)
          val messageDigest: Array[Byte] = digest.digest
          messageDigest.bytes2hex()
        }
        catch {
          case e: NoSuchAlgorithmException =>
            e.printStackTrace()
        }
      } else {
        throw new NullPointerException("Null string") //TODO:Should not throw NullPointerException!
      }
      ""
    }

    /**
      * Use SHA-1 algorithm to hash the string.
      *
      * @return encrypted string
      */
    def sha1() = hash(Some(str), SHA1)

    /**
      * Use SHA-256 algorithm to hash the string.
      *
      * @return encrypted string
      */
    def sha256() = hash(Some(str), SHA256)

    /**
      * Use SHA-512 algorithm to hash the string.
      *
      * @return encrypted string
      */
    def sha512() = hash(Some(str), SHA512)

    /**
      * Use SHA-256 algorithm (with salt) to hash the string.
      *
      * @param salt the salt string
      * @return encrypted string
      */
    //TODO:TO BE ACCOMPLISHED
    def sha256(salt: String) = ???

  }

}
