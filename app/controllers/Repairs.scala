package controllers

import models.classification.Repair
import play.api.libs.json._
import play.api.mvc._

object Repairs extends Controller  {


  def repairs() = Action(parse.empty){ implicit request =>
    Ok(Json.toJson(Repair.findAll))
  }
  def repair(id: Long) = Action(parse.empty){ implicit request =>
    Ok(Json.toJson(Repair.find(id)))
  }

  def createRepair(componentId:Long) = Action(parse.json) { implicit request =>
    val state = request.body.validate[Repair]
    if(state.isSuccess)
    {
      val id = Repair.add(componentId, state.get)
      if(id.isDefined) {
        Created(Json.toJson(Repair.find(id.get)))
      }
      else {
        BadRequest(request.body)
      }
    }
    else {
      BadRequest(request.body)
    }
  }

  def createRepairByValues(componentId:Long, incidentId:Long, span:Double, cost:Double, probability:Double) = Action(parse.empty) { implicit request =>
      val id = Repair.add(componentId, incidentId, span, cost, probability)
      if(id.isDefined) {
        Created(Json.toJson(Repair.find(id.get)))
      }
      else {
        BadRequest(Json.toJson(id))
      }

  }


  def updateRepair(componentId: Long) = Action(parse.json) { implicit request =>
    val state = request.body.validate[Repair]
    if(state.isSuccess)
    {
      Repair.update(componentId, state.get)
      Accepted(Json.toJson(state.get))
    }
    else {
      BadRequest(request.body)
    }
  }

  def deleteRepair(id: Long) = Action(parse.empty) { implicit request =>
    val state = Repair.find(id)
    if(state.isDefined)
    {
      Repair.delete(id)
      Ok(Json.toJson(id))
    }
    else {
      BadRequest(Json.toJson(id))
    }
  }

}
