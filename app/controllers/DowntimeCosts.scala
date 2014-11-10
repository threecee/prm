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

  def createDowntimeCost(powerUnitId:Long) = Action(parse.json) { implicit request =>
    val state = request.body.validate[DowntimeCost]
    if(state.isSuccess)
    {
     val id = DowntimeCost.add(powerUnitId, state.get)
      if(id.isDefined) {
        Created(Json.toJson(DowntimeCost.find(id.get)))
      }
      else {
        BadRequest(request.body)
      }
    }
    else {
      BadRequest(request.body)
    }
  }

  def updateDowntimeCost(powerUnitId: Long) = Action(parse.json) { implicit request =>
    val state = request.body.validate[DowntimeCost]
    if(state.isSuccess)
    {
      DowntimeCost.update(powerUnitId, state.get)
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
