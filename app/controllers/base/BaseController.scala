package controllers.base

import play.api.Logger
import play.api.libs.json._
import play.api.mvc._

class BaseController extends Controller  {

  def prettyPrintError(state: JsResult[Any]) {
    Logger.debug(state.recoverTotal(e => JsError.toFlatJson(e)).toString)
  }


}
