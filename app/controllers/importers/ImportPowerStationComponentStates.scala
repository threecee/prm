package controllers.importers

import models.classification._
import org.apache.poi.ss.usermodel.{Cell, Row}
import org.apache.poi.xssf.usermodel.XSSFSheet


object ImportPowerStationComponentStates extends ImportBase
{
  def processRow(row: Row) {
    val powerStationName: String = getCellValueAsString(row.getCell(0))



    if (PowerStation.findByName(powerStationName).isDefined) {
      val powerStation: PowerStation = PowerStation.findByName(powerStationName).get

      val startAt: Int = 1
      val stopAt: Int = row.getSheet.getRow(0).getLastCellNum

      println("Importing columns from 1 to  : " + stopAt)

      var i: Int = startAt

      while (i <= stopAt) {
        val name: String = getCellValueAsString(row.getSheet.getRow(0).getCell(i, Row.CREATE_NULL_AS_BLANK))
        val value: Int = getCellValueAsInt(row.getCell(i, Row.CREATE_NULL_AS_BLANK))

        val state: EquipmentState = getState(value)

        val componentType: Option[ComponentType] = ComponentType.findByName(name)

        if (componentType.isDefined && componentType.get.partOfPowerStation) {
          val existingComponent = powerStation.components.find(_.componentType.name == name)
          if (existingComponent.isDefined) {
            Component.updateForPowerStation(powerStation.id.get, Component(existingComponent.get.id, state,  componentType.get))
          }
          else {
            Component.addForPowerStation(powerStation.id.get, Component(None, state, componentType.get))
          }
          println("Added Component " + name + " with state " + state.value + " to stations " + powerStation.id.get)
        }
        else {
          println("Cant import Component state for: " + name + ". Component does not exist!")
        }


        i += 1
      }

    }
  }


}

