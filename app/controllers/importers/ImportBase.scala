package controllers.importers

import java.io.{File, FileInputStream}

import controllers.base.BaseController
import models.classification.EquipmentState
import org.apache.poi.ss.usermodel.{Cell, Row}
import org.apache.poi.xssf.usermodel.{XSSFSheet, XSSFWorkbook}
import play.api.libs.json.Json
import play.api.mvc.Action
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future


abstract class ImportBase   extends BaseController
  {

  def importer = Action(parse.multipartFormData) { request =>
    request.body.file("import").map { source =>
      import java.io.File
      val filename = source.filename
      val contentType = source.contentType
      var reference:File = File.createTempFile("IMPORT_","")
      source.ref.moveTo(reference, true)
      process(reference)
      Accepted(Json.toJson("{}"))
    }.getOrElse {
      BadRequest(Json.toJson("{}"))
    }

    }

  def getCellValueAsString(cell:Cell):String = {
    cell.getCellType match {
      case Cell.CELL_TYPE_NUMERIC => {
        val numeric:Double = cell.getNumericCellValue
        if(numeric == numeric.toInt)
        {
          return numeric.toInt.toString
        }
        else return numeric.toString
      }
      case Cell.CELL_TYPE_STRING => cell.getStringCellValue
      case _ => ""

    }


  }


 def getCellValueAsNumber(cell:Cell):Double = {
  getCellValueAsNumberOption(cell).getOrElse(0)
  }

  def getCellValueAsNumberOption(cell:Cell):Option[Double] = {
    cell.getCellType match {
      case Cell.CELL_TYPE_NUMERIC => {
        Some(cell.getNumericCellValue)
      }
      case Cell.CELL_TYPE_STRING => {
        val value: String = cell.getStringCellValue
        if (value.length > 0) {
          try {
            return Some(value.toDouble)
          } catch {
            case e: Exception =>
              return None
          }
        }
        return None
      }
      case _ => return None

    }


  }

  def getCellValueAsInt(cell:Cell):Int = {
    getCellValueAsIntOption(cell).getOrElse(0)
  }
  def getCellValueAsIntOption(cell:Cell):Option[Int] = {
    val doubleOption:Option[Double] = getCellValueAsNumberOption(cell)

    if(doubleOption.isDefined)
    {
      Some(doubleOption.get.toInt)
    }
    else
    {
      None
    }

  }



  def process(source:File): Unit = {
    val fis:FileInputStream = new FileInputStream(source)
    val wb:XSSFWorkbook = new XSSFWorkbook(fis)

    processSheets(wb)

  }


  def processSheets(wb: XSSFWorkbook) {
    val sh: XSSFSheet = wb.getSheetAt(0)

    processSheet(sh)
  }

  def processSheet(sh: XSSFSheet) {
    var i = 1
    while (i <= sh.getLastRowNum) {
      val row: Row = sh.getRow(i)

      Future {
        processRow(row)
      }

      i += 1
    }
  }

  def getState(value: Int):EquipmentState = {
    val stateOption: Option[EquipmentState] = EquipmentState.findByReference(value)
    if (!stateOption.isDefined) {
      EquipmentState.add(EquipmentState(None, value))
    }
    return EquipmentState.findByReference(value).get

  }



  def processRow(row: Row)
}

