package models.classification

import java.util.concurrent.Executors

import anorm.SqlParser._
import anorm._
import play.api.Play.current
import play.api.db.DB
import utils.dbMapper

import scala.concurrent.ExecutionContext

case class ComponentType(
                           id: Option[Long],
                           name: String
                           )

object ComponentType {
  private val table: String = "componenttypes"

  import play.api.libs.functional.syntax._
  import play.api.libs.json._

  implicit val ComponentTypeFromJson: Reads[ComponentType] = (
    (__ \ "id").readNullable[Long] ~
      (__ \ "name").read[String]
    )(ComponentType.apply _)

  implicit val ComponentTypeToJson: Writes[ComponentType] = (
    (__ \ "id").writeNullable[Long] ~
      (__ \ "name").write[String]
    )((componentType: ComponentType) => (
    componentType.id,
    componentType.name
    ))


  val executorService = Executors.newFixedThreadPool(4)
  val executionContext = ExecutionContext.fromExecutorService(executorService)

  val simple = {
    get[Option[Long]]("id") ~
      get[String]("name") map { case id ~ name => ComponentType(id, name)
    }
  }

  def findAll(): Seq[ComponentType] = {
    DB.withConnection { implicit connection =>
      dbMapper.makeSelectStatement(table).as(ComponentType.simple *)
    }
  }

  def find(id: Long): Option[ComponentType] = {
    DB.withConnection { implicit connection =>
      dbMapper.makeSelectStatement(table, "id" -> id).as(ComponentType.simple.singleOpt)
    }
  }

  def add(eq:ComponentType): Option[Long] = {
    DB.withTransaction {
      implicit connection =>
        dbMapper.makeInsertStatement(table, dbMapper.mapValues("name" -> eq.name))
          .executeInsert() match {
          case Some(long) => Some(long.asInstanceOf[Long])
          case None       => None
        }
    }
  }

  def update(eq:ComponentType): Unit = {
    DB.withTransaction {
      implicit connection =>
        if(find(eq.id.getOrElse(-1)).isDefined){
          dbMapper.makeUpdateStatement(table, "id" -> eq.id, dbMapper.mapValues("name" -> eq.name))
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
