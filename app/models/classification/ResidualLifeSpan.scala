
package models.classification

import java.util.concurrent.Executors

import anorm.SqlParser._
import anorm._
import play.api.Play.current
import play.api.db.DB
import utils.dbMapper

import scala.concurrent.ExecutionContext

case class ResidualLifeSpan(
                         id: Option[Long],
                         span: Double,
                         equipmentState:EquipmentState
                         )

object ResidualLifeSpan {
  private val table: String = "residuallifespans"

  import play.api.libs.functional.syntax._
  import play.api.libs.json._

  implicit val ResidualLifeSpanFromJson: Reads[ResidualLifeSpan] = (
    (__ \ "id").readNullable[Long] ~
      (__ \ "span").read[Double] ~
      (__ \ "equipmentState").read[EquipmentState]
    )(ResidualLifeSpan.apply _)

  implicit val ResidualLifeSpanToJson: Writes[ResidualLifeSpan] = (
    (__ \ "id").writeNullable[Long] ~
      (__ \ "span").write[Double] ~
        (__ \ "equipmentState").write[EquipmentState]
    )((residualLifeSpan: ResidualLifeSpan) => (
    residualLifeSpan.id,
    residualLifeSpan.span,
    residualLifeSpan.equipmentState
    ))


  val executorService = Executors.newFixedThreadPool(4)
  val executionContext = ExecutionContext.fromExecutorService(executorService)

  val simple = {
    get[Option[Long]]("id") ~
      get[Double]("span") ~
      get[Long]("equipmentstate") map { case id ~ span ~ equipmentstate => ResidualLifeSpan(id, span, EquipmentState.find(equipmentstate).get)
    }
  }

  def findAll(): Seq[ResidualLifeSpan] = {
    DB.withConnection { implicit connection =>
      dbMapper.makeSelectStatement(table).as(ResidualLifeSpan.simple *)
    }
  }

  def find(id: Long): Option[ResidualLifeSpan] = {
    DB.withConnection { implicit connection =>
      dbMapper.makeSelectStatement(table, "id" -> id).as(ResidualLifeSpan.simple.singleOpt)
    }
  }

  def findWithComponentType(id: Long): Seq[ResidualLifeSpan] = {
    DB.withConnection { implicit connection =>
      dbMapper.makeSelectStatement(table, "componenttype" -> id).as(ResidualLifeSpan.simple *)
    }
  }


  def add(componentId:Long, eq:ResidualLifeSpan): Option[Long] = {
    DB.withTransaction {
      implicit connection =>
        dbMapper.makeInsertStatement(table, dbMapper.mapValues("span" -> eq.span, "componenttype" -> componentId, "equipmentstate" -> eq.equipmentState.id))
          .executeInsert() match {
          case Some(long) => Some(long.asInstanceOf[Long])
          case None       => None
        }
    }
  }

  def add(componentId:Long, eq:Long, span:Double): Option[Long] = {
    DB.withTransaction {
      implicit connection =>
        dbMapper.makeInsertStatement(table, dbMapper.mapValues("span" -> span, "componenttype" -> componentId, "equipmentstate" -> eq))
          .executeInsert() match {
          case Some(long) => Some(long.asInstanceOf[Long])
          case None       => None
        }
    }
  }


  def update(eq:ComponentType): Unit = {
    eq.residuallifespans.map(span => update(eq.id.get, span))
  }

  def update(componentId:Long, eq:ResidualLifeSpan): Unit = {
    DB.withTransaction {
      implicit connection =>
        if(find(eq.id.getOrElse(-1)).isDefined){
          dbMapper.makeUpdateStatement(table, "id" -> eq.id, dbMapper.mapValues("span" -> eq.span, "componenttype" -> componentId, "equipmentstate" -> eq.equipmentState.id))
            .executeUpdate()
        }
        else{
          val id = add(componentId, eq)
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

