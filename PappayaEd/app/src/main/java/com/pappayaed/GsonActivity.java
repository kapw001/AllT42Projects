package com.pappayaed;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import com.pappayaed.Model.Obj;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import static com.pappayaed.R.string.source;

public class GsonActivity extends AppCompatActivity {


    private static final String TAG = "GsonActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gson);


        String json = "  {\n" +
                "  \t\"k\": {\n" +
                "\n" +
                "  \t\t\"name\": \"Test\"\n" +
                "\n" +
                "\n" +
                "  \t},\n" +
                "\n" +
                "  \t\"k2\": {\n" +
                "\n" +
                "  \t\t\"name\": \"Hello\"\n" +
                "\n" +
                "\n" +
                "  \t}\n" +
                "\n" +
                "\n" +
                "  }";


        json = "{\n" +
                "  \"test\":{\n" +
                "  \n" +
                "    \"k\": {\n" +
                "      \"name\": \"HHhhh\",\n" +
                "      \"id\":123\n" +
                "    },\n" +
                "  \n" +
                "   \"k1\": {\n" +
                "      \"name\": \"dsdssd\",\n" +
                "     \"id\":125\n" +
                "    }\n" +
                "  }\n" +
                "    \n" +
                "}";


        Re test = new Gson().fromJson(json, Re.class);
        Log.e(TAG, "onCreate: test  " + test.test.toString());

        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONObject jsonObject1 = jsonObject.optJSONObject("test");
//            JSONObject obj = jsonObject1;

            Iterator iterator = jsonObject1.keys();

            while (iterator.hasNext()) {

                JSONObject jsonObject2 = jsonObject1.getJSONObject(iterator.next().toString());

                Log.e(TAG, "onCreate: " + jsonObject2.getString("name") + "    " + jsonObject2.getString("id"));
            }

//            Iterator iterator = i.entrySet().iterator();
//
//
//            while (iterator.hasNext()) {
//
//                Log.e(TAG, "onCreate: " + iterator.next().toString());
//            }


        } catch (JSONException e) {
            e.printStackTrace();
        }


//        Gson gson = new Gson();
//        GsonBuilder builder = new GsonBuilder();
//
//
//        builder.registerTypeAdapter(ResultTest.class, new CheckInterfaceDeserializer());
//        Gson gson = builder.create();
//
//        ResultTest[] resultTest = gson.fromJson(json, ResultTest[].class);
//

//        Log.e(TAG, "onCreate: " + (double) resultTest.getAnyType());

        GsonBuilder gsonBuilder = new GsonBuilder();
//        gsonBuilder.registerTypeAdapter(TT.class, new NaturalDeserializer());
        Gson gson = gsonBuilder.create();
//
        TT natural = gson.fromJson(json, TT.class);

        Log.e(TAG, "onCreate: TT " + natural.test.size());
//
//
        for (Map.Entry<Object, ResultTest> entry : natural.test.entrySet()
                ) {

            Log.e(TAG, "onCreate: " + entry.getKey() + "   " + entry.getValue().getAnyType().toString() + "  " + entry.getValue().getId());

        }

//        Type mapType = new TypeToken<Map<Object, ResultTest>>() {
//        }.getType(); // define generic type
//        Map<Object, ResultTest> result = gson.fromJson(json, mapType);
//
//        for (Map.Entry<Object, ResultTest> entry : result.entrySet()
//                ) {
//
//            Log.e(TAG, "onCreate: " + entry.getKey() + "   " + entry.getValue().toString());
//
//        }


    }
}


class TT {


    @SerializedName("test")
    Map<Object, ResultTest> test;


}

class Re {

    @SerializedName("test")
    Object test;
}

class Test {

    Object object;

    @Override
    public String toString() {
        return "Test{" +
                "object=" + object +
                '}';
    }
//    Map<String, Object> mapMy;
}


class NaturalDeserializer implements JsonDeserializer<Object> {

    private static final String TAG = "NaturalDeserializer";

    public Object deserialize(JsonElement json, Type typeOfT,
                              JsonDeserializationContext context) {
        if (json.isJsonNull()) return null;
        else if (json.isJsonPrimitive()) return handlePrimitive(json.getAsJsonPrimitive());
        else if (json.isJsonArray()) return handleArray(json.getAsJsonArray(), context);
        else return handleObject(json.getAsJsonObject(), context);


    }

    private Object handlePrimitive(JsonPrimitive json) {

        Log.e(TAG, "handlePrimitive: " + json);
        if (json.isBoolean())
            return json.getAsBoolean();
        else if (json.isString())
            return json.getAsString();
        else {
            BigDecimal bigDec = json.getAsBigDecimal();
            // Find out if it is an int type
            try {
                bigDec.toBigIntegerExact();
                try {
                    return bigDec.intValueExact();
                } catch (ArithmeticException e) {
                }
                return bigDec.longValue();
            } catch (ArithmeticException e) {
            }
            // Just return it as a double
            return bigDec.doubleValue();
        }
    }

    private Object handleArray(JsonArray json, JsonDeserializationContext context) {
        Object[] array = new Object[json.size()];
        for (int i = 0; i < array.length; i++)
            array[i] = context.deserialize(json.get(i), Object.class);
        return array;
    }

    private Object handleObject(JsonObject json, JsonDeserializationContext context) {

        Log.e(TAG, "handleObject:1 " + json.size());

        if (json.size() > 1) {
            Map<String, Object> map = new HashMap<String, Object>();
            for (Map.Entry<String, JsonElement> entry : json.entrySet())
                map.put(entry.getKey(), context.deserialize(entry.getValue(), Object.class));
            return map;
        }

        return json.get("test");

    }
}


class CheckInterfaceDeserializer implements JsonDeserializer<ResultTest> {
    private static final String TAG = "CheckInterfaceDeseriali";

    @Override
    public ResultTest deserialize(JsonElement json, Type typeOfT,
                                  JsonDeserializationContext context) throws JsonParseException {
        JsonObject jObject = (JsonObject) json;

        Log.e(TAG, "deserialize: " + jObject.toString());


//        JsonElement typeObj = jObject.get("TypeName");

//        if(typeObj!= null ){
//            String typeVal = typeObj.getAsString();
//
//            switch (typeVal){
//                case "CheckSpecificDday":
//                    return context.deserialize(json, CheckSpecificDday.class);
//                case "CheckEveryDayDday":
//                    return context.deserialize(json, CheckEveryDayDday.class);
//                case "CheckEveryDdayOfWeek":
//                    return context.deserialize(json, CheckEveryDdayOfWeek.class);
//                case "CheckEveryMonthSpecificDday":
//                    return context.deserialize(json, CheckEveryMonthSpecificDday.class);
//                case "CheckEveryYearWeek":
//                    return context.deserialize(json, CheckEveryYearWeek.class);
//            }
//        }

        return null;
    }
}


class T {


}

class ResultTest {


//    Map<Object, Object> objectMap;


    @SerializedName("name")
    private Object anyType;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @SerializedName("id")
    private long id;

    public Object getAnyType() {
        return anyType;
    }

    public void setAnyType(Object anyType) {
        this.anyType = anyType;
    }
}