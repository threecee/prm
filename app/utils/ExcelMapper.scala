package utils





class ExcelMapper(sheet:ExcelSheet) {

   val excelSheet:ExcelSheet = sheet



  def getCell(row: Int, column: Int): Option[ExcelCell] = {
    if (excelSheet.fastCells != null) {
      return Option(excelSheet.fastCells(row)(column))
    }
    return None
  }



  def getCellAsString(row: Int, column: Int): Option[String] = {
    val cell: Option[ExcelCell] = getCell(row, column)
    if (!cell.isDefined) {
      return None
    }

    return Option(cell.get.value.toString())
  }


  def getCellAsInt(row: Int, column: Int): Option[Int] = {
    val cell: Option[Double] = getCellAsDouble(row, column)
    if (!cell.isDefined) {
      return None
    }

    return Option(cell.get.toInt)
  }

  def getCellAsDouble(row: Int, column: Int): Option[Double] = {
    val cell: Option[ExcelCell] = getCell(row, column)
    if (cell.isDefined && cell.get.`type` == xssfDataType.NUMBER) {
      return Option(cell.get.value.asInstanceOf[Double])
    }
    return None
  }
}
