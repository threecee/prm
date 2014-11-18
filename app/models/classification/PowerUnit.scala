
package models.classification

import java.util.concurrent.Executors

import anorm.SqlParser._
import anorm._
import play.api.Play.current
import play.api.db.DB
import utils.dbMapper

import scala.concurrent.ExecutionContext

case class PowerUnit(
                             id: Option[Long],
                             referenceId:Option[String],
                             downtimeCosts:Seq[DowntimeCost],
                             components:Seq[Component]
                             )

object PowerUnit {

  private val table: String = "powerunits"

  import play.api.libs.functional.syntax._
  import play.api.libs.json._

  implicit val PowerUnitFromJson: Reads[PowerUnit] = (
    (__ \ "id").readNullable[Long] ~
      (__ \ "referenceId").readNullable[String] ~
      (__ \ "downtimeCosts").read[Seq[DowntimeCost]]  ~
        (__ \ "components").read[Seq[Component]]
    )(PowerUnit.apply _)

  implicit val PowerUnitToJson: Writes[PowerUnit] = (
    (__ \ "id").writeNullable[Long] ~
      (__ \ "referenceId").writeNullable[String]~
      (__ \ "downtimeCosts").write[Seq[DowntimeCost]]  ~
      (__ \ "components").write[Seq[Component]]

    )((powerUnit: PowerUnit) => (
    powerUnit.id,
    powerUnit.referenceId,
    powerUnit.downtimeCosts,
    powerUnit.components
    ))


  val executorService = Executors.newFixedThreadPool(4)
  val executionContext = ExecutionContext.fromExecutorService(executorService)

  val simple = {
    get[Option[Long]]("id") ~
      get[Option[String]]("referenceid")  map { case id  ~ referenceid  => PowerUnit(id, referenceid, DowntimeCost.findByPowerUnit(id.get), Component.findWithPowerUnit(id.get))
    }
  }
  def simpleWithPowerStation(downtimeCosts:Seq[(Long, Seq[DowntimeCost])], components:Seq[(Long, Seq[Component])]) = {
    get[Option[Long]]("id") ~
      get[Long]("powerstation") ~
      get[Option[String]]("referenceid")  map { case id  ~ powerstation ~ referenceid  => (powerstation, PowerUnit(id, referenceid, findDowntimeCost(id.get, downtimeCosts) , findComponents(id.get, components)))
    }
  }

  def findDowntimeCost(id:Long, downtimeCosts:Seq[(Long, Seq[DowntimeCost])]):Seq[DowntimeCost] = {
    val result = downtimeCosts.find(_._1 == id)
      if(result.isDefined)
        {
          result.get._2
        }
        else {
        Seq.empty
      }
  }
  def findComponents(id:Long, components:Seq[(Long, Seq[Component])]):Seq[Component] = {
    val result = components.find(_._1 == id)
    if(result.isDefined)
    {
      result.get._2
    }
    else {
      Seq.empty
    }
  }

  def findAll(): Seq[PowerUnit] = {
    DB.withConnection { implicit connection =>
      dbMapper.makeSelectStatement(table).as(PowerUnit.simple *)
    }
  }

  def find(id: Long): Option[PowerUnit] = {
    DB.withConnection { implicit connection =>
      dbMapper.makeSelectStatement(table, "id" -> id).as(PowerUnit.simple.singleOpt)
    }
  }



  def findByReference(ref: String): Option[PowerUnit] = {
    DB.withConnection { implicit connection =>
      dbMapper.makeSelectStatement(table, "referenceid" -> ref).as(PowerUnit.simple.singleOpt)
    }
  }


  def findWithPowerStation(id: Long): Seq[PowerUnit] = {
    DB.withConnection { implicit connection =>
      dbMapper.makeSelectStatement(table, "powerStation" -> id).as(PowerUnit.simple *)
    }
  }

  def findAllWithPowerStation(): Seq[(Long, Seq[PowerUnit])] = {
    DB.withConnection { implicit connection =>
    val list:Seq[(Long, PowerUnit)] =  dbMapper.makeSelectStatement(table).as(PowerUnit.simpleWithPowerStation(DowntimeCost.findAllWithPowerUnit(), Component.findAllWithPowerUnit()) *)

    val ids:Seq[Long] = list.map(item => item._1).distinct
    ids.map(id => (id, list.filter(_._1 == id).map(item => item._2)))

    }
  }


  def add(powerStationsId:Long, eq:PowerUnit): Option[Long] = {
    DB.withTransaction {
      implicit connection =>
        dbMapper.makeInsertStatement(table, dbMapper.mapValues("powerStation" -> powerStationsId, "referenceid" -> eq.referenceId))
          .executeInsert() match {
          case Some(long) => Some(long.asInstanceOf[Long])
          case None       => None
        }
    }
  }

  def add(powerStationsId:Long, referenceId:Option[String]): Option[Long] = {
    add(powerStationsId, PowerUnit(None, referenceId, Seq.empty, Seq.empty))
  }



  def update(powerStationsId:Long, eq:PowerUnit): Unit = {
    DB.withTransaction {
      implicit connection =>
        if(find(eq.id.getOrElse(-1)).isDefined){
          dbMapper.makeUpdateStatement(table, "id" -> eq.id, dbMapper.mapValues("powerStation" -> powerStationsId, "referenceid" -> eq.referenceId))
            .executeUpdate()
        }
        else{
          val id = add(powerStationsId, eq)
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

