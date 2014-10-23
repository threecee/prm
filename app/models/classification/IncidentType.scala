package models.classification

import java.util.concurrent.Executors

import anorm.SqlParser._
import anorm._
import play.api.Play.current
import play.api.db.DB
import utils.dbMapper

import scala.concurrent.ExecutionContext

case class IncidentType(
                          id: Option[Long],
                          name: String,
                          description:String
                          )

object IncidentType {
  private val table: String = "incidenttypes"

  import play.api.libs.functional.syntax._
  import play.api.libs.json._

  implicit val IncidentTypeFromJson: Reads[IncidentType] = (
    (__ \ "id").readNullable[Long] ~
      (__ \ "name").read[String] ~
      (__ \ "description").read[String]
    )(IncidentType.apply _)

  implicit val IncidentTypeToJson: Writes[IncidentType] = (
    (__ \ "id").writeNullable[Long] ~
      (__ \ "name").write[String] ~
        (__ \ "description").write[String]
    )((incidentType: IncidentType) => (
    incidentType.id,
    incidentType.name,
    incidentType.description
    ))


  val executorService = Executors.newFixedThreadPool(4)
  val executionContext = ExecutionContext.fromExecutorService(executorService)

  val simple = {
    get[Option[Long]]("id") ~
      get[String]("name") ~
      get[String]("description") map { case id ~ name ~ description => IncidentType(id, name, description)
    }
  }

  def findAll(): Seq[IncidentType] = {
    DB.withConnection { implicit connection =>
      dbMapper.makeSelectStatement(table).as(IncidentType.simple *)
    }
  }

  def find(id: Long): Option[IncidentType] = {
    DB.withConnection { implicit connection =>
      dbMapper.makeSelectStatement(table, "id" -> id).as(IncidentType.simple.singleOpt)
    }
  }

  def add(eq:IncidentType): Option[Long] = {
    DB.withTransaction {
      implicit connection =>
        dbMapper.makeInsertStatement(table, dbMapper.mapValues("name" -> eq.name, "description" -> eq.description))
          .executeInsert() match {
          case Some(long) => Some(long.asInstanceOf[Long])
          case None       => None
        }
    }
  }

  def update(eq:IncidentType): Unit = {
    DB.withTransaction {
      implicit connection =>
        if(find(eq.id.getOrElse(-1)).isDefined){
          dbMapper.makeUpdateStatement(table, "id" -> eq.id, dbMapper.mapValues("name" -> eq.name, "description" -> eq.description))
            .executeUpdate()
        }
        else{
          val id = add(eq)
        }
    }
  }

  def delete(id:Long): Unit = {
    DB.withTransaction {
      implicit connection =>
        dbMapper.makeDeleteStatement(table, "id" -> id)
          .executeUpdate()
    }
  }

}
