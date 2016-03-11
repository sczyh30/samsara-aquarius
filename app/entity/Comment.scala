package entity

/**
  * Samsara Aquarius
  * Comment row case class
  *
  * @author sczyh30
  *
  * @param cid comment id
  * @param uid user id of the comment
  * @param dataId data id of the relevant comment
  * @param text comment text
  */
case class Comment(cid: Int, uid: Int, dataId: Int, text: String)

