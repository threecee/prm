package controllers

import controllers.base.BaseController
import models.classification.Group
import play.api.libs.json._
import play.api.mvc._

object Groups extends BaseController  {


  def groups() = Action(parse.empty){ implicit request =>
    Ok(Json.toJson(Group.findAll))
  }
  def group(id: Long) = Action(parse.empty){ implicit request =>
     Ok(Json.toJson(Group.find(id)))
  }

  def createGroup() = Action(parse.json) { implicit request =>
    val state = request.body.validate[Group]
    if(state.isSuccess)
    {
     val id = Group.add(state.get)
      if(id.isDefined) {
        Created(Json.toJson(Group.find(id.get)))
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

  def updateGroup(id: Long) = Action(parse.json) { implicit request =>
    val state = request.body.validate[Group]
    if(state.isSuccess)
    {
      Group.update(state.get)
      Accepted(Json.toJson(state.get))
    }
    else {
      prettyPrintError(state)
      BadRequest(request.body)
    }
  }

  def deleteGroup(id: Long) = Action(parse.empty) { implicit request =>
    val state = Group.find(id)
    if(state.isDefined)
    {
      Group.delete(id)
      Ok(Json.toJson(id))
    }
    else {
      BadRequest(Json.toJson(id))
    }
  }

}
