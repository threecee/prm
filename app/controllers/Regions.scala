package controllers

import controllers.base.BaseController
import models.classification.Region
import play.api.libs.json._
import play.api.mvc._

object Regions extends BaseController  {


  def regions() = Action(parse.empty){ implicit request =>
    Ok(Json.toJson(Region.findAll))
  }
  def region(id: Long) = Action(parse.empty){ implicit request =>
     Ok(Json.toJson(Region.find(id)))
  }

  def createRegion() = Action(parse.json) { implicit request =>
    val state = request.body.validate[Region]
    if(state.isSuccess)
    {
     val id = Region.add(state.get)
      if(id.isDefined) {
        Created(Json.toJson(Region.find(id.get)))
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

  def updateRegion(id: Long) = Action(parse.json) { implicit request =>
    val state = request.body.validate[Region]
    if(state.isSuccess)
    {
      Region.update(state.get)
      Accepted(Json.toJson(state.get))
    }
    else {
      prettyPrintError(state)
      BadRequest(request.body)
    }
  }

  def deleteRegion(id: Long) = Action(parse.empty) { implicit request =>
    val state = Region.find(id)
    if(state.isDefined)
    {
      Region.delete(id)
      Ok(Json.toJson(id))
    }
    else {
      BadRequest(Json.toJson(id))
    }
  }

}
