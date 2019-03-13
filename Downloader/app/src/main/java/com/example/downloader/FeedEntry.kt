package com.example.downloader

class FeedEntry {
   var name: String = ""
   var artist: String = ""
   var releaseDate: String = ""
   var summary: String = ""
   var imageURL: String = ""

   override fun toString(): String {
      return """
         name = $name
         artists = $artist
         release date = $releaseDate
         image URL = $imageURL
      """.trimIndent()
   }
}