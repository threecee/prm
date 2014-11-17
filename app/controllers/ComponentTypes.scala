package controllers

import controllers.base.BaseController
import models.classification.ComponentType
import play.api.libs.json._
import play.api.mvc._

object ComponentTypes extends BaseController  {



  def componentTypesForPowerUnits() = Action(parse.empty){ implicit request =>
    Ok(Json.toJson(ComponentType.findAll().filterNot(_.partOfPowerStation)))
  }
  def componentTypesForPowerStations() = Action(parse.empty){ implicit request =>
    Ok(Json.toJson(ComponentType.findAll().filter(_.partOfPowerStation)))
  }


  def componentTypes() = Action(parse.empty){ implicit request =>
    Ok(Json.toJson(ComponentType.findAll))
  }
  def componentType(id: Long) = Action(parse.empty){ implicit request =>
     Ok(Json.toJson(ComponentType.find(id)))
  }

  def createComponentType() = Action(parse.json) { implicit request =>
    val state = request.body.validate[ComponentType]
    if(state.isSuccess)
    {
     val id = ComponentType.add(state.get)
      if(id.isDefined) {
        Created(Json.toJson(ComponentType.find(id.get)))
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

  def updateComponentType(id: Long) = Action(parse.json) { implicit request =>
    val state = request.body.validate[ComponentType]
    if(state.isSuccess)
    {
      ComponentType.update(state.get)
      Accepted(Json.toJson(state.get))
    }
    else {
      prettyPrintError(state)
      BadRequest(request.body)
    }
  }

  def deleteComponentType(id: Long) = Action(parse.empty) { implicit request =>
    val state = ComponentType.find(id)
    if(state.isDefined)
    {
      ComponentType.delete(id)
      Ok(Json.toJson(id))
    }
    else {
      BadRequest(Json.toJson(id))
    }
  }

}
