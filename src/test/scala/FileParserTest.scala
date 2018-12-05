import org.scalatest._

class FileParserTest extends FunSuite {

  val john_and_jeff =
    s"""Jeff	10
       |John	3
       |John	7
       |Jeff	7
       |Jeff	3
       |John	6
       |John	3
       |Jeff	9
       |Jeff	0
       |John	10
       |Jeff	10
       |John	8
       |John	1
       |Jeff	0
       |Jeff	8
       |John	10
       |Jeff	8
       |Jeff	2
       |John	10
       |Jeff	F
       |Jeff	6
       |John	9
       |John	0
       |Jeff	10
       |John	7
       |John	3
       |Jeff	10
       |John	4
       |John	4
       |Jeff	10
       |Jeff	8
       |Jeff	1
       |John	10
       |John	9
       |John	0""".stripMargin

  val jeff_output =
    s"""|Jeff
        |PinFalls	 	X	7	/	9	0	 	X	0	8	8	/	F	6	 	X	 	X	X	8	1
        |Score		20		39		48		66		74		84		90		120		148		167""".stripMargin

  val john_output =
    s"""John
       |PinFalls	3	/	6	3	 	X	8	1	 	X	 	X	9	0	7	/	4	4	X	9	0
       |Score		16		25		44		53		82		101		110		124		132		151""".stripMargin

  val carl =
    s"""Carl	10
       |Carl	10
       |Carl	10
       |Carl	10
       |Carl	10
       |Carl	10
       |Carl	10
       |Carl	10
       |Carl	10
       |Carl	10
       |Carl	10
       |Carl	10
       |Carl	10""".stripMargin

  val carl_output =
    s"""Carl
        |PinFalls	 	X	 	X	 	X	 	X	 	X	 	X	 	X	 	X	 	X	X	X	X
        |Score		30		60		90		120		150		180		210		240		270		300""".stripMargin

  // Note that since Player is such a small Class, not creating a separate unit test for it

  test("Test parsing of john_and_jeff") {
    val fileLines = john_and_jeff.split("\n")
    val players = FileParser.parseFile(fileLines.iterator)
    assert(players.size == 2)
    val playerRolls = FileParser.parsePlayerRolls(players)
    val jeff = playerRolls(0)
    val john = playerRolls(1)
    assert(jeff.frames.length == 10)
    assert(john.frames.length == 10)
    val jeffLastFrame = jeff.frames.last
    assert(jeffLastFrame.cumulativeScore == 167)
    val johnLastFrame = john.frames.last
    assert(johnLastFrame.cumulativeScore == 151)
    assert(jeff.toString == jeff_output)
    assert(john.toString == john_output)
  }

  test("Test parsing of carl") {
    val fileLines = carl.split("\n")
    val players = FileParser.parseFile(fileLines.iterator)
    assert(players.size == 1)
    val playerRolls = FileParser.parsePlayerRolls(players)
    val carlOnly = playerRolls.head
    assert(carlOnly.frames.length == 10)
    val carlLastFrame = carlOnly.frames.last
    assert(carlLastFrame.cumulativeScore == 300)
    assert(carlOnly.toString == carl_output)
  }

}