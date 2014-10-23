package utils

import java.util.Date

import anorm._
import org.joda.time.DateTime
import org.joda.time.format.{DateTimeFormat, DateTimeFormatter}
import play.api.Logger
import play.libs.F.None

import scala.None


object dbMapper {


  implicit def dateTimeToStatement = new ToStatement[DateTime] {
    def set(s: java.sql.PreparedStatement, index: Int, aValue: DateTime): Unit = {
      s.setTimestamp(index, new java.sql.Timestamp(aValue.withMillisOfSecond(0).getMillis()) )
      Logger.trace("Converting DateTime to db:" + aValue.toString("yyyyMMdd") +  " " + aValue.withMillisOfSecond(0).getMillis())
    }
  }

  def mapValues(tuples:(String, Any)*):Seq[(String, Any)] = {
    val allTuples = Seq.newBuilder[(String, Any)]

    tuples.map(tuple => {
      allTuples += tuple
    })
    allTuples.result()
  }

  def makeDeleteStatement(table:String, idTuple:(String, Any)):SimpleSql[Row] = {
    val idKey:String = idTuple._1
    val idValue:Any = idTuple._2

    SQL(s"""delete from $table where $idKey = ($idValue)""").on(dbMapper.mapTuple(idTuple))
  }

  def makeInsertStatement(table:String, tuples:Seq[(String, Any)]):SimpleSql[Row] = {
    val definedTuples = filterTuples(tuples)

    val listNames:String = definedTuples.map(tuple => tuple._1).mkString(", ")
    val listValues:String = definedTuples.map(tuple => "{" + tuple._1 + "}").mkString(", ")

    SQL(s"""insert into $table ($listNames) values($listValues)""").on(dbMapper.mapQuery(tuples):_*)
  }

  def filterTuples(tuples: Seq[(String, Any)]): Seq[(String, Any)] = {
    tuples.filterNot(value => value._2.isInstanceOf[Option[Any]] && !value._2.asInstanceOf[Option[Any]].isDefined)
  }

  def makeUpdateStatement(table:String, idTuple:(String, Any), tuples:Seq[(String, Any)]):SimpleSql[Row] = {
    val definedTuples = filterTuples(tuples)

    val listUpdates:String = definedTuples.map(tuple => tuple._1 + " =  ({" + tuple._1 + "})").mkString(", ")
    val idKey:String = idTuple._1

    SQL(s"""update $table set $listUpdates where $idKey = ($idKey)""").on(dbMapper.mapQuery(merge(idTuple, tuples)):_* )
  }
  def makeSelectStatement(table:String, idTuple:(String, Any)):SimpleSql[Row] = {
    val idKey:String = idTuple._1
    val idValue:Any = idTuple._2

    SQL(s"""select * from $table where $idKey = ($idValue)""").on(dbMapper.mapTuple(idTuple))
  }

  def makeSelectStatement(table:String):SimpleSql[Row] = {
    SQL(s"""select * from $table""")
  }

  def merge(idTuple:(String, Any), tuples:Seq[(String, Any)]):Seq[(String, Any)] = {
    val allTuples = Seq.newBuilder[(String, Any)]

    tuples.map(tuple => {
      allTuples += tuple
    })
    allTuples += idTuple

    allTuples.result()
  }

  def merge(tuples1:Seq[(String, Any)], tuples2:Seq[(String, Any)]):Seq[(String, Any)] = {
    val allTuples = Seq.newBuilder[(String, Any)]

    tuples1.map(tuple => {
      allTuples += tuple
    })
    tuples2.map(tuple => {
      allTuples += tuple
    })

    allTuples.result()
  }

  def mapTuple(tuple:(String, Any)):NamedParameter = {
    NamedParameter(tuple._1, tuple._2 match {
      case a:Int => ParameterValue.toParameterValue[Int](a)
      case a:Date => ParameterValue.toParameterValue[Date](a)
      case a:DateTime => ParameterValue.toParameterValue[DateTime](a)
      case a:String => ParameterValue.toParameterValue[String](a)
      case a:Double => ParameterValue.toParameterValue[Double](a)
      case a:Long => ParameterValue.toParameterValue[Long](a)
      case a:Boolean => ParameterValue.toParameterValue[Boolean](a)
      case Some(x) => x match {
        case a: Int => ParameterValue.toParameterValue[Int](a)
        case a: String => ParameterValue.toParameterValue[String](a)
        case a: Long => ParameterValue.toParameterValue[Long](a)
        case a:Double => ParameterValue.toParameterValue[Double](a)
        case a:Boolean => ParameterValue.toParameterValue[Boolean](a)
        case a:Date => ParameterValue.toParameterValue[Date](a)
        case a:DateTime => ParameterValue.toParameterValue[DateTime](a)
        case _ => throw new IllegalStateException("Can't convert type to ANORM: " + tuple._2.getClass)
      }

      case scala.None =>  ParameterValue.toParameterValue(scala.None)
      case _ => throw new IllegalStateException("Can't convert type to ANORM: " + tuple._2.getClass)
    } )
  }


  def mapQueries(tuples: (String, Any)*):Seq[NamedParameter] = {

    val params = Seq.newBuilder[NamedParameter]

    tuples.map(tuple => {

      params += mapTuple(tuple)

    })

    params.result()
  }


  def mapQuery(tuples: Seq[(String, Any)]):Seq[NamedParameter] = {

    val params = Seq.newBuilder[NamedParameter]

    tuples.map(tuple => {

      params += mapTuple(tuple)

    })

    params.result()
  }



}

