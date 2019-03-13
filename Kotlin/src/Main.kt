fun main(args: Array<String>) {
   val player = Player("Kennedy")

//   println(player.name)
//   println(player.lives)
//   println(player.level)
//   println(player.score)

   val redPotion = Loot("Red Potion", LootType.POTION, 7.50)
   player.inventory.add(redPotion)
   println(player)
}