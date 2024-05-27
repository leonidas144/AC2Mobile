package com.example.ac2atividade5.api;


import com.example.ac2atividade5.model.Aluno;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface AlunoService {
    @GET("aluno")
    Call<List<Aluno>> getAlunos();
    @POST("aluno")
    Call<Aluno> postAluno(@Body Aluno aluno);
    @DELETE("aluno/{id}")
    Call<Void> deleteAluno(@Path("id") int raAluno);
    @GET("aluno/{id}")
    Call<Aluno> getAlunoPorId(@Path("id") String raAluno);
    @PUT("aluno/{id}")
    Call<Aluno> putAluno(@Path("id") String idUsuario, @Body Aluno aluno);
}
