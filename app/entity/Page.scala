package entity

/**
  * Page Entity
  * @param now current page number
  * @param all total page number
  * @param path direct path
  */
case class Page(now: Int, all: Int, path: String)
