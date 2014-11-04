
package models.classification

import java.util.concurrent.Executors

import anorm.SqlParser._
import anorm._
import play.api.Play.current
import play.api.db.DB
import utils.dbMapper

import scala.concurrent.ExecutionContext

case class Repair(
                             id: Option[Long],
                             span: Double,
                             cost:Double,
                             incidenttype:IncidentType,
                             probability:Double
                             )

object Repair {
  private val table: String = "repairs"

  import play.api.libs.functional.syntax._
  import play.api.libs.json._

  implicit val RepairFromJson: Reads[Repair] = (
    (__ \ "id").readNullable[Long] ~
      (__ \ "span").read[Double] ~
      (__ \ "cost").read[Double] ~
      (__ \ "incidenttype").read[IncidentType]  ~
        (__ \ "probability").read[Double]
    )(Repair.apply _)

  implicit val RepairToJson: Writes[Repair] = (
    (__ \ "id").writeNullable[Long] ~
      (__ \ "span").write[Double] ~
      (__ \ "cost").write[Double] ~
      (__ \ "incidenttype").write[IncidentType]  ~
      (__ \ "probability").write[Double]
    )((repair: Repair) => (
    repair.id,
    repair.span,
    repair.cost,
    repair.incidenttype,
    repair.probability
    ))


  val executorService = Executors.newFixedThreadPool(4)
  val executionContext = ExecutionContext.fromExecutorService(executorService)

  val simple = {
    get[Option[Long]]("id") ~
      get[Double]("span") ~
      get[Double]("cost") ~
      get[Long]("incidenttype") ~
      get[Double]("probability") map { case id ~ span ~ cost ~ incidenttype ~ probability => Repair(id, span, cost, IncidentType.find(incidenttype).get, probability)
    }
  }

  def findAll(): Seq[Repair] = {
    DB.withConnection { implicit connection =>
      dbMapper.makeSelectStatement(table).as(Repair.simple *)
    }
  }

  def find(id: Long): Option[Repair] = {
    DB.withConnection { implicit connection =>
      dbMapper.makeSelectStatement(table, "id" -> id).as(Repair.simple.singleOpt)
    }
  }

  def findWithComponentType(id: Long): Seq[Repair] = {
    DB.withConnection { implicit connection =>
      dbMapper.makeSelectStatement(table, "componenttype" -> id).as(Repair.simple *)
    }
  }


  def add(componentId:Long, eq:Repair): Option[Long] = {
    DB.withTransaction {
      implicit connection =>
        dbMapper.makeInsertStatement(table, dbMapper.mapValues("span" -> eq.span, "cost" -> eq.cost, "incidenttype" -> eq.incidenttype.id.get, "probability" -> eq.probability, "componenttype" -> componentId))
          .executeInsert() match {
          case Some(long) => Some(long.asInstanceOf[Long])
          case None       => None
        }
    }
  }

  def add(componentId:Long, incidentId:Long, span:Double, cost:Double, probability:Double): Option[Long] = {
    DB.withTransaction {
      implicit connection =>
        dbMapper.makeInsertStatement(table, dbMapper.mapValues("span" -> span, "cost" -> cost, "incidenttype" -> incidentId, "probability" -> probability, "componenttype" -> componentId))
          .executeInsert() match {
          case Some(long) => Some(long.asInstanceOf[Long])
          case None       => None
        }
    }
  }



  def update(componentId:Long, eq:Repair): Unit = {
    DB.withTransaction {
      implicit connection =>
        if(find(eq.id.getOrElse(-1)).isDefined){
          dbMapper.makeUpdateStatement(table, "id" -> eq.id, dbMapper.mapValues("span" -> eq.span, "cost" -> eq.cost, "incidenttype" -> eq.incidenttype.id.get, "probability" -> eq.probability, "componenttype" -> componentId))
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

