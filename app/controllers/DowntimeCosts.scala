package controllers

import controllers.base.BaseController
import models.classification.DowntimeCost
import play.api.libs.json._
import play.api.mvc._

object DowntimeCosts extends BaseController  {


  def downtimecosts() = Action(parse.empty){ implicit request =>
    Ok(Json.toJson(DowntimeCost.findAll))
  }
  def downtimecost(id: Long) = Action(parse.empty){ implicit request =>
     Ok(Json.toJson(DowntimeCost.find(id)))
  }

  def createDowntimeCostForPowerUnit(powerUnitId:Long) = Action(parse.json) { implicit request =>
    val state = request.body.validate[DowntimeCost]
    if(state.isSuccess)
    {
     val id = DowntimeCost.addForPowerUnit(powerUnitId, state.get)
      if(id.isDefined) {
        Created(Json.toJson(DowntimeCost.find(id.get)))
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

  def createDowntimeCostForPowerStation(powerStationId:Long) = Action(parse.json) { implicit request =>
    val state = request.body.validate[DowntimeCost]
    if(state.isSuccess)
    {
      val id = DowntimeCost.addForPowerStation(powerStationId, state.get)
      if(id.isDefined) {
        Created(Json.toJson(DowntimeCost.find(id.get)))
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

  def updateDowntimeCostForPowerUnit(powerUnitId: Long) = Action(parse.json) { implicit request =>
    val state = request.body.validate[DowntimeCost]
    if(state.isSuccess)
    {
      DowntimeCost.updateForPowerUnit(powerUnitId, state.get)
      Accepted(Json.toJson(state.get))
    }
    else {
      prettyPrintError(state)
      BadRequest(request.body)
    }
  }
  def updateDowntimeCostForPowerStation(powerStationId: Long) = Action(parse.json) { implicit request =>
    val state = request.body.validate[DowntimeCost]
    if(state.isSuccess)
    {
      DowntimeCost.updateForPowerStation(powerStationId, state.get)
      Accepted(Json.toJson(state.get))
    }
    else {
      prettyPrintError(state)
      BadRequest(request.body)
    }
  }

  def deleteDowntimeCost(id: Long) = Action(parse.empty) { implicit request =>
    val state = DowntimeCost.find(id)
    if(state.isDefined)
    {
      DowntimeCost.delete(id)
      Ok(Json.toJson(id))
    }
    else {
      BadRequest(Json.toJson(id))
    }
  }

}
