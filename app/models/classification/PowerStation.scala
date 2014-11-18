package models.classification

import java.util.concurrent.Executors

import anorm.SqlParser._
import anorm._
import play.api.Play.current
import play.api.db.DB
import utils.dbMapper

import scala.concurrent.ExecutionContext

case class PowerStation(
                         id: Option[Long],
                         name: String,
                         powerUnits:Seq[PowerUnit],
                         downtimeCosts:Seq[DowntimeCost],
                         components:Seq[Component],
group:Option[Group],
region:Option[Region]
                         )

object PowerStation {
  private val table: String = "powerstations"

  import play.api.libs.functional.syntax._
  import play.api.libs.json._

  implicit val PowerStationFromJson: Reads[PowerStation] = (
    (__ \ "id").readNullable[Long] ~
      (__ \ "name").read[String] ~
        (__ \ "powerUnits").read[Seq[PowerUnit]]  ~
      (__ \ "downtimeCosts").read[Seq[DowntimeCost]]  ~
      (__ \ "components").read[Seq[Component]] ~
      (__ \ "group").readNullable[Group] ~
      (__ \ "region").readNullable[Region]
    )(PowerStation.apply _)

  implicit val PowerStationToJson: Writes[PowerStation] = (
    (__ \ "id").writeNullable[Long] ~
      (__ \ "name").write[String] ~
      (__ \ "powerUnits").write[Seq[PowerUnit]] ~
      (__ \ "downtimeCosts").write[Seq[DowntimeCost]]  ~
      (__ \ "components").write[Seq[Component]]  ~
      (__ \ "group").writeNullable[Group] ~
      (__ \ "region").writeNullable[Region]
    )((powerStation: PowerStation) => (
    powerStation.id,
    powerStation.name,
    powerStation.powerUnits,
    powerStation.downtimeCosts,
    powerStation.components,
    powerStation.group,
    powerStation.region

    ))


  val executorService = Executors.newFixedThreadPool(4)
  val executionContext = ExecutionContext.fromExecutorService(executorService)

  val simple = {
    get[Option[Long]]("id") ~
      get[String]("name") ~
      get[Option[Long]]("group_id") ~
      get[Option[Long]]("region")  map { case id ~ name ~ group ~ region  => PowerStation(id, name, PowerUnit.findWithPowerStation(id.get), DowntimeCost.findWithPowerStation(id.get), Component.findWithPowerStation(id.get), Group.find(group.getOrElse(-1)) ,  Region.find(region.getOrElse(-1)))
    }
  }

  def findAll(): Seq[PowerStation] = {
    DB.withConnection { implicit connection =>
      dbMapper.makeSelectStatement(table).as(PowerStation.simple *)
    }
  }

  def find(id: Long): Option[PowerStation] = {
    DB.withConnection { implicit connection =>
      dbMapper.makeSelectStatement(table, "id" -> id).as(PowerStation.simple.singleOpt)
    }
  }

  def add(eq:PowerStation): Option[Long] = {
    DB.withTransaction {
      implicit connection =>
        dbMapper.makeInsertStatement(table, dbMapper.mapValues("name" -> eq.name, "group_id" -> getIdOfGroupIfDefined(eq.group), "region" -> getIdOfRegionIfDefined(eq.region)))
          .executeInsert() match {
          case Some(long) => Some(long.asInstanceOf[Long])
          case None       => None
        }
    }
  }

  def getIdOfRegionIfDefined(region: Option[Region]):Option[Long] = {
    if(region.isDefined)
    {
      region.get.id
    }
    else
    {
      None
    }
  }
  def getIdOfGroupIfDefined(group: Option[Group]):Option[Long] = {
    if(group.isDefined)
    {
      group.get.id
    }
    else
    {
      None
    }
  }


  def add(name:String): Option[Long] = {
    add(PowerStation(None, name, Seq.empty, Seq.empty, Seq.empty, None, None))
  }

  def update(eq:PowerStation): Unit = {
    DB.withTransaction {
      implicit connection =>
        if(find(eq.id.getOrElse(-1)).isDefined){
          dbMapper.makeUpdateStatement(table, "id" -> eq.id, dbMapper.mapValues("name" -> eq.name, "group_id" -> getIdOfGroupIfDefined(eq.group), "region" -> getIdOfRegionIfDefined(eq.region)))
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
