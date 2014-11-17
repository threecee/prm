
package models.classification

import java.util.concurrent.Executors

import anorm.SqlParser._
import anorm._
import play.api.Play.current
import play.api.db.DB
import utils.dbMapper

import scala.concurrent.ExecutionContext

case class Component(
                             id: Option[Long],
                             equipmentState:EquipmentState,
                             componentType:ComponentType
                             )

object Component {

  private val table: String = "components"

  import play.api.libs.functional.syntax._
  import play.api.libs.json._

  implicit val ComponentFromJson: Reads[Component] = (
    (__ \ "id").readNullable[Long] ~
      (__ \ "equipmentState").read[EquipmentState] ~
        (__ \ "componentType").read[ComponentType]
    )(Component.apply _)

  implicit val ComponentToJson: Writes[Component] = (
    (__ \ "id").writeNullable[Long] ~
      (__ \ "equipmentState").write[EquipmentState]  ~
      (__ \ "componentType").write[ComponentType]
    )((component: Component) => (
    component.id,
    component.equipmentState,
    component.componentType
    ))


  val executorService = Executors.newFixedThreadPool(4)
  val executionContext = ExecutionContext.fromExecutorService(executorService)

  val simple = {
    get[Option[Long]]("id") ~
      get[Long]("equipmentstate") ~
      get[Long]("componenttype") map { case id ~ equipmentstate ~ componenttype => Component(id, EquipmentState.find(equipmentstate).get, ComponentType.find(componenttype).get)
    }
  }

  def findAll(): Seq[Component] = {
    DB.withConnection { implicit connection =>
      dbMapper.makeSelectStatement(table).as(Component.simple *)
    }
  }

  def find(id: Long): Option[Component] = {
    DB.withConnection { implicit connection =>
      dbMapper.makeSelectStatement(table, "id" -> id).as(Component.simple.singleOpt)
    }
  }

  def findWithPowerUnit(id: Long): Seq[Component] = {
    DB.withConnection { implicit connection =>
      dbMapper.makeSelectStatement(table, "powerunit" -> id).as(Component.simple *)
    }
  }
  def findWithPowerStation(id: Long): Seq[Component] = {
    DB.withConnection { implicit connection =>
      dbMapper.makeSelectStatement(table, "powerstation" -> id).as(Component.simple *)
    }
  }


  def addForPowerUnit(powerunitId:Long, eq:Component): Option[Long] = {
    DB.withTransaction {
      implicit connection =>
        dbMapper.makeInsertStatement(table, dbMapper.mapValues("componenttype" -> eq.componentType.id.get, "equipmentstate" -> eq.equipmentState.id, "powerunit" -> powerunitId))
          .executeInsert() match {
          case Some(long) => Some(long.asInstanceOf[Long])
          case None       => None
        }
    }
  }

  def addForPowerUnit(powerunitId:Long, equipmentStateId:Long, componentTypeId:Long): Option[Long] = {
    addForPowerUnit(powerunitId, Component(None, EquipmentState.find(equipmentStateId).get, ComponentType.find(componentTypeId).get))
  }


  def addForPowerStation(powerStationId:Long, eq:Component): Option[Long] = {
    DB.withTransaction {
      implicit connection =>
        dbMapper.makeInsertStatement(table, dbMapper.mapValues("componenttype" -> eq.componentType.id.get, "equipmentstate" -> eq.equipmentState.id, "powerstation" -> powerStationId))
          .executeInsert() match {
          case Some(long) => Some(long.asInstanceOf[Long])
          case None       => None
        }
    }
  }

  def addForPowerStation(powerStationId:Long, equipmentStateId:Long, componentTypeId:Long): Option[Long] = {
    addForPowerStation(powerStationId, Component(None, EquipmentState.find(equipmentStateId).get, ComponentType.find(componentTypeId).get))
  }


  def updateForPowerUnit(powerunitId:Long, eq:Component): Unit = {
    DB.withTransaction {
      implicit connection =>
        if(find(eq.id.getOrElse(-1)).isDefined){
          dbMapper.makeUpdateStatement(table, "id" -> eq.id, dbMapper.mapValues("componenttype" -> eq.componentType.id.get, "equipmentstate" -> eq.equipmentState.id, "powerunit" -> powerunitId))
            .executeUpdate()
        }
        else{
          val id = addForPowerUnit(powerunitId, eq)
        }
    }
  }

  def updateForPowerStation(powerstationId:Long, eq:Component): Unit = {
    DB.withTransaction {
      implicit connection =>
        if(find(eq.id.getOrElse(-1)).isDefined){
          dbMapper.makeUpdateStatement(table, "id" -> eq.id, dbMapper.mapValues("componenttype" -> eq.componentType.id.get, "equipmentstate" -> eq.equipmentState.id, "powerstation" -> powerstationId))
            .executeUpdate()
        }
        else{
          val id = addForPowerStation(powerstationId, eq)
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

