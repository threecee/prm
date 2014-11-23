package controllers.importers

import models.classification._
import org.apache.poi.ss.usermodel.Row


object ImportGeneralLifespans extends ImportBase {
  def processRow(row: Row) {
    val equipmentStateValue: Int = getCellValueAsInt(row.getCell(0))

    if (!EquipmentState.findByReference(equipmentStateValue).isDefined) {
      EquipmentState.add(EquipmentState(None, equipmentStateValue))
    }

    val equipmentState: EquipmentState = EquipmentState.findByReference(equipmentStateValue).get


    val startAt: Int = 2
    val stopAt: Int = row.getSheet.getRow(0).getLastCellNum

    println("Importing columns from 1 to  : " + stopAt)

    var i: Int = startAt

    while (i < stopAt) {
      val componentTypeName: String = getCellValueAsString(row.getSheet.getRow(0).getCell(i, Row.CREATE_NULL_AS_BLANK))
      val value: Option[Int] = getCellValueAsIntOption(row.getCell(i, Row.CREATE_NULL_AS_BLANK))

      if (value.isDefined) {
        val componentTypeOption: Option[ComponentType] = ComponentType.findByName(componentTypeName)

        if (componentTypeOption.isDefined) {
          val existingValueOption: Option[ResidualLifeSpan] = componentTypeOption.get.residuallifespans.find(_.equipmentState.value == equipmentState.value)

          if (existingValueOption.isDefined) {
            ResidualLifeSpan.update(componentTypeOption.get.id.get, ResidualLifeSpan(existingValueOption.get.id, value.get, equipmentState))
            println("Updated residual life span to " + value.get + " for component type " + componentTypeName + " with equipment state " + equipmentState.value)

          }
          else {
            ResidualLifeSpan.add(componentTypeOption.get.id.get, ResidualLifeSpan(None, value.get, equipmentState))
            println("Added residual life span " + value.get + " for component type " + componentTypeName + " with equipment state " + equipmentState.value)
          }
        }
        else {
          println("Can't add residual life span " + value.get + " for component type " + componentTypeName + " with equipment state " + equipmentState.value + " - component type not found!")
        }
      }
      else {
        println("Can't add residual life span for component type " + componentTypeName + " with equipment state " + equipmentState.value + " - no span defined!")
      }
      i += 1
    }

  }

}

