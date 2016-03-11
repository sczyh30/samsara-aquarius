package utils

/**
  * Samsara Aquarius Utils
  * String Util Object
  * contains implicit class
  * @author sczyh30
  */
object StringUtilImplicit {

  /**
    * Byte to array implicit converter
    * @param bytes array of bytes
    */
  implicit class ByteToString(bytes: Array[Byte]) {

    /**
      * bytes to array
      * @return the string
      */
    def bytes2hex(): String = {
      //TODO: needs to be more functional
      if (bytes != null) {
        val hexString: StringBuilder = new StringBuilder
        for (b <- bytes) {
          val strHex: String = Integer.toHexString(b & 0xFF)
          if (strHex.length < 2) {
            hexString.append(0)
          }
          hexString.append(strHex)
        }
        hexString.toString.toUpperCase
      }
      else ""
    }
  }
}
