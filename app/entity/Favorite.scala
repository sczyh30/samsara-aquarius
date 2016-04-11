package entity

/**
  * Favorite Relation Entity
  * Primary key: (articleId, likeUid)
  * @since 0.3.12
  *
  * @param articleId article id
  * @param likeUid corresponding user id
  * @param ctime action time
  */
case class Favorite(articleId: Int, likeUid: Int, ctime: Option[java.sql.Timestamp] = None)
