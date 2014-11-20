
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
                             repairs:Seq[Repair],
                             componentType:ComponentType
                             )

object Component {

  private val table: String = "components"

  import play.api.libs.functional.syntax._
  import play.api.libs.json._

  implicit val ComponentFromJson: Reads[Component] = (
    (__ \ "id").readNullable[Long] ~
      (__ \ "equipmentState").read[EquipmentState] ~
      (__ \ "repairs").read[Seq[Repair]] ~
      (__ \ "componentType").read[ComponentType]
    )(Component.apply _)

  implicit val ComponentToJson: Writes[Component] = (
    (__ \ "id").writeNullable[Long] ~
      (__ \ "equipmentState").write[EquipmentState]  ~
      (__ \ "repairs").write[Seq[Repair]]  ~
      (__ \ "componentType").write[ComponentType]
    )((component: Component) => (
    component.id,
    component.equipmentState,
    component.repairs,
    component.componentType
    ))


  val executorService = Executors.newFixedThreadPool(4)
  val executionContext = ExecutionContext.fromExecutorService(executorService)

  val simple = {
    get[Option[Long]]("id") ~
      get[Long]("equipmentstate") ~
      get[Long]("componenttype") map { case id ~ equipmentstate ~ componenttype => Component(id, EquipmentState.find(equipmentstate).get, Repair.findWithComponent(id.get), ComponentType.find(componenttype).get)
    }
  }

  def simpleFast(equipmentStates:Seq[EquipmentState], componentTypes:Seq[ComponentType]) = {
    get[Option[Long]]("id") ~
      get[Long]("equipmentstate") ~
      get[Long]("componenttype") map { case id ~ equipmentstate ~ componenttype => Component(id, equipmentStates.find(_.id.get == equipmentstate).get, Repair.findWithComponent(id.get), componentTypes.find(_.id.get == componenttype).get)
    }
  }


  def simpleWithPowerStation(equipmentStates:Seq[EquipmentState], componentTypes:Seq[ComponentType]) = {
    get[Option[Long]]("id") ~
      get[Long]("equipmentstate") ~
      get[Long]("componenttype")~
      get[Option[Long]]("powerstation") map { case id ~ equipmentstate ~ componenttype ~ powerstation =>  (powerstation, Component(id, equipmentStates.find(_.id.get == equipmentstate).get, Repair.findWithComponent(id.get), componentTypes.find(_.id.get == componenttype).get))
    }
  }

  def simpleWithPowerUnit(equipmentStates:Seq[EquipmentState], componentTypes:Seq[ComponentType]) = {
    get[Option[Long]]("id") ~
      get[Long]("equipmentstate") ~
      get[Long]("componenttype") ~
      get[Option[Long]]("powerunit") map { case id ~ equipmentstate ~ componenttype ~ powerunit =>  (powerunit, Component(id, equipmentStates.find(_.id.get == equipmentstate).get, Repair.findWithComponent(id.get), componentTypes.find(_.id.get == componenttype).get))
    }
  }


  def findAll(): Seq[Component] = {
    val equipmentStates:Seq[EquipmentState] = EquipmentState.findAll()
    val componentTypes:Seq[ComponentType] = ComponentType.findAll()
    DB.withConnection { implicit connection =>
      dbMapper.makeSelectStatement(table).as(Component.simpleFast(equipmentStates, componentTypes) *)
    }
  }

  def find(id: Long): Option[Component] = {
    DB.withConnection { implicit connection =>
      dbMapper.makeSelectStatement(table, "id" -> id).as(Component.simple.singleOpt)
    }
  }

  def findByName(ref: String): Option[Component] = {
    DB.withConnection { implicit connection =>
      dbMapper.makeSelectStatement(table, "name" -> ref).as(Component.simple.singleOpt)
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
  def findAllWithPowerStation(): Seq[(Long, Seq[Component])] = {
    val equipmentStates:Seq[EquipmentState] = EquipmentState.findAll()
    val componentTypes:Seq[ComponentType] = ComponentType.findAll()
    DB.withConnection { implicit connection =>
      val list:Seq[(Option[Long], Component)] =  dbMapper.makeSelectStatement(table).as(Component.simpleWithPowerStation(equipmentStates, componentTypes) *)

      val ids:Seq[Long] = list.filter(_._1.isDefined).map(item => item._1.get).distinct
      ids.map(id => (id, list.filter(_._1.get == id).map(item => item._2)))

    }
  }
  def findAllWithPowerUnit(): Seq[(Long, Seq[Component])] = {
    val equipmentStates:Seq[EquipmentState] = EquipmentState.findAll()
    val componentTypes:Seq[ComponentType] = ComponentType.findAll()
    DB.withConnection { implicit connection =>
      val list:Seq[(Option[Long], Component)] =  dbMapper.makeSelectStatement(table).as(Component.simpleWithPowerUnit(equipmentStates, componentTypes) *)

      val ids:Seq[Long] = list.filter(_._1.isDefined).map(item => item._1.get).distinct
      ids.map(id => (id, list.filter(_._1.get == id).map(item => item._2)))

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
    addForPowerUnit(powerunitId, Component(None, EquipmentState.find(equipmentStateId).get, Seq.empty, ComponentType.find(componentTypeId).get))
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
    addForPowerStation(powerStationId, Component(None, EquipmentState.find(equipmentStateId).get, Seq.empty, ComponentType.find(componentTypeId).get))
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

