package controllers.importers

import java.io.File.TempDirectory
import java.io.{FileInputStream, File}

import controllers.base.BaseController
import models.classification.{Region, Group, PowerStation}
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.xssf.usermodel.{XSSFSheet, XSSFWorkbook}
import play.api.libs.json.Json
import play.api.mvc.Action


object ImportPowerStations   extends BaseController
  {

  def powerstations = Action(parse.multipartFormData) { request =>
    request.body.file("import").map { source =>
      import java.io.File
      val filename = source.filename
      val contentType = source.contentType
      var reference:File = File.createTempFile("IMPORT_POWERSTATIONS_","")
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
    println(sh.getLastRowNum())
    println("Name: "+sh.getSheetName())

    var i = 1
    while(i < sh.getLastRowNum)
    {
      val row:Row = sh.getRow(i)

      val powerstationName:String = row.getCell(0).getStringCellValue()
      val regionName:String = row.getCell(1).getStringCellValue()
      val groupName:String = row.getCell(2).getStringCellValue()

      if(!PowerStation.findByName(powerstationName).isDefined)
      {
        PowerStation.add(powerstationName)
      }

      val station:PowerStation = PowerStation.findByName(powerstationName).get

      if(regionName.length > 0 && !Region.findByName(regionName).isDefined)
      {
        Region.add(Region(None, regionName))
      }

      val region:Option[Region] = Region.findByName(regionName)


      if(groupName.length > 0 && !Group.findByName(groupName).isDefined)
      {
        Group.add(Group(None, groupName))
      }

      val group:Option[Group] = Group.findByName(groupName)


     PowerStation.update(PowerStation(station.id, station.name, station.powerUnits, station.downtimeCosts, station.components, group, region))

      i += 1
    }



  }


}

