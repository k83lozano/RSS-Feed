package com.example.rssapp

import android.util.Log
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.lang.Exception

class ParseApp {
    private val TAG = "ParseApp"
    val application = ArrayList<FeedEntry>()

    fun parse( xmlData: String ): Boolean {
        var status = true
        var tagInEntry = false
        var textValue = ""

        try{
            val factory = XmlPullParserFactory.newInstance()
            factory.isNamespaceAware = true
            val pullParser = factory.newPullParser()

            pullParser.setInput(xmlData.reader())
            var eventType = pullParser.eventType
            var currentRecord = FeedEntry()
            while (eventType != XmlPullParser.END_DOCUMENT){
                val tagName = pullParser.name?.lowercase()
                when (eventType){
                    XmlPullParser.START_TAG -> {
                        Log.d(TAG, "parse: Starting tag for: $tagName")
                        if(tagName == "entry") {
                            tagInEntry = true
                        }
                    }
                    XmlPullParser.TEXT -> {
                        textValue = pullParser.text
                    }
                    XmlPullParser.END_TAG -> {
                        Log.d(TAG, "parse: Ending tag for: $tagName")
                        if(tagInEntry){
                            when(tagName){
                                "entry" -> {
                                    application.add(currentRecord)
                                    tagInEntry = false
                                    currentRecord = FeedEntry()
                                }
                                "name" -> currentRecord.name = textValue
                                "artist" -> currentRecord.artist = textValue
                                "summary" -> currentRecord.summary = textValue
                                "releasedate" -> currentRecord.releaseDate = textValue
                                "image" -> currentRecord.imageUrl = textValue
                            }
                        }
                    }
                }
                eventType = pullParser.next()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            status = false
        }

        return true

    }
}