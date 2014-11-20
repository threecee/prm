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

  val simpleNoDeps = {
    get[Option[Long]]("id") ~
      get[String]("name") ~
      get[Option[Long]]("group_id") ~
      get[Option[Long]]("region")  map { case id ~ name ~ group ~ region  => PowerStation(id, name, Seq.empty,  Seq.empty,  Seq.empty, None,  None)
    }
  }

  def simpleFast(units:Seq[(Long, Seq[PowerUnit])], groups:Seq[Group], regions:Seq[Region], downtimeCosts:Seq[(Long, Seq[DowntimeCost])], components:Seq[(Long, Seq[Component])]) = {
    get[Option[Long]]("id") ~
      get[String]("name") ~
      get[Option[Long]]("group_id") ~
      get[Option[Long]]("region")  map { case id ~ name ~ group ~ region  => PowerStation(id, name, findPowerUnit(id.get, units), findDowntimeCost(id.get, downtimeCosts), findComponents(id.get, components), groups.find(_.id.get == group.getOrElse(-1)), regions.find(_.id.get == region.getOrElse(-1)) )
    }
  }

  def findPowerUnit(id:Long, units:Seq[(Long, Seq[PowerUnit])]):Seq[PowerUnit] = {
    val result = units.find(_._1 == id)
    if(result.isDefined)
    {
      result.get._2
    }
    else {
      Seq.empty
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

  def findAll(): Seq[PowerStation] = {
    DB.withConnection { implicit connection =>
      dbMapper.makeSelectStatement(table).as(PowerStation.simpleFast(PowerUnit.findAllWithPowerStation(), Group.findAll(), Region.findAll(), DowntimeCost.findAllWithPowerStation(), Component.findAllWithPowerStation()) *)
    }
  }
  def findAllNoDeps(): Seq[PowerStation] = {
    DB.withConnection { implicit connection =>
      dbMapper.makeSelectStatement(table).as(PowerStation.simpleNoDeps *)
    }
  }

  def find(id: Long): Option[PowerStation] = {
    DB.withConnection { implicit connection =>
      dbMapper.makeSelectStatement(table, "id" -> id).as(PowerStation.simple.singleOpt)
    }
  }

  def findByName(name: String): Option[PowerStation] = {
    DB.withConnection { implicit connection =>
      dbMapper.makeSelectStatement(table, "name" -> name).as(PowerStation.simple.singleOpt)
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
