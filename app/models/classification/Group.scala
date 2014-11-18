package models.classification

import java.util.concurrent.Executors

import anorm.SqlParser._
import anorm._
import play.api.Play.current
import play.api.db.DB
import utils.dbMapper

import scala.concurrent.ExecutionContext

case class Group(
                   id: Option[Long],
                   name: String
                   )

object Group {
  private val table: String = "groups"

  import play.api.libs.functional.syntax._
  import play.api.libs.json._

  implicit val GroupFromJson: Reads[Group] = (
    (__ \ "id").readNullable[Long] ~
      (__ \ "name").read[String]
    )(Group.apply _)

  implicit val GroupToJson: Writes[Group] = (
    (__ \ "id").writeNullable[Long] ~
      (__ \ "name").write[String]
    )((group: Group) => (
    group.id,
    group.name
    ))


  val executorService = Executors.newFixedThreadPool(4)
  val executionContext = ExecutionContext.fromExecutorService(executorService)

  val simple = {
    get[Option[Long]]("id") ~
      get[String]("name") map { case id ~ name => Group(id, name)
    }
  }

  def findAll(): Seq[Group] = {
    DB.withConnection { implicit connection =>
      dbMapper.makeSelectStatement(table).as(Group.simple *)
    }
  }

  def find(id: Long): Option[Group] = {
    DB.withConnection { implicit connection =>
      dbMapper.makeSelectStatement(table, "id" -> id).as(Group.simple.singleOpt)
    }
  }

  def add(eq:Group): Option[Long] = {
    DB.withTransaction {
      implicit connection =>
        dbMapper.makeInsertStatement(table, dbMapper.mapValues("name" -> eq.name))
          .executeInsert() match {
          case Some(long) => Some(long.asInstanceOf[Long])
          case None       => None
        }
    }
  }

  def update(eq:Group): Unit = {
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
