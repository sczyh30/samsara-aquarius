package filters

import javax.inject.Inject

import play.api.http.HttpFilters
import play.filters.headers.SecurityHeadersFilter

/**
  * Samsara Aquarius
  * Main HTTP Filter
  */
class Filters @Inject() (security: SecurityHeadersFilter) extends HttpFilters {

  override def filters = Seq(security)

}
