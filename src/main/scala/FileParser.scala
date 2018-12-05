import java.io.File

import scala.collection.mutable


object FileParser {

  val TAB = "\t"
  val VALID_PINFALLS = Set("0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "F")

  def main(args: Array[String]): Unit = {

    def helpMsg: String = {
      s"""
         |Usage: FileParser <file>
    """.stripMargin
    }

    if (args.isEmpty) {
      println(helpMsg)
      System.exit(0)
    }

    val file = new File(args(0))
    if (!file.exists()) {
      println(s"Cannot find file ${file}. Cannot continue....")
      System.exit(1)
    }
    val fileLines = scala.io.Source.fromFile(file).getLines()
//    val fileLines = scala.io.Source.fromFile(file).getLines()
    val playerRolls = parseFile(fileLines)
//    playerRolls.foreach(println)
    val players = parsePlayerRolls(playerRolls)
    val header = (1 to 10).mkString(s"Frame${TAB}${TAB}", s"${TAB}${TAB}", "")
    println(header)
    players.foreach(println)
  }

  /**
    * Read a file containing pinfalls at each roll and return a map of player to the rolls for the 10 frames.
    * It is assumed that the rolls truly correspond to 10 frames and no validation is done to check that.
    * @param fileContents
    * @return
    */
  def parseFile(fileContents: Iterator[String]): Map[String, Seq[String]] = {
    val playerRolls = mutable.HashMap[String, mutable.ArrayBuffer[String]]()
    for (line <- fileContents) {
      val split = line.split(TAB)
      assert(split.length == 2, s"Invalid line encountered. Line should contain Name<TAB>PinFalls. Found = (${line})")
      val name = split(0).trim
      val pinFalls = split(1).trim
      // Basic data validation
      assert(VALID_PINFALLS.contains(pinFalls), s"Invalid pin-falls value in line ($line)")
      if (!playerRolls.contains(name)) {
        playerRolls(name) = mutable.ArrayBuffer[String]()
      }
      playerRolls(name).append(pinFalls)
    }
    playerRolls.toMap
  }

  def roll2Score(roll: String): Int = {
    roll match {
      case "F" => 0
      case _ => roll.toInt
    }
  }

  /**
    * Given the rolls corresponding to 10 frames for a set of players, convert that into a sequence of Player data.
    * Player data is essentially the mapping from player name to a sequence of 10 frames for that player.
    * @param playerRolls
    * @return
    */
  def parsePlayerRolls(playerRolls: Map[String, Seq[String]]): Seq[Player] = {
    val players = mutable.ArrayBuffer[Player]()
    for (playerInfo <- playerRolls) {
      val player = playerInfo._1
      val rolls = playerInfo._2
//      println(s"Working on player ${player} with rolls ${rolls}")
      val frames = mutable.ArrayBuffer[Frame]()
      var index = 0
      var score = 0
      for (frameNum <- 1 to 10) {
//        println(s"Starting on frame ${frameNum} at index ${index} and score = ${score}")
        val roll1 = rolls(index)
        var frameTotal = 0
//        println(s"Got roll1 = ${roll1}")
        roll1 match {
          case "10" => { // Strike
//            println("Got X (10 on roll1 of frame).....")
            frameTotal += roll2Score(roll1) + roll2Score(rolls(index + 1)) + roll2Score(rolls(index + 2))
            score += frameTotal
            val frame =
              if (frameNum != 10) {
//                println("Not frame 10")
              Frame(frameNum, roll1, "", score)
            } else {
//                println("Frame 10")
              Frame(frameNum, roll1, "", score, Some(rolls(index + 1)), Some(rolls(index + 2)))
            }
//            println(s"Appending frame # ${frameNum}")
            frames.append(frame)
          }
          case _ => {
            frameTotal += roll2Score(roll1)
            index += 1
            val roll2 = rolls(index)
//            println(s"Got roll2 = ${roll2}")
            frameTotal += roll2Score(roll2)
            if (frameTotal == 10) { // Spare
//              println(s"Got roll1 + roll2 = 10 (roll1 = ${roll1}, roll2 = ${roll2})")
              frameTotal += roll2Score(rolls(index + 1))
              score += frameTotal
              val frame =
                if (frameNum != 10) {
                  Frame(frameNum, roll1, roll2, score)
                } else {
                  Frame(frameNum, roll1, roll2, score, Some(rolls(index + 1)), Some(""))
                }
//              println(s"Appending frame # ${frameNum}")
              frames.append(frame)
            } else {
              score += frameTotal
              val frame = Frame(frameNum, roll1, roll2, score)
              frames.append(frame)
            }
          }
        }
        index += 1
//        println(s"Total for frame ${frameNum} = ${frameTotal}. Running score = ${score}. Next index = ${index}")
      }
//      println(s"${player} has ${frames.length} frames")
      players.append(Player(player, frames))
    }
    players
  }

}
