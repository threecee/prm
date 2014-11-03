package controllers

import models.classification.IncidentType
import play.api.libs.json._
import play.api.mvc._

object IncidentTypes extends Controller  {


  def incidentTypes() = Action(parse.empty){ implicit request =>
    Ok(Json.toJson(IncidentType.findAll))
  }
  def incidentType(id: Long) = Action(parse.empty){ implicit request =>
    Ok(Json.toJson(IncidentType.find(id)))
  }

  def createIncidentType() = Action(parse.json) { implicit request =>
    val state = request.body.validate[IncidentType]
    if(state.isSuccess)
    {
      val id = IncidentType.add(state.get)
      if(id.isDefined) {
        Created(Json.toJson(IncidentType.find(id.get)))
      }
      else {
        BadRequest(request.body)
      }
    }
    else {
      BadRequest(request.body)
    }
  }

  def updateIncidentType(id: Long) = Action(parse.json) { implicit request =>
    val state = request.body.validate[IncidentType]
    if(state.isSuccess)
    {
      IncidentType.update(state.get)
      Accepted(Json.toJson(state.get))
    }
    else {
      BadRequest(request.body)
    }
  }

  def deleteIncidentType(id: Long) = Action(parse.empty) { implicit request =>
    val state = IncidentType.find(id)
    if(state.isDefined)
    {
      IncidentType.delete(id)
      Ok(Json.toJson(id))
    }
    else {
      BadRequest(Json.toJson(id))
    }
  }

}
