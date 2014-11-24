package controllers.importers

import models.classification._
import org.apache.poi.ss.usermodel.Row


object ImportPowerUnitComponentUnplannedUnavailabilityCosts extends ImportBase {
    def processRow(row: Row) {
      val powerUnitName: String = getCellValueAsString(row.getCell(0))



      if (PowerUnit.findByReference(powerUnitName).isDefined) {
        val powerUnit: PowerUnit = PowerUnit.findByReference(powerUnitName).get

        val startAt: Int = 2
        val stopAt: Int = row.getSheet.getRow(0).getLastCellNum

        println("Importing columns from 1 to  : " + stopAt)

        var i: Int = startAt

        while (i <= stopAt) {
          val span: Int = getCellValueAsInt(row.getSheet.getRow(0).getCell(i, Row.CREATE_NULL_AS_BLANK))
          val value: Option[Double] = getCellValueAsNumberOption(row.getCell(i, Row.CREATE_NULL_AS_BLANK))

          if(value.isDefined) {
            val existingSpanOption:Option[DowntimeCost] = powerUnit.downtimeCosts.find(_.span == span)

            if (existingSpanOption.isDefined) {
                DowntimeCost.updateForPowerUnit(powerUnit.id.get, DowntimeCost(existingSpanOption.get.id, existingSpanOption.get.span, value.get, false))
              println("Updated downtimecost for power unit " + powerUnitName + " for span " + span + " to " + value.get)
              }
              else {
              DowntimeCost.addForPowerUnit(powerUnit.id.get, DowntimeCost(None, span, value.get, false))
              println("Added downtimecost for power unit " + powerUnitName + " for span " + span + " to " + value.get)
              }
            }
          else {
            println("Cant import downtimecost for power unit " + powerUnitName + " for span " + span + ". No value defined!")
          }


          i += 1
        }

      }
      else {
        println("Cant find power unit " + powerUnitName)
      }
    }

  }

