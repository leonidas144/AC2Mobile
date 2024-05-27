package com.example.ac2atividade5.adapter;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ac2atividade5.ActivityAluno;
import com.example.ac2atividade5.R;
import com.example.ac2atividade5.api.AlunoService;
import com.example.ac2atividade5.api.ApiClient;
import com.example.ac2atividade5.model.Aluno;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AlunoAdapter extends RecyclerView.Adapter<AlunoAdapter.AlunoHolder> {

    private final List<Aluno> Alunos;
    Context context;

    public AlunoAdapter(List<Aluno> Alunos, Context context) {
        this.Alunos = Alunos;
        this.context = context;
    }

    private void removerItem(int position) {
        String id = Alunos.get(position).getRa();
        AlunoService apiService = ApiClient.getUsuarioService();
        Call<Void> call = apiService.deleteAluno(Integer.parseInt(String.valueOf(id)));
        call.enqueue(new Callback<Void>() {

            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    // A solicitação foi bem-sucedida
                    Alunos.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, Alunos.size());
                    Toast.makeText(context, "Excluído com sucesso!", Toast.LENGTH_SHORT).show();
                } else {
                    // A solicitação falhou
                    Log.e("Exclusao", "Erro ao excluir");
                }
            }


            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                // Ocorreu um erro ao fazer a solicitação
                Log.e("Exclusao", "Erro ao excluir");
            }
        });
    }

    private void editarItem(int position) {
        String id = Alunos.get(position).getRa();
        Intent i = new Intent(context, ActivityAluno.class);
        i.putExtra("id",id);
        context.startActivity(i);
    }

    public static class AlunoHolder extends RecyclerView.ViewHolder {
        public TextView nome;
        public ImageButton btnEditar;
        public AlunoHolder(View itemView) {
            super(itemView);
            nome = (TextView) itemView.findViewById(R.id.txtNome);


        }
    }

    @Override
    public AlunoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new AlunoHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_item_aluno, parent, false));
    }

    @Override
    public void onBindViewHolder(AlunoHolder holder, int position) {
        holder.nome.setText(Alunos.get(position).getRa() + " - " + Alunos.get(position).getNome());
    }

    @Override
    public int getItemCount() {
        return Alunos != null ? Alunos.size() : 0;
    }


}