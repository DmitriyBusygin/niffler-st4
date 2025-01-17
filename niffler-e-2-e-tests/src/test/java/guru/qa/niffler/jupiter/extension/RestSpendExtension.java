package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.api.spend.SpendApi;
import guru.qa.niffler.jupiter.extension.SpendExtension;
import guru.qa.niffler.model.SpendJson;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.io.IOException;

public class RestSpendExtension extends SpendExtension {

    private static final OkHttpClient HTTP_CLIENT = new OkHttpClient.Builder().build();
    private static final Retrofit RETROFIT = new Retrofit.Builder()
            .client(HTTP_CLIENT)
            .baseUrl("http://127.0.0.1:8093")
            .addConverterFactory(JacksonConverterFactory.create())
            .build();

    private final SpendApi spendApi = RETROFIT.create(SpendApi.class);

    @Override
    public SpendJson create(SpendJson spend) throws IOException {
        return spendApi.addSpend(spend).execute().body();
    }
}
