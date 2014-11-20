package controllers.importers

import models.classification._
import org.apache.poi.ss.usermodel.Row


object ImportPowerStationComponentUnplannedUnavailabilityCosts extends ImportBase {
    def processRow(row: Row) {
      val powerStationName: String = getCellValueAsString(row.getCell(0))



      if (PowerStation.findByName(powerStationName).isDefined) {
        val powerStation: PowerStation = PowerStation.findByName(powerStationName).get

        val startAt: Int = 1
        val stopAt: Int = row.getSheet.getRow(0).getLastCellNum

        println("Importing columns from 1 to  : " + stopAt)

        var i: Int = startAt

        while (i < stopAt) {
          val span: Int = getCellValueAsInt(row.getSheet.getRow(0).getCell(i, Row.CREATE_NULL_AS_BLANK))
          val value: Option[Double] = getCellValueAsNumberOption(row.getCell(i, Row.CREATE_NULL_AS_BLANK))

          if(value.isDefined) {
            val existingSpanOption:Option[DowntimeCost] = powerStation.downtimeCosts.find(_.span == span)

            if (existingSpanOption.isDefined) {
                DowntimeCost.updateForPowerStation(powerStation.id.get, DowntimeCost(existingSpanOption.get.id, existingSpanOption.get.span, value.get, false))
              println("Updated downtimecost for power station " + powerStationName + " for span " + span + " to " + value.get)
              }
              else {
              DowntimeCost.addForPowerStation(powerStation.id.get, DowntimeCost(None, span, value.get, false))
              }
            println("Added downtimecost for power station " + powerStationName + " for span " + span + " to " + value.get)
            }
          else {
            println("Cant import downtimecost for power station " + powerStationName + " for span " + span + ". No value defined!")
          }


          i += 1
        }

      }
    }

  }

