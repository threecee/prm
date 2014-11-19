package controllers.importers

import models.classification.{Group, PowerStation, Region}
import org.apache.poi.ss.usermodel.Row


object ImportPowerStations   extends ImportBase
  {


  def processRow(row: Row) {
    val powerstationName: String = getCellValueAsString(row.getCell(0))
    val regionName: String = getCellValueAsString(row.getCell(1))
    val groupName: String = getCellValueAsString(row.getCell(2))

    if (!PowerStation.findByName(powerstationName).isDefined) {
      PowerStation.add(powerstationName)
      println("Power station: " + powerstationName + " created.")

    }
    else {
      println("Power station: " + powerstationName + " found, updating.")
    }


    val station: PowerStation = PowerStation.findByName(powerstationName).get

    if (regionName.length > 0 && !Region.findByName(regionName).isDefined) {
      Region.add(Region(None, regionName))
      println("Region: " + regionName + " created.")
    }

    val region: Option[Region] = Region.findByName(regionName)


    if (groupName.length > 0 && !Group.findByName(groupName).isDefined) {
      Group.add(Group(None, groupName))
      println("Group: " + regionName + " created.")
    }

    val group: Option[Group] = Group.findByName(groupName)


    PowerStation.update(PowerStation(station.id, station.name, station.powerUnits, station.downtimeCosts, station.components, group, region))
    println("Updated power station : " + station.name + " with region " + regionName + " and group " + groupName)
  }
}

