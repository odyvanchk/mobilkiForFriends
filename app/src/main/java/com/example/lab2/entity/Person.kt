package com.example.lab2.entity

data class Person( var id: String="",
                   var name: String="",
                   var age: Int=0,
                   var sex : String="",
                   var city: String="",
                   var country: String="",
                   var costPerHour: Double=0.0,
                   var languageLevel:String="",
                   var language : String="",
                   var pathImage : String="",
                   var pathVideo : String=""){

    fun toMap(): Map<String, Any> {
        return mapOf(
            "id" to id,
            "name" to name,
            "age" to age,
            "costPerHour" to costPerHour,
            "sex" to sex,
            "city" to city,
            "country" to country,
            "languageLevel" to languageLevel,
            "language" to language,
            "pathImage" to pathImage,
            "pathVideo" to pathVideo,

        )
    }
}

//
// class Person(){
//     var name: String=""
//     var age: Int=0
//     var sex : Sex=Sex.MALE
//     var city: String=""
//     var country: String=""
//     var costPerHour: Double=0.0
//     var languageLevel: LanguageLevel= LanguageLevel.A1
//     var language : Language=Language.ENGLISH
//     var pathImage : String=""
//     var pathVideo : String="";
//constructor(  name: String, age: Int, sex : String, city: String="", country: String, costPerHour: Double,
//              languageLevel: String, language : String, pathImage : String, pathVideo : String):this(){
//                  this.age = age;
//    this.city =city;
//    this.costPerHour = costPerHour;
//    this.country=country;
//    this.language = Language.valueOf(language);
//    this.languageLevel=LanguageLevel.valueOf(languageLevel);
//    this.name=name;
//    this.sex= Sex.valueOf(sex)
//    this.pathImage=pathImage;
//    this.pathVideo=pathVideo;
//
//}
//
//}


