package controllers.importers

import models.classification._
import org.apache.poi.ss.usermodel.{Cell, Row}
import org.apache.poi.xssf.usermodel.XSSFSheet


object ImportPowerUnitComponentStates extends ImportBase
  {

  def processRow(row: Row) {
    val powerUnitId: String = getCellValueAsString(row.getCell(0))
    val powerstationName: String = getCellValueAsString(row.getCell(1))



    if (PowerUnit.findByReference(powerUnitId).isDefined) {
      val powerUnit: PowerUnit = PowerUnit.findByReference(powerUnitId).get

      val startAt: Int = 2
      val stopAt: Int = row.getSheet.getRow(0).getLastCellNum

      println("Importing columns from 2 to  : " + stopAt)

      var i: Int = startAt

      while (i < stopAt) {
        val name: String = getCellValueAsString(row.getSheet.getRow(0).getCell(i, Row.CREATE_NULL_AS_BLANK))
        val value: Int = getCellValueAsInt(row.getCell(i, Row.CREATE_NULL_AS_BLANK))

        val state: EquipmentState = getState(value)

        val componentType: Option[ComponentType] = ComponentType.findByName(name)

        if (componentType.isDefined && !componentType.get.partOfPowerStation) {
          val existingComponent = powerUnit.components.find(_.componentType.name == name)
          if (existingComponent.isDefined) {
            Component.updateForPowerUnit(powerUnit.id.get, Component(existingComponent.get.id, state, Seq.empty, componentType.get))
          }
          else {
            Component.addForPowerUnit(powerUnit.id.get, Component(None, state, Seq.empty, componentType.get))
          }
          println("Added Component " + name + " with state " + state.value + " to unit " + powerUnit.id.get)
        }
        else {
          println("Cant import Component state for: " + name + ". Component does not exist!")
        }


        i += 1
      }

    }
  }

}

