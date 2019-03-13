// should have a default name argument
// can have a second level argument if not initialize to 1
class Player(val name: String, var level: Int = 1) {
   var score = 0
   var lives = 3

   val inventory = ArrayList<Loot>()

   override fun toString(): String {
      return """
         name: $name
         score: $score
         lives: $lives
         level: $level
      """
   }
}