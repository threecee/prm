package controllers.importers

import models.classification.{Group, PowerStation, Region}
import org.apache.poi.ss.usermodel.Row


object ImportPowerStations   extends ImportBase
  {


  def processRow(row: Row) {
    val powerstationName: String = row.getCell(0).getStringCellValue()
    val regionName: String = row.getCell(1).getStringCellValue()
    val groupName: String = row.getCell(2).getStringCellValue()

    if (!PowerStation.findByName(powerstationName).isDefined) {
      PowerStation.add(powerstationName)
    }

    val station: PowerStation = PowerStation.findByName(powerstationName).get

    if (regionName.length > 0 && !Region.findByName(regionName).isDefined) {
      Region.add(Region(None, regionName))
    }

    val region: Option[Region] = Region.findByName(regionName)


    if (groupName.length > 0 && !Group.findByName(groupName).isDefined) {
      Group.add(Group(None, groupName))
    }

    val group: Option[Group] = Group.findByName(groupName)


    PowerStation.update(PowerStation(station.id, station.name, station.powerUnits, station.downtimeCosts, station.components, group, region))
  }
}

