package controllers

import models.classification.EquipmentState
import play.api.libs.json._
import play.api.mvc._

object EquipmentStates extends Controller  {


  def equipmentStates() = Action(parse.empty){ implicit request =>
    Ok(Json.toJson(EquipmentState.findAll))
  }
  def equipmentState(id: Long) = Action(parse.empty){ implicit request =>
     Ok(Json.toJson(EquipmentState.find(id)))
  }

  def createEquipmentState() = Action(parse.json) { implicit request =>
    val state = request.body.validate[EquipmentState]
    if(state.isSuccess)
    {
     val id = EquipmentState.add(state.get)
      if(id.isDefined) {
        Created(Json.toJson(EquipmentState.find(id.get)))
      }
      else {
        BadRequest(request.body)
      }
    }
    else {
      BadRequest(request.body)
    }
  }

  def updateEquipmentState(id: Long) = Action(parse.json) { implicit request =>
    val state = request.body.validate[EquipmentState]
    if(state.isSuccess)
    {
      EquipmentState.update(state.get)
      Accepted(Json.toJson(state.get))
    }
    else {
      BadRequest(request.body)
    }
  }

  def deleteEquipmentState(id: Long) = Action(parse.empty) { implicit request =>
    val state = EquipmentState.find(id)
    if(state.isDefined)
    {
      EquipmentState.delete(id)
      Ok(Json.toJson(id))
    }
    else {
      BadRequest(Json.toJson(id))
    }
  }

}
