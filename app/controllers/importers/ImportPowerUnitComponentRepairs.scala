package controllers.importers

import java.io.File

import models.classification._
import org.apache.poi.ss.usermodel.Row
import play.api.Logger
import utils.{ExcelMapper, ExcelSheet, Timer, XLSXStreamReader}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.collection.JavaConverters._
import scala.collection.mutable
import scala.concurrent.Future

case class PowerUnitDataSources(componentTypes:Seq[ComponentType], incidentTypes:Seq[IncidentType], powerUnits:Seq[PowerUnit])

object ImportPowerUnitComponentRepairs extends ImportBase {

  override def process(source:File): Unit = {
    val streamReader:XLSXStreamReader = new XLSXStreamReader(source)

    val excelSheets:mutable.Buffer[ExcelSheet] = Timer.ptime("processExcel", streamReader.process().asScala)

    val componentTypes:Seq[ComponentType] = Timer.ptime("getComponentTypes", ComponentType.findAll())
    val incidentTypes:Seq[IncidentType] = Timer.ptime("getIncidentTypes", IncidentType.findAll())
    val powerUnits:Seq[PowerUnit] = Timer.ptime("getPowerUnits", PowerUnit.findAll())
    
    val dataSources:PowerUnitDataSources = PowerUnitDataSources(componentTypes, incidentTypes, powerUnits)
    excelSheets.map(sheet => Future { processSheet(new ExcelMapper(sheet), dataSources)})

  }



  def processSheet(sh: ExcelMapper, dataSource:PowerUnitDataSources) {
    val paramtersOption: Option[Parameters] =  processSetup(sh, dataSource)

    if (paramtersOption.isDefined) {
      var i = 3
      while (i < sh.excelSheet.numRows) {

        //Future {
        processRow(sh, i, paramtersOption.get, dataSource)
       // }

        i += 1
      }
    }
  }

  def processSetup(sh: ExcelMapper, dataSource:PowerUnitDataSources): Option[Parameters] = {
    val componentTypeName: Option[String] = sh.getCellAsString(1,0)

    val componentTypeOption: Option[ComponentType] = dataSource.componentTypes.find(_.name == componentTypeName.getOrElse(""))

    if (componentTypeOption.isDefined) {

      var offset: Int = 1
      val builder = Seq.newBuilder[Incidents]
      while (sh.getCell(2, offset).isDefined) {
        val incidentTypeName: Option[String] = sh.getCellAsString(0, offset)
        if (incidentTypeName.isDefined) {
          val incidentTypeOption: Option[IncidentType] = dataSource.incidentTypes.find(_.name == incidentTypeName.getOrElse(""))

          if (!incidentTypeOption.isDefined) {
            Logger.trace("Incident type " + incidentTypeName + " is not defined, adding now ")
            IncidentType.add(incidentTypeName.get, "")
          }
          val incidentType: IncidentType = IncidentType.findByName(incidentTypeName.get).get
          builder += Incidents(offset, incidentType)
          Logger.trace("Added incident type " + incidentType.name)
        }
        else {
          Logger.trace("No incident type defined at cell 2," + offset + " Can't import repairs")
          None
        }
        offset += 3
      }
        val incidents: Seq[Incidents] = builder.result()
        Some(Parameters(componentTypeOption.get, incidents))
      }
    else {
      Logger.trace("Component type " + componentTypeName + " is not defined. Can't import repairs")
      None
    }
  }

  def processRow(row: Row) {}

    def processRow(sheet: ExcelMapper, rowIndex:Int, parameters: Parameters, dataSource:PowerUnitDataSources) {

      val powerUnitId: String = sheet.getCellAsInt(rowIndex, 0).getOrElse(-1).toString()

      val powerUnitOption:Option[PowerUnit] = dataSource.powerUnits.find(_.referenceId.getOrElse("-") == powerUnitId)

    if (powerUnitOption.isDefined) {
      val powerUnit: PowerUnit = powerUnitOption.get

      val componentOption: Option[Component] = powerUnit.components.find(_.componentType.id.get == parameters.componentType.id.get)
      if (componentOption.isDefined) {
        parameters.incidents.map(incident => {
          val span:Option[Double] = sheet.getCellAsDouble(rowIndex, incident.offset)
          if (span.isDefined) {
            val probability: Option[Double] = sheet.getCellAsDouble(rowIndex, incident.offset + 1)
            if (probability.isDefined) {
              val cost: Option[Double] = sheet.getCellAsDouble(rowIndex, incident.offset + 2)
              if (cost.isDefined) {

                val repairOption: Option[Repair] = Timer.ptime("FindRepair",Repair.findWithComponent(componentOption.get.id.get).find(_.incidenttype.id.get == incident.incident.id.get))
                if (repairOption.isDefined) {
                  Repair.update(componentOption.get.id.get, Repair(repairOption.get.id, span.get, cost.get, incident.incident, probability.get))
                  Logger.trace("Updated repair data for " + componentOption.get.componentType.name + " at " + powerUnitId + " and incident type " + incident.incident.name + " Span: " + span.get + ", Cost: " + cost.get + ", Probability: " + probability.get * 100 + "%")

                } else {
                   Repair.add(componentOption.get.id.get, Repair(None, span.get, cost.get, incident.incident, probability.get))
                  Logger.trace("Added repair data for " + powerUnitId + " and incident type " + incident.incident.name + " Span: " + span.get + ", Cost: " + cost.get + ", Probability: " + (probability.get * 100) + "%")

                }
              }
              else {
                Logger.trace("Cost is not defined for " + powerUnitId + " and incident type " + incident.incident.name + ". Can't import repairs")
              }
            }
            else {
              Logger.trace("Probability is not defined for " + powerUnitId + " and incident type " + incident.incident.name + ". Can't import repairs")
            }
          }
          else {
            Logger.trace("Span is not defined for " + powerUnitId + " and incident type " + incident.incident.name + ". Can't import repairs")
          }
        })
      }
      else {
        Logger.trace("Component for Component type " + parameters.componentType.name + " is not defined for power station " + powerUnitId + ", cant import repairs")
      }

    }


  }
}

