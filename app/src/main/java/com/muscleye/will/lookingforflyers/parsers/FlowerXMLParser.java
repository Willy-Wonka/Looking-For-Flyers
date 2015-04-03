package com.muscleye.will.lookingforflyers.parsers;

import android.util.Log;

import com.muscleye.will.lookingforflyers.model.Flower;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Will on 15-04-02.
 */
public class FlowerXMLParser
{
    public static List<Flower> parseFeed(String content)
    {
        try
        {
            boolean inDataItemTag = false;
            String currentTagName = "";
            Flower flower = null;
            List<Flower> flowerList = new ArrayList<>();

            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(new StringReader(content));

            int eventType = parser.getEventType();

            while (eventType != XmlPullParser.END_DOCUMENT)
            {
//                Log.d("fxp", "eventType: " + eventType);
                switch (eventType)
                {
                    case XmlPullParser.START_TAG:
//                        Log.d("fxp", "eventType: 2 is start tag");
                        currentTagName = parser.getName();
//                        Log.d("fxp", "tag name: " + currentTagName);
                        if (currentTagName.equals("product"))
                        {
                            inDataItemTag = true;
                            flower = new Flower();
                            flowerList.add(flower);
//                            Log.d("fxp", "current=product & new flower is done!");
                        }
                        break;

                    case XmlPullParser.END_TAG:
//                        Log.d("fxp", "eventType: 3 is end tag=" + parser.getName());
                        if (parser.getName().equals("product"))
                        {
//                            Log.d("fxp", "parser.getName: " + parser.getName());
                            inDataItemTag = false;
//                            Log.d("fxp", "if getname=product & false is done!");
                        }
                        currentTagName = "";
//                        Log.d("fxp", "reset tag name!");
                        break;

                    case XmlPullParser.TEXT:
//                        Log.d("fxp", "eventType: 4 is text");
                        if (inDataItemTag && flower != null)
                        {
//                            Log.d("fxp", "Tag Name: " + currentTagName);
//                            Log.d("fxp", "Parser: " + parser.getText());
                            switch (currentTagName)
                            {
                                case "productId":
                                    flower.setProductId(Integer.parseInt(parser.getText()));
                                    break;
                                case "name":
                                    flower.setName(parser.getText());
                                    break;
                                case "instructions":
                                    flower.setInstruction(parser.getText());
                                    break;
                                case "category":
                                    flower.setCategory(parser.getText());
                                    break;
                                case "price":
                                    flower.setPrice(Double.parseDouble(parser.getText()));
                                    break;
                                case "photo":
                                    flower.setPhoto(parser.getText());
                                    break;
                                default:
                                    break;
                            }
                        }
                        break;
                }

                eventType = parser.next();
            }

//            Log.d("fxp", "NOT nullllllll");
            for (Flower flowers : flowerList)
            {
                Log.d("fxp", flowers.getName() + "\n" + flowers.getCategory() + "\n");
            }
            return flowerList;

        } catch (Exception e) {
            e.printStackTrace();

//            Log.d("fxp", "nullllllll");
            return null;
        }
    }
}