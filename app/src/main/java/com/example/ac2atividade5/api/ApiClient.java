package com.example.ac2atividade5.api;


import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
public class ApiClient {
    private static final String BASE_URL = "https://664291f53d66a67b3437a949.mockapi.io/";
    private static Retrofit retrofit = null;
    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
    public static AlunoService getUsuarioService() {
        return getClient().create(AlunoService.class);
    }
}
