package controllers.importers

import java.io.{File, FileInputStream}

import controllers.base.BaseController
import models.classification.{Group, PowerStation, Region}
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.xssf.usermodel.{XSSFSheet, XSSFWorkbook}
import play.api.libs.json.Json
import play.api.mvc.Action


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

  def process(source:File): Unit = {
    val fis:FileInputStream = new FileInputStream(source)
    val wb:XSSFWorkbook = new XSSFWorkbook(fis)

    val sh:XSSFSheet = wb.getSheetAt(0)

    var i = 1
    while(i < sh.getLastRowNum)
    {
      val row:Row = sh.getRow(i)

      processRow(row)

      i += 1
    }



  }


   def processRow(row: Row)
}

