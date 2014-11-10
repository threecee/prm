package controllers

import controllers.base.BaseController
import models.classification.Component
import play.api.libs.json._
import play.api.mvc._

object Components extends BaseController  {


  def components() = Action(parse.empty){ implicit request =>
    Ok(Json.toJson(Component.findAll))
  }
  def component(id: Long) = Action(parse.empty){ implicit request =>
     Ok(Json.toJson(Component.find(id)))
  }

  def createComponent(powerUnitId:Long) = Action(parse.json) { implicit request =>
    val state = request.body.validate[Component]
    if(state.isSuccess)
    {
     val id = Component.add(powerUnitId, state.get)
      if(id.isDefined) {
        Created(Json.toJson(Component.find(id.get)))
      }
      else {
        BadRequest(request.body)
      }
    }
    else {
      prettyPrintError(state)
      BadRequest(request.body)
    }
  }

  def updateComponent(powerUnitId: Long) = Action(parse.json) { implicit request =>
    val state = request.body.validate[Component]
    if(state.isSuccess)
    {
      Component.update(powerUnitId, state.get)
      Accepted(Json.toJson(state.get))
    }
    else {
      prettyPrintError(state)
      BadRequest(request.body)
    }
  }

  def deleteComponent(id: Long) = Action(parse.empty) { implicit request =>
    val state = Component.find(id)
    if(state.isDefined)
    {
      Component.delete(id)
      Ok(Json.toJson(id))
    }
    else {
      BadRequest(Json.toJson(id))
    }
  }

}
