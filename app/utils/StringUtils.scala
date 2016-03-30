package utils

/**
  * Samsara Aquarius Utils
  * String Utils Object
  *
  * @author sczyh30
  */
object StringUtils {

  /**
    * Byte to array implicit converter
    *
    * @param bytes array of bytes
    */
  implicit class ByteToString(bytes: Array[Byte]) {

    /**
      * bytes to array
      *
      * @return the string
      */
    def bytes2hex(): String = Option(bytes) match {
      case Some(bs) =>
        val hexString: StringBuilder = new StringBuilder
        for (b <- bs) { //TODO: need to be more functional
          val strHex: String = Integer.toHexString(b & 0xFF)
          if (strHex.length < 2) {
            hexString.append(0)
          }
          hexString.append(strHex)
        }
        hexString.toString.toUpperCase
      case None => ""
    }
  }
}
