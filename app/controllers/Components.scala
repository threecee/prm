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

  def createComponentForPowerUnit(powerUnitId:Long) = Action(parse.json) { implicit request =>
    val state = request.body.validate[Component]
    if(state.isSuccess)
    {
     val id = Component.addForPowerUnit(powerUnitId, state.get)
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


  def createComponentForPowerStation(powerStationId:Long) = Action(parse.json) { implicit request =>
    val state = request.body.validate[Component]
    if(state.isSuccess)
    {
      val id = Component.addForPowerStation(powerStationId, state.get)
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


  def updateComponentForPowerStation(powerStationId: Long) = Action(parse.json) { implicit request =>
    val state = request.body.validate[Component]
    if(state.isSuccess)
    {
      Component.updateForPowerStation(powerStationId, state.get)
      Accepted(Json.toJson(state.get))
    }
    else {
      prettyPrintError(state)
      BadRequest(request.body)
    }
  }
  def updateComponentForPowerUnit(powerUnitId: Long) = Action(parse.json) { implicit request =>
    val state = request.body.validate[Component]
    if(state.isSuccess)
    {
      Component.updateForPowerUnit(powerUnitId, state.get)
      Accepted(Json.toJson(state.get))
    }
    else {
      prettyPrintError(state)
      BadRequest(request.body)
    }
  }



  def updateComponentForPowerStationByValues(powerStationId: Long, componentTypeId:Long) = Action(parse.json) { implicit request =>
    val state = request.body.validate[Component]
    if(state.isSuccess)
    {
      Component.updateForPowerStation(powerStationId, state.get)
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
