package controllers.importers

import java.io.{FileInputStream, File}

import controllers.importers.ImportPowerStationComponentRepairs._
import models.classification._
  import org.apache.poi.ss.usermodel.Row
import org.apache.poi.xssf.usermodel.{XSSFSheet, XSSFWorkbook}
import utils.{Timer, ExcelSheet, XLSXStreamReader}

import scala.collection.JavaConverters._
import scala.collection.mutable
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

case class Parameters(componentType:ComponentType, incidents:Seq[Incidents])
case class Incidents(offset:Int, incident:IncidentType)
case class DataSources(componentTypes:Seq[ComponentType], incidentTypes:Seq[IncidentType], powerStations:Seq[PowerStation], componentsByPowerStation:Seq[(Long, Seq[Component])])

object ImportPowerStationComponentRepairs extends ImportBase {

  override def process(source:File): Unit = {
    val streamReader:XLSXStreamReader = new XLSXStreamReader(source)

    val excelSheets:mutable.Buffer[ExcelSheet] = Timer.ptime("processExcel", streamReader.process().asScala)

    val componentTypes:Seq[ComponentType] = Timer.ptime("getComponentTypes", ComponentType.findAll())
    val incidentTypes:Seq[IncidentType] = Timer.ptime("getIncidentTypes", IncidentType.findAll())
    val powerStations:Seq[PowerStation] = Timer.ptime("getPowerStations", PowerStation.findAllNoDeps())

    val componentsByPowerStation:Seq[(Long, Seq[Component])] = Timer.ptime("getComponentsForPowerStations", Component.findAllWithPowerStation())

    val dataSources:DataSources = DataSources(componentTypes, incidentTypes, powerStations, componentsByPowerStation)
    excelSheets.map(sheet => processSheet(sheet, dataSources))

  }



  def processSheet(sh: ExcelSheet, dataSource:DataSources) {
    val paramtersOption: Option[Parameters] = Timer.ptime("processSetup", processSetup(sh, dataSource))

    if (paramtersOption.isDefined) {
      var i = 3
      while (i < sh.numRows) {

        //Future {
        Timer.ptime("processRow", processRow(sh, i, paramtersOption.get, dataSource))
       // }

        i += 1
      }
    }
  }

  def processSetup(sh: ExcelSheet, dataSource:DataSources): Option[Parameters] = {
    val componentTypeName: String = sh.getCellAsString(1,0)
    val componentTypeOption: Option[ComponentType] = dataSource.componentTypes.find(_.name == componentTypeName)

    if (componentTypeOption.isDefined) {


      var offset:Int = 1
      val builder = Seq.newBuilder[Incidents]
      while (sh.getCell(2, offset) != null) {
        val incidentTypeName: String = sh.getCellAsString(0, offset)
        val incidentTypeOption: Option[IncidentType] = dataSource.incidentTypes.find(_.name == incidentTypeName)

        if (!incidentTypeOption.isDefined) {
          println("Incident type " + incidentTypeName + " is not defined, adding now ")
          IncidentType.add(incidentTypeName, "")
        }
        val incidentType: IncidentType = IncidentType.findByName(incidentTypeName).get
        builder += Incidents(offset, incidentType)
        offset += 3
      }
      val incidents: Seq[Incidents] = builder.result()
      Some(Parameters(componentTypeOption.get, incidents))
    }
    else {
      println("Component type " + componentTypeName + " is not defined. Can't import repairs")
      None
    }
  }

  def processRow(row: Row) {}

    def processRow(sheet: ExcelSheet, rowIndex:Int, parameters: Parameters, dataSource:DataSources) {
    val powerStationName: String = sheet.getCellAsString(rowIndex, 0)

      val powerStationOption:Option[PowerStation] = dataSource.powerStations.find(_.name == powerStationName)

    if (powerStationOption.isDefined) {
      val powerStation: PowerStation = powerStationOption.get

      val componentListOption:Option[(Long, Seq[Component])] = dataSource.componentsByPowerStation.find(_._1 == powerStation.id.get)
      if(componentListOption.isDefined) {
      val componentOption: Option[Component] = componentListOption.get._2.find(_.componentType.id.get == parameters.componentType.id.get)
      if (componentOption.isDefined) {
        parameters.incidents.map(incident => {
          val span:Option[Double] = Option(sheet.getCellAsDoubleOption(rowIndex, incident.offset))
          if (span.isDefined) {
            val probability: Option[Double] = Option(sheet.getCellAsDoubleOption(rowIndex, incident.offset + 1))
            if (probability.isDefined) {
              val cost: Option[Double] = Option(sheet.getCellAsDoubleOption(rowIndex, incident.offset + 2))
              if (cost.isDefined) {

                val repairOption: Option[Repair] = Timer.ptime("FindRepair",Repair.findWithComponent(componentOption.get.id.get).find(_.incidenttype.id.get == incident.incident.id.get))
                if (repairOption.isDefined) {
                  Timer.ptime("UpdateRepair", Repair.update(parameters.componentType.id.get, Repair(repairOption.get.id, span.get, cost.get, incident.incident, probability.get)))
                  println("Updated repair data for " + componentOption.get.componentType.name + " at " + powerStationName + " and incident type " + incident.incident.name + " Span: " + span.get + ", Cost: " + cost.get + ", Probability: " + probability.get * 100 + "%")

                } else {
                  Timer.ptime("AddRepair", Repair.add(parameters.componentType.id.get, Repair(None, span.get, cost.get, incident.incident, probability.get)))
                  println("Added repair data for " + powerStationName + " and incident type " + incident.incident.name + " Span: " + span.get + ", Cost: " + cost.get + ", Probability: " + (probability.get * 100) + "%")

                }
              }
              else {
                println("Cost is not defined for " + powerStationName + " and incident type " + incident.incident.name + ". Can't import repairs")
              }
            }
            else {
              println("Probability is not defined for " + powerStationName + " and incident type " + incident.incident.name + ". Can't import repairs")
            }
          }
          else {
            println("Span is not defined for " + powerStationName + " and incident type " + incident.incident.name + ". Can't import repairs")
          }
        })
      }
      else {
        println("Component for Component type " + parameters.componentType.name + " is not defined for power station " + powerStationName + ", cant import repairs")
      }
      }
        else {
          println("Component for Component type " + parameters.componentType.name + " is not defined for power station " + powerStationName + ", cant import repairs")
        }

    }


  }
}

