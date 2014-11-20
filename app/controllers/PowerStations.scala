package controllers

import controllers.base.BaseController
import models.classification.PowerStation
import play.api.libs.json._
import play.api.mvc._

object PowerStations extends BaseController  {


  def powerStations() = Action(parse.empty){ implicit request =>
    Ok(Json.toJson(PowerStation.findAll))
  }
  def powerStationsNoDeps() = Action(parse.empty){ implicit request =>
    Ok(Json.toJson(PowerStation.findAllNoDeps()))
  }
  def powerStation(id: Long) = Action(parse.empty){ implicit request =>
    Ok(Json.toJson(PowerStation.find(id)))
  }

  def createPowerStation() = Action(parse.json) { implicit request =>
    val state = request.body.validate[PowerStation]
    if(state.isSuccess)
    {
      val id = PowerStation.add(state.get)
      if(id.isDefined) {
        Created(Json.toJson(PowerStation.find(id.get)))
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


  def createPowerStationByValues(name:String) = Action(parse.empty) { implicit request =>
      val id = PowerStation.add(name)
      if(id.isDefined) {
        Created(Json.toJson(PowerStation.find(id.get)))
      }
      else {
        BadRequest(Json.toJson(name))
      }
    }

  def updatePowerStation(id: Long) = Action(parse.json) { implicit request =>
    val state = request.body.validate[PowerStation]
    if(state.isSuccess)
    {
      PowerStation.update(state.get)
      Accepted(Json.toJson(state.get))
    }
    else {
      prettyPrintError(state)
      BadRequest(request.body)
    }
  }

  def deletePowerStation(id: Long) = Action(parse.empty) { implicit request =>
    val state = PowerStation.find(id)
    if(state.isDefined)
    {
      PowerStation.delete(id)
      Ok(Json.toJson(id))
    }
    else {
      BadRequest(Json.toJson(id))
    }
  }

}
