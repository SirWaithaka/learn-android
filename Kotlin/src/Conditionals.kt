fun conditionals() {
   // conditionals in koltin

   val lives = 3

   // declare a boolean
   val isGameOver = (lives < 1)
   if (isGameOver) {
      println("Game Over")
   } else {
      println("Keep playing")
   }


   println("How old are you: ")
   val age = readLine()!!.toInt()
   val message: String
   message = when {
      (age < 18) -> "Youre too young to vote"
      (age == 100) -> "Congratulations"
      else -> "You can vote"
   }
//   message = if (age < 18) {
//      "Youre too young to vote"
//   } else if (age == 100) {
//      "Congratulations"
//   } else {
//      "You can vote"
//   }

   println(message)
}