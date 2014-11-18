package models.classification

import java.util.concurrent.Executors

import anorm.SqlParser._
import anorm._
import play.api.Play.current
import play.api.db.DB
import utils.dbMapper

import scala.concurrent.ExecutionContext

case class DowntimeCost(
                         id: Option[Long],
                         span: Int,
                         cost:Double,
                         planned:Boolean
                         )

object DowntimeCost {


  private val table: String = "downtimecosts"

  import play.api.libs.functional.syntax._
  import play.api.libs.json._

  implicit val DowntimeCostFromJson: Reads[DowntimeCost] = (
    (__ \ "id").readNullable[Long] ~
      (__ \ "span").read[Int] ~
      (__ \ "cost").read[Double] ~
      (__ \ "planned").read[Boolean]
    )(DowntimeCost.apply _)

  implicit val DowntimeCostToJson: Writes[DowntimeCost] = (
    (__ \ "id").writeNullable[Long] ~
      (__ \ "span").write[Int] ~
      (__ \ "cost").write[Double] ~
      (__ \ "planned").write[Boolean]
    )((downtimeCost: DowntimeCost) => (
    downtimeCost.id,
    downtimeCost.span,
    downtimeCost.cost,
    downtimeCost.planned
    ))


  val executorService = Executors.newFixedThreadPool(4)
  val executionContext = ExecutionContext.fromExecutorService(executorService)

  val simple = {
    get[Option[Long]]("id") ~
      get[Int]("span") ~
      get[Double]("cost") ~
      get[Boolean]("planned") map { case id ~ span ~ cost ~ planned => DowntimeCost(id, span, cost, planned)
    }
  }


  val simpleWithPowerStation = {
    get[Option[Long]]("id") ~
      get[Int]("span") ~
      get[Double]("cost") ~
      get[Boolean]("planned") ~
      get[Option[Long]]("powerstation") map { case id ~ span ~ cost ~ planned ~ powerstation =>  (powerstation, DowntimeCost(id, span, cost, planned))
    }
  }

  val simpleWithPowerUnit = {
    get[Option[Long]]("id") ~
      get[Int]("span") ~
      get[Double]("cost") ~
      get[Boolean]("planned") ~
      get[Option[Long]]("powerunit") map { case id ~ span ~ cost ~ planned ~ powerunit =>  (powerunit, DowntimeCost(id, span, cost, planned))
    }
  }

  def findAll(): Seq[DowntimeCost] = {
    DB.withConnection { implicit connection =>
      dbMapper.makeSelectStatement(table).as(DowntimeCost.simple *)
    }
  }

  def find(id: Long): Option[DowntimeCost] = {
    DB.withConnection { implicit connection =>
      dbMapper.makeSelectStatement(table, "id" -> id).as(DowntimeCost.simple.singleOpt)
    }
  }

  def findByPowerUnit(id: Long): Seq[DowntimeCost] = {
    DB.withConnection { implicit connection =>
      dbMapper.makeSelectStatement(table, "powerunit" -> id).as(DowntimeCost.simple *)
    }
  }
  def findWithPowerStation(id: Long): Seq[DowntimeCost] = {
    DB.withConnection { implicit connection =>
      dbMapper.makeSelectStatement(table, "powerstation" -> id).as(DowntimeCost.simple *)
    }
  }
  def findAllWithPowerStation(): Seq[(Long, Seq[DowntimeCost])] = {
    DB.withConnection { implicit connection =>
      val list:Seq[(Option[Long], DowntimeCost)] =  dbMapper.makeSelectStatement(table).as(DowntimeCost.simpleWithPowerStation *)

      val ids:Seq[Long] = list.filter(_._1.isDefined).map(item => item._1.get).distinct
      ids.map(id => (id, list.filter(_._1.get == id).map(item => item._2)))

    }
  }
  def findAllWithPowerUnit(): Seq[(Long, Seq[DowntimeCost])] = {
    DB.withConnection { implicit connection =>
      val list:Seq[(Option[Long], DowntimeCost)] =  dbMapper.makeSelectStatement(table).as(DowntimeCost.simpleWithPowerUnit *)

      val ids:Seq[Long] = list.filter(_._1.isDefined).map(item => item._1.get).distinct
      ids.map(id => (id, list.filter(_._1.get == id).map(item => item._2)))

    }
  }

  def add(powerUnitId:Long, eq:DowntimeCost): Option[Long] = {
    DB.withTransaction {
      implicit connection =>
        dbMapper.makeInsertStatement(table, dbMapper.mapValues("powerunit" -> powerUnitId, "span" -> eq.span, "cost" -> eq.cost, "planned" -> eq.planned))
          .executeInsert() match {
          case Some(long) => Some(long.asInstanceOf[Long])
          case None       => None
        }
    }
  }

  def add(powerUnitId:Long, span:Int, cost:Double, planned:Boolean): Option[Long] = {
    add(powerUnitId, DowntimeCost(None, span, cost, planned))
  }

  def update(powerUnitId:Long, eq:DowntimeCost): Unit = {
    DB.withTransaction {
      implicit connection =>
        if(find(eq.id.getOrElse(-1)).isDefined){
          dbMapper.makeUpdateStatement(table, "id" -> eq.id, dbMapper.mapValues("span" -> eq.span, "cost" -> eq.cost, "planned" -> eq.planned))
            .executeUpdate()
        }
        else{
          val id = add(powerUnitId, eq)
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
