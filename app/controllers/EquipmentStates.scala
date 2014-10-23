package controllers

import play.api.libs.json._
import play.api.mvc._

import models.{EquipmentState, User}

object EquipmentStates extends Controller  {


  def equipmentState(id: Long) = Action(parse.json){ implicit request =>
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
    }
    BadRequest(request.body)
  }

  def updateEquipmentState(id: Long) = Action(parse.json) { implicit request =>
    val state = request.body.validate[EquipmentState]
    if(state.isSuccess)
    {
      EquipmentState.update(state.get)
      Accepted(Json.toJson(state.get))
    }
    BadRequest(request.body)
  }

  def deleteEquipmentState(id: Long) = Action(parse.empty) { implicit request =>
    val state = EquipmentState.find(id)
    if(state.isDefined)
    {
      EquipmentState.delete(id)
      Gone(Json.toJson(id))
    }
    BadRequest(Json.toJson(id))
  }

}
