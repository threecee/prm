package controllers

import models.classification.ResidualLifeSpan
import play.api.libs.json._
import play.api.mvc._

object ResidualLifeSpans extends Controller  {


  def residualLifeSpans() = Action(parse.empty){ implicit request =>
    Ok(Json.toJson(ResidualLifeSpan.findAll))
  }
  def residualLifeSpan(id: Long) = Action(parse.empty){ implicit request =>
    Ok(Json.toJson(ResidualLifeSpan.find(id)))
  }

  def createResidualLifeSpan(componentId:Long) = Action(parse.json) { implicit request =>
    val state = request.body.validate[ResidualLifeSpan]
    if(state.isSuccess)
    {
      val id = ResidualLifeSpan.add(componentId, state.get)
      if(id.isDefined) {
        Created(Json.toJson(ResidualLifeSpan.find(id.get)))
      }
      else {
        BadRequest(request.body)
      }
    }
    else {
      BadRequest(request.body)
    }
  }

  def createResidualLifeSpanByValues(componentId:Long, equipmentStateId:Long, span:Double) = Action(parse.empty) { implicit request =>
      val id = ResidualLifeSpan.add(componentId, equipmentStateId, span)
      if(id.isDefined) {
        Created(Json.toJson(ResidualLifeSpan.find(id.get)))
      }
      else {
        BadRequest(Json.toJson(id))
      }

  }


  def updateResidualLifeSpan(componentId: Long) = Action(parse.json) { implicit request =>
    val state = request.body.validate[ResidualLifeSpan]
    if(state.isSuccess)
    {
      ResidualLifeSpan.update(componentId, state.get)
      Accepted(Json.toJson(state.get))
    }
    else {
      BadRequest(request.body)
    }
  }

  def deleteResidualLifeSpan(id: Long) = Action(parse.empty) { implicit request =>
    val state = ResidualLifeSpan.find(id)
    if(state.isDefined)
    {
      ResidualLifeSpan.delete(id)
      Ok(Json.toJson(id))
    }
    else {
      BadRequest(Json.toJson(id))
    }
  }

}
