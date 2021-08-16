package com.inlog.ecommerce.model

import org.json.JSONArray

class RatingViewModel {
var create_date = ""
var description = ""
var name = ""
var avg_rating = 0.0
var delivery_rating = 0.0
var good_quality_rating = 0.0
var professional_rating = 0.0
var responsive_rating = 0.0
var value_for_money_rating = 0.0
var quality_rating = 0.0
var meet_expectation_rating = 0.0
    companion object{
        fun mapData(data : String):ArrayList<RatingViewModel>{
            val ratingList =  ArrayList<RatingViewModel>()
            var ratingJsonArray = JSONArray(data)
            for (i in 0 until ratingJsonArray.length()){
                val ratingJsonObj =ratingJsonArray.getJSONObject(i)
                val ratObj = RatingViewModel()
                ratObj.create_date = ratingJsonObj.optString("create_date")
                ratObj.description = ratingJsonObj.optString("description")
                ratObj.name = ratingJsonObj.optString("name")
                ratObj.avg_rating = ratingJsonObj.optDouble("avg_rating")
                ratObj.delivery_rating = ratingJsonObj.optDouble("delivery_rating")
                ratObj.good_quality_rating = ratingJsonObj.optDouble("good_quality_rating")
                ratObj.professional_rating = ratingJsonObj.optDouble("professional_rating")
                ratObj.responsive_rating = ratingJsonObj.optDouble("responsive_rating")
                ratObj.value_for_money_rating = ratingJsonObj.optDouble("value_for_money_rating")
                ratObj.quality_rating = ratingJsonObj.optDouble("quality_rating")
                ratObj.meet_expectation_rating = ratingJsonObj.optDouble("meet_expectation_rating")
                ratingList.add(ratObj)
            }

            return ratingList
        }
    }
}