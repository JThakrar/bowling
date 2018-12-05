
import scala.collection.mutable

/**
  * A Frame encapsulates the frame number, the rolls for the frame and the cumulativeScore for the frame.
  * For frame 10 when a person gets 1 or 2 extra rolls for a spare or strike respectively, those too are accommodated in the toString.
  * The main functionality of this class is to support printing output for pinfalls.
  * It does not validate the rolls and/or the extrarolls for being valid values.
  *
  * Note that the cumulativeScore is the running total score until this frame.
  *
  * @param frameNumber
  * @param roll1
  * @param roll2
  * @param cumulativeScore
  * @param extraRoll1
  * @param extraRoll2
  */
case class Frame(frameNumber: Int,
                 roll1: String,
                 roll2: String,
                 cumulativeScore: Int,
                 extraRoll1: Option[String] = null,
                 extraRoll2: Option[String] = null) {

  val TAB = FileParser.TAB

  /**
    * printing of the pinfalls for the frame.
    * @return
    */
   def pinFalls: String = {
    val output = new mutable.StringBuilder()
    var count: Int = 0
    // roll1 can be "F" (foul ball) or 0 through 10
    roll1 match {
      // a 10 will result in the output being <empty string> + TAB + "X" for all but frame 10
      // for frame 10, a 10 will result in "X" + TAB + the extra rolls
      // Also note that this also has a short-circuit return
      case "10" =>
        if (frameNumber != 10)
          return output.append(" ").append(TAB).append("X").toString
        else
          return output.append("X").append(frame10Extra).toString
      case "F" => { // Foul ball = 0 pins
        count += 0
        output.append(roll1)
      }
      case _ => {
        count += roll1.toInt
        output.append(roll1)
      }
     }
    output.append(TAB)
    roll2 match {
//      case "10" => output.append("/")
      case "F" => output.append(roll2)
      case "" => output.append("")
      case _ => {
        count += roll2.toInt
        if (count == 10) {
          output.append("/")
          if (frameNumber == 10) {
            output.append(frame10Extra)
          }
        } else {
          output.append(roll2)
        }
      }
    }
    return output.toString
  }

  def frame10Extra: String = {
    val extraRollStr = new mutable.StringBuilder()
    val extra1 = extraRoll1.get
    extra1 match {
      case "10" => extraRollStr.append(TAB).append("X")
      case _ => extraRollStr.append(TAB).append(extra1)
    }
    if (roll1 == "10") {
      val extra2 = extraRoll2.get
      extra2 match {
        case "" => extraRollStr // Do nothing
        case "10" => extraRollStr.append(TAB).append("X")
        case _ => extraRollStr.append(TAB).append(extra2)
      }
    }
    return extraRollStr.toString
  }
}
