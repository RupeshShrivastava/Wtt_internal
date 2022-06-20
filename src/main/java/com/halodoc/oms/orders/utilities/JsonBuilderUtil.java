package com.halodoc.oms.orders.utilities;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JsonBuilderUtil {

    private  static Gson gson = new Gson();
    private  static final String CLONE_NODE = "RepeatNode";

    /**
     * This function takes SingleJsonTemplate, and Map Values as input - generates the Output Json Value
     * Input:
     * String jsonTemplate name, Placed in location "a2hotelsautomation\dataResource\JsonTemplate"
     * map which has entity values corresponding
     * Output:
     * Generates the Output Json Value, and places it is "a2hotelsautomation\dataResource\JsonTemplateValue
     *
     * @param template : JsonTemplate
     * @param map        : Values is Map format
     */
    public static String generateDataFromMap(String folder,String template,HashMap<String,Object> map) {
        String valueJson=null;
        HashMap<String,Object> updatedMap=new HashMap<>();
        HashMap<String,Object> tempMap=new HashMap<>();
        String filePath = System.getProperty("user.dir")+"/src/main/resources/jsonFiles/" +folder+"/"+template+".json";
        File json_templateFile = new File(filePath);
        if(json_templateFile.exists()) {
            String dataTemplateFileContents=readFileToString(System.getProperty("user.dir")+"/src/main/resources/jsonFiles/" +folder+"/"+template+".json");

            //updated template and map
            tempMap =getJsonTemplateArray(dataTemplateFileContents,map);
            dataTemplateFileContents = modifyJsonTemplate(dataTemplateFileContents,tempMap);
            updatedMap=getUpdatedMap(map,tempMap);

            String dataTemplateString = dataTemplateFileContents;
            dataTemplateString = updateTemplateForEachVariable(updatedMap, dataTemplateString);
            valueJson=dataTemplateString;
            //log.info("Generated Value File is :"+dataTemplateString);
            template.split("");
            //create folder
            //createfolder(System.getProperty("user.dir")+"/resources/jsonFiles/JsonTemplateValue/",folder);
            //writeStringToFile(System.getProperty("user.dir")+"/dataResource/JsonTemplateValue/"+folder+template+".json",dataTemplateString);

        }
        else
        {
            logErrorMessage("importDataForEachEntity", "cannot find the template file: "
                    + " as mentioned in the Json Map file: "+ updatedMap);
        }
        return valueJson;
    }

    /**
     * This function takes MultipleJsonTemplate, and Map Values as input - generates the Output Json Value Files for corresponding templates
     * Input:
     * ArrayList<String> templateLists, Placed in location "a2hotelsautomation\dataResource\JsonTemplate"
     * map which has entity values corresponding
     * Output:
     * Generates the Output ArrayList<String> JsonValue, and places it is "a2hotelsautomation\dataResource\JsonTemplateValue
     *
     * @param templateLists : List of JsonTemplates
     * @param map        : Values is Map format
     *
     *
     */
    public static ArrayList<String> generateDataFromMap(String folder,ArrayList<String> templateLists,HashMap<String,Object> map)
    {
        checkForJsonTemplateValueDirectory("JsonTemplateValue");
        ArrayList<String> valueJson = new ArrayList<String>();
        for (String templateList : templateLists) {
            HashMap<String,Object> updatedMap=new HashMap<>();
            HashMap<String,Object> tempMap=new HashMap<>();
            String filePath = System.getProperty("user.dir")+"/dataResource/JsonTemplate/" +folder+templateList+".json";
            File json_templateFile = new File(filePath);
            if(json_templateFile.exists()) {
                String dataTemplateFileContents=readFileToString(System.getProperty("user.dir") + "/dataResource/JsonTemplate/" + folder+templateList+".json");

                //updated template and map
                tempMap =getJsonTemplateArray(dataTemplateFileContents,map);
                dataTemplateFileContents = modifyJsonTemplate(dataTemplateFileContents,tempMap);
                updatedMap=getUpdatedMap(map,tempMap);

                String dataTemplateString = dataTemplateFileContents;
                dataTemplateString = updateTemplateForEachVariable(updatedMap, dataTemplateString);
                valueJson.add(dataTemplateString);
                //log.info("Generated Value File is :"+dataTemplateString);
                //create folder
                createfolder(System.getProperty("user.dir")+"/dataResource/JsonTemplateValue/",folder);
                writeStringToFile(System.getProperty("user.dir")+"/dataResource/JsonTemplateValue/"+folder+templateList+".json",dataTemplateString);
            }
            else
            {
                logErrorMessage("importDataForEachEntity", "cannot find the template file: "
                        + " as mentioned in the Json Map file: "+ updatedMap);
            }
        }
        return valueJson;
    }


    public static HashMap<String,Object> getJsonTemplateArray(String jsonGiven,HashMap<String,Object> keyMap)
    {
        Map<String, Object> jsonMap = convertJsonToMap(jsonGiven);
        HashMap<String,Object> map=new HashMap<>();
        //get all the ArrayList, and its count to be repeat into map
        map=extractArrayLists(jsonMap,new HashMap<String,Object>(),keyMap);
        return map;
    }


    public static String modifyJsonTemplate(String jsonGiven,HashMap<String,Object> map)
    {

        //updatedJson has the updated json, with the required number of ArrayList
        Map<String, Object> mapData = parseJSONObjectToMap(new JSONObject(jsonGiven),map);
        mapData = updateTheMapData(mapData,map);
        JSONObject updatedJson=new JSONObject(mapData);

        return updatedJson.toString();
    }

    private static  Map<String, Object> updateTheMapData( Map<String, Object> mapData,HashMap<String,Object> map) {
        for(Map.Entry<String, Object> entry : mapData.entrySet()) {
            String key = entry.getKey();
            // System.out.println();
            Object value = entry.getValue();
            if(value instanceof Map) {
                updateTheMapData((Map<String, Object>)value,map);
            }else if(map.containsKey(key)){

                List jsonArray = (List)value;
                Object jsonObjValue = jsonArray.get(0);
                HashMap jsonObjMap = null;
                String jsonObjStr = null;
                if(jsonObjValue instanceof String) {
                    jsonObjStr = jsonObjValue.toString();
                }
                if(jsonObjValue instanceof HashMap) {
                    jsonObjMap = (HashMap) jsonArray.get(0);
                }
                try {
                    long numberOfTimesToRepeat =((Number) map.get(key)).longValue();
                    while(numberOfTimesToRepeat > 1) {
                        if(jsonObjMap != null)jsonArray.add(jsonObjMap.clone());
                        if(jsonObjStr != null)jsonArray.add(jsonObjStr);
                        numberOfTimesToRepeat --;
                    }
                    if(numberOfTimesToRepeat==0)
                    {
                        if(jsonObjMap != null)jsonArray.remove(jsonObjMap.clone());
                        if(jsonObjStr != null)jsonArray.remove(jsonObjStr);
                    }
                }catch(Exception e) {
                    /*System.out.println("This is not a number. Below is the exception. it wont halt the json formation");
                    System.out.println(e.getMessage());*/
                }

                if(jsonObjValue instanceof HashMap) {
                    updateTheMapData((Map<String, Object>)jsonObjValue,map);
                }
            }
        }
        return mapData;
    }

    public static Map<String,Object> parseJSONObjectToMap(JSONObject jsonObject,HashMap<String,Object> map) throws JSONException{
        Map<String, Object> mapData = new HashMap<String, Object>();
        Iterator<String> keysItr = jsonObject.keys();

        while(keysItr.hasNext()) {
            String key = keysItr.next();
            Object value = jsonObject.get(key);

            if(value instanceof JSONArray) {
                if(map.containsKey(key)) {
                    //System.out.println(key +" - "+map.get(key));
                }
                value = parseJSONArrayToList((JSONArray) value,map);
            }else if(value instanceof JSONObject) {
                value = parseJSONObjectToMap((JSONObject) value,map);
            }
            mapData.put(key, value);
        }
        // System.out.println(mapData);
        return mapData;
    }

    public static List<Object> parseJSONArrayToList(JSONArray array,HashMap<String,Object> map) throws JSONException {
        List<Object> list = new ArrayList<Object>();
        for(int i = 0; i < array.length(); i++) {
            Object value = array.get(i);
            if(value instanceof JSONArray) {
                value = parseJSONArrayToList((JSONArray) value,map);
            }else if(value instanceof JSONObject) {
                value = parseJSONObjectToMap((JSONObject) value,map);
            }
            list.add(value);
        }
        return list;
    }


    public static HashMap<String, Object> extractArrayLists(Object jsonResponse, HashMap<String, Object> keys, HashMap<String,Object> map)
            throws JSONException {

        if (jsonResponse instanceof Map) {
            Map<String, Object> jsonResponseMap = (Map<String, Object>) jsonResponse;
            for (String key : jsonResponseMap.keySet()) {
                Object value = jsonResponseMap.get(key);
                if ( value instanceof List)
                {
                    keys.put(key,map.get(key));
                    extractArrayLists(value,keys,map);
                }
                else if(value instanceof Map)
                {
                    extractArrayLists(value,keys,map);
                }
            }
        } else if (jsonResponse instanceof List) {
            List<Object> values = (List) jsonResponse;
            for (Object value : values) {
                extractArrayLists(value,keys,map);
            }
        }
        return keys;
    }


    public static Map<String, Object> convertJsonToMap(String dataTemplateFileContents)
    {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> jsonMap = new HashMap<>();
        try {
            // convert JSON string to Map
            jsonMap = mapper.readValue(dataTemplateFileContents, Map.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonMap;
    }

    private static void checkForJsonTemplateValueDirectory(String directoryName)
    {
        File directory = new File(System.getProperty("user.dir")+"/dataResource/"+directoryName);
        if (! directory.exists()){
            directory.mkdir();
        }
    }

    private static String updateTemplateForEachVariable(HashMap<String, Object> entityValuesMap1,
                                                        String dataTemplateString){
        TreeMap<String, Object>entityValuesMap=sortMapAsPerKeyLength(entityValuesMap1);
        for (String entityValuesMapKey : entityValuesMap.keySet()) {
            Object entityValuesMapValue = entityValuesMap.get(entityValuesMapKey);
            String dollarEscapedEntityValuesMapKey = new String("\\" + "$"+entityValuesMapKey);
            if (entityValuesMapValue == null) {
                dataTemplateString = dataTemplateString.replaceAll("\\\"" + dollarEscapedEntityValuesMapKey + "\\\"", "null");
            } else {
                ObjectParameters valueDO = null;
                try {
                    valueDO = gson.fromJson(gson.toJson(entityValuesMapValue), ObjectParameters.class);
                } catch (JsonSyntaxException e) {
                    if(entityValuesMapValue instanceof Integer)
                    {
                        Integer value=(Integer)entityValuesMapValue;
                        String removeEscapedEntityValuesMapKey="\""+dollarEscapedEntityValuesMapKey.replace("\\","")+"\"";
                        dataTemplateString = dataTemplateString.replace(removeEscapedEntityValuesMapKey, value.toString());
                    }
                    else if(entityValuesMapValue instanceof Float)
                    {
                        Float value=(Float) entityValuesMapValue;
                        String removeEscapedEntityValuesMapKey="\""+dollarEscapedEntityValuesMapKey.replace("\\","")+"\"";
                        dataTemplateString = dataTemplateString.replace(removeEscapedEntityValuesMapKey, value.toString());
                    }
                    else
                    {
                        entityValuesMapValue = returnEscapedDollarEntity(entityValuesMapValue.toString());
                        dataTemplateString = dataTemplateString.replaceAll(dollarEscapedEntityValuesMapKey, entityValuesMapValue.toString());
                    }
                }
            }
        }
        return dataTemplateString;
    }

    public static void createfolder(String path, String folderName)
    {
        File file = new File(path+folderName);
        if (!file.exists()) {
            if (file.mkdir()) {
                //"Directory is created!"
            } else {
                //"Failed to create directory!"
            }
        }
    }


    public static TreeMap<String, Object> sortMapAsPerKeyLength(HashMap<String, Object> entityValuesMap)
    {
        TreeMap<String,Object> treeMap = new TreeMap<String, Object>(
                new Comparator<String>() {
                    @Override
                    public int compare(String s1, String s2) {
                        return s1.length() == s2.length() ? s1.compareTo(s2) : s2.length() - s1.length();
                    }
                }
        );
        treeMap.putAll(entityValuesMap);
        return treeMap;
    }

    public static HashMap<String,Object> getUpdatedMap(HashMap<String,Object> keyMap,HashMap<String,Object> map)
    {
        //remove the ArrayLists from the keyMap
        for (Map.Entry<String,Object> entry : map.entrySet())
            keyMap.remove(entry.getKey());
        return keyMap;
    }

    public static String readFileToString(String importConfigFile) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            InputStream is = new FileInputStream(importConfigFile);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
            String line = bufferedReader.readLine();
            while (line != null) {
                stringBuilder.append(line).append("\n");
                line = bufferedReader.readLine();
            }
            return stringBuilder.toString();
        } catch (Exception e) {
           // log.error("ERROR WHILE Loading Import Config File + " + e);
        }
        return stringBuilder.toString();
    }

        public static void writeStringToFile(String fileName, String fileContents) {
        try {
            Writer writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(fileName), StandardCharsets.UTF_8));
            writer.write(fileContents);
            writer.close();
        } catch (UnsupportedEncodingException | FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

        public static void logErrorMessage(String methodName, String message) {
        //log.error("Error in method: {} with error message: {}",methodName,message);
    }

     public static Object returnEscapedDollarEntity(Object stringValue) {
        if(stringValue.toString().contains("$") && !stringValue.toString().contains("\\$")) {
            stringValue = stringValue.toString().replaceAll("\\$", "\\\\\\$");
        }
        return stringValue;
    }

}