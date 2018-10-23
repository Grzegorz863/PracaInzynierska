package pl.tcps.tcps.api_client.retrofit;

import android.content.Context;

import pl.tcps.tcps.R;
import pl.tcps.tcps.ssl.SelfSigningClientBuilder;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import android.content.res.Resources;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;


public class RetrofitBuilder {

    public static Retrofit createRetrofit(Context context){

        Resources res = context.getResources();

        Gson gson = new GsonBuilder()
                .setLenient()
                .registerTypeAdapter(ZonedDateTime.class, new JsonDeserializer<ZonedDateTime>() {
                    @Override
                    public ZonedDateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                       return ZonedDateTime.parse(json.getAsJsonPrimitive().getAsString());
                    }
                })
                .create();

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(res.getString(R.string.base_url))
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(SelfSigningClientBuilder.createClient(context));

        return builder.build();
    }
}
