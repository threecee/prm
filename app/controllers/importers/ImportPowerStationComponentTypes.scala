package controllers.importers

import models.classification._
import org.apache.poi.ss.usermodel.Row


object ImportPowerStationComponentTypes extends ImportBase {

  def processRow(row: Row) {
    val componentTypeName: String = getCellValueAsString(row.getCell(0))

    if (!ComponentType.findByName(componentTypeName).isDefined) {

      ComponentType.add(ComponentType(None, componentTypeName, Seq.empty, true))
      println("Added new Component type with name " + componentTypeName + " to power stations")
    }
  }

}

