package controllers.classification

import models.classification.ComponentType
import play.api.libs.json._
import play.api.mvc._

object ComponentTypes extends Controller  {


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
