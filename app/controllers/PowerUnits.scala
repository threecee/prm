package controllers

import models.classification.PowerUnit
import play.api.libs.json._
import play.api.mvc._

object PowerUnits extends Controller  {


  def powerUnits() = Action(parse.empty){ implicit request =>
    Ok(Json.toJson(PowerUnit.findAll))
  }
  def powerUnit(id: Long) = Action(parse.empty){ implicit request =>
     Ok(Json.toJson(PowerUnit.find(id)))
  }

  def createPowerUnit(powerStationId:Long) = Action(parse.json) { implicit request =>
    val state = request.body.validate[PowerUnit]
    if(state.isSuccess)
    {
     val id = PowerUnit.add(powerStationId, state.get)
      if(id.isDefined) {
        Created(Json.toJson(PowerUnit.find(id.get)))
      }
      else {
        BadRequest(request.body)
      }
    }
    else {
      BadRequest(request.body)
    }
  }

  def updatePowerUnit(powerUnitId: Long) = Action(parse.json) { implicit request =>
    val state = request.body.validate[PowerUnit]
    if(state.isSuccess)
    {
      PowerUnit.update(powerUnitId, state.get)
      Accepted(Json.toJson(state.get))
    }
    else {
      BadRequest(request.body)
    }
  }

  def deletePowerUnit(id: Long) = Action(parse.empty) { implicit request =>
    val state = PowerUnit.find(id)
    if(state.isDefined)
    {
      PowerUnit.delete(id)
      Ok(Json.toJson(id))
    }
    else {
      BadRequest(Json.toJson(id))
    }
  }

}
