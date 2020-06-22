

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.*;
import java.util.*;

public class solrPOJO {

    public static void main(String[] args) {


        HashMap<String, String> sampleJson = new HashMap<String, String>();


        try{
            //파일 객체 생성
            File file = new File("/Users/imac/workspace_2020/solr-import-export-json/data/collection.json_31.json");

            //입력 스트림 생성
            FileReader filereader = new FileReader(file);
            //입력 버퍼 생성
            BufferedReader bufReader = new BufferedReader(filereader);
            String line = "";

            while((line = bufReader.readLine()) != null){

//                System.out.println(line);

                try {
                    JSONParser jsonParser = new JSONParser();
                    JSONObject jsonObj = (JSONObject) jsonParser.parse(line);

                    Map<String, Object> map = getMapFromJsonObject(jsonObj);

                    Set<String> set = map.keySet();

                    Iterator it = set.iterator();

                    while (it.hasNext()) {

                        String key = it.next().toString();

                        if(sampleJson.get(key) == null) {
                            sampleJson.put(key, map.get(key).toString());
                        }
                    }

                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }



            Set<String> keySet = sampleJson.keySet();

            Iterator it = keySet.iterator();

            while (it.hasNext()) {
                String key = it.next().toString();
                String value = sampleJson.get(key);
                key = "\"" + key + "\"";
                String newValue = "";

//                System.out.println(value);

                if (value.startsWith("[")) {
                    value = value.replace("[", "").replace("]", "");
                    String[] valueArray = value.split(",");


                    for (int i = 0; i < valueArray.length; i++) {
                        newValue += "\"" + valueArray[i] + "\"";

                        if (i != valueArray.length - 1) {
                            newValue += ",";
                        }
                    }

                    newValue = "[" + newValue + "]";
                } else {
                    newValue = "[\"" + value + "\"]";
                }



                System.out.print(key + ":" + newValue + ",");

            }



            bufReader.close();
        }catch (FileNotFoundException e) {
            // TODO: handle exception
        }catch(IOException e){
            System.out.println(e);
        }

    }


    public static Map<String, Object> getMapFromJsonObject(JSONObject jsonObj )
    {
        Map<String, Object> map = null;

        try {

            map = new ObjectMapper().readValue(jsonObj.toJSONString(), Map.class) ;

        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return map;
    }


}