package models.classification

import java.util.concurrent.Executors

import anorm.SqlParser._
import anorm._
import play.api.Play.current
import play.api.db.DB
import utils.dbMapper

import scala.concurrent.ExecutionContext

case class Region(
                           id: Option[Long],
                           name: String
                           )

object Region {
  private val table: String = "regions"

  import play.api.libs.functional.syntax._
  import play.api.libs.json._

  implicit val RegionFromJson: Reads[Region] = (
    (__ \ "id").readNullable[Long] ~
      (__ \ "name").read[String]
    )(Region.apply _)

  implicit val RegionToJson: Writes[Region] = (
    (__ \ "id").writeNullable[Long] ~
      (__ \ "name").write[String]
    )((region: Region) => (
    region.id,
    region.name
    ))


  val executorService = Executors.newFixedThreadPool(4)
  val executionContext = ExecutionContext.fromExecutorService(executorService)

  val simple = {
    get[Option[Long]]("id") ~
      get[String]("name") map { case id ~ name => Region(id, name)
    }
  }

  def findAll(): Seq[Region] = {
    DB.withConnection { implicit connection =>
      dbMapper.makeSelectStatement(table).as(Region.simple *)
    }
  }

  def find(id: Long): Option[Region] = {
    DB.withConnection { implicit connection =>
      dbMapper.makeSelectStatement(table, "id" -> id).as(Region.simple.singleOpt)
    }
  }
  def findByName(name: String): Option[Region] = {
    DB.withConnection { implicit connection =>
      dbMapper.makeSelectStatement(table, "name" -> name).as(Region.simple.singleOpt)
    }
  }



  def add(eq:Region): Option[Long] = {
    DB.withTransaction {
      implicit connection =>
        dbMapper.makeInsertStatement(table, dbMapper.mapValues("name" -> eq.name))
          .executeInsert() match {
          case Some(long) => Some(long.asInstanceOf[Long])
          case None       => None
        }
    }
  }

  def update(eq:Region): Unit = {
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
