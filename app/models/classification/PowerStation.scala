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
                         powerUnits:Seq[PowerUnit]
                         )

object PowerStation {
  private val table: String = "powerstations"

  import play.api.libs.functional.syntax._
  import play.api.libs.json._

  implicit val PowerStationFromJson: Reads[PowerStation] = (
    (__ \ "id").readNullable[Long] ~
      (__ \ "name").read[String] ~
        (__ \ "powerUnits").read[Seq[PowerUnit]]
    )(PowerStation.apply _)

  implicit val PowerStationToJson: Writes[PowerStation] = (
    (__ \ "id").writeNullable[Long] ~
      (__ \ "name").write[String] ~
      (__ \ "powerUnits").write[Seq[PowerUnit]]
    )((powerStation: PowerStation) => (
    powerStation.id,
    powerStation.name,
    powerStation.powerUnits
    ))


  val executorService = Executors.newFixedThreadPool(4)
  val executionContext = ExecutionContext.fromExecutorService(executorService)

  val simple = {
    get[Option[Long]]("id") ~
      get[String]("name") map { case id ~ name  => PowerStation(id, name, PowerUnit.findWithPowerStation(id.get))
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
        dbMapper.makeInsertStatement(table, dbMapper.mapValues("name" -> eq.name))
          .executeInsert() match {
          case Some(long) => Some(long.asInstanceOf[Long])
          case None       => None
        }
    }
  }

  def add(name:String): Option[Long] = {
    add(PowerStation(None, name, Seq.empty))
  }

  def update(eq:PowerStation): Unit = {
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
