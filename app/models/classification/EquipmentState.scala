package models.classification

import java.util.concurrent.Executors

import anorm.SqlParser._
import anorm._
import play.api.Play.current
import play.api.db.DB
import utils.dbMapper

import scala.concurrent.ExecutionContext

case class EquipmentState(
                 id: Option[Long],
                 value: Int
                 )

object EquipmentState {
  private val table: String = "equipmentstates"

  import play.api.libs.functional.syntax._
  import play.api.libs.json._

  implicit val EquipmentStateFromJson: Reads[EquipmentState] = (
    (__ \ "id").readNullable[Long] ~
      (__ \ "value").read[Int]
    )(EquipmentState.apply _)

  implicit val EquipmentStateToJson: Writes[EquipmentState] = (
    (__ \ "id").writeNullable[Long] ~
      (__ \ "value").write[Int]
    )((equipmentState: EquipmentState) => (
    equipmentState.id,
    equipmentState.value
    ))


  val executorService = Executors.newFixedThreadPool(4)
  val executionContext = ExecutionContext.fromExecutorService(executorService)

  val simple = {
    get[Option[Long]]("id") ~
      get[Int]("value") map { case id ~ value => EquipmentState(id, value)
    }
  }

  def findAll(): Seq[EquipmentState] = {
    DB.withConnection { implicit connection =>
      dbMapper.makeSelectStatement(table).as(EquipmentState.simple *)
    }
  }

  def find(id: Long): Option[EquipmentState] = {
    DB.withConnection { implicit connection =>
      dbMapper.makeSelectStatement(table, "id" -> id).as(EquipmentState.simple.singleOpt)
    }
  }
  def findByReference(ref: Int): Option[EquipmentState] = {
    DB.withConnection { implicit connection =>
      dbMapper.makeSelectStatement(table, "value" -> ref).as(EquipmentState.simple.singleOpt)
    }
  }

  def add(eq:EquipmentState): Option[Long] = {
    DB.withTransaction {
      implicit connection =>
       dbMapper.makeInsertStatement(table, dbMapper.mapValues("value" -> eq.value))
        .executeInsert() match {
          case Some(long) => Some(long.asInstanceOf[Long])
          case None       => None
        }
    }
  }

  def update(eq:EquipmentState): Unit = {
    DB.withTransaction {
      implicit connection =>
        if(find(eq.id.getOrElse(-1)).isDefined){
          dbMapper.makeUpdateStatement(table, "id" -> eq.id, dbMapper.mapValues("value" -> eq.value))
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
