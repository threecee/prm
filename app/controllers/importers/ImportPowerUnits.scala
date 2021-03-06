package controllers.importers

import models.classification.{PowerUnit, Group, PowerStation, Region}
import org.apache.poi.ss.usermodel.{Cell, Row}


object ImportPowerUnits   extends ImportBase
  {



  def processRow(row: Row) {
    val powerUnitId: String = getCellValueAsString(row.getCell(0))
    val powerstationName: String = getCellValueAsString(row.getCell(1))

    if (PowerStation.findByName(powerstationName).isDefined) {
      val station: PowerStation = PowerStation.findByName(powerstationName).get

      if(!PowerUnit.findByReference(powerUnitId).isDefined)
      {
        val refName:Option[String] = if(powerUnitId.length > 0){ Some(powerUnitId) } else { None}

          PowerUnit.add(station.id.get, refName)
        println("Successfully imported power unit: " + powerUnitId + " into power station: " + powerstationName )
      }
      else{
        println("Cant import Power Unit: Power Unit with reference id " + powerUnitId + " already exists.")
      }
    }
    else
    {
      println("Cant import Power Unit: Power Station with name " + powerstationName + " does not exist.")
    }
  }
}

