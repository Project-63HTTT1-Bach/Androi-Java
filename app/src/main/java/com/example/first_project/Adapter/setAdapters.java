package com.example.first_project.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.first_project.Active.QuestionActivity;
import com.example.first_project.Model.QuestionModel;
import com.example.first_project.Model.setModel;
import com.example.first_project.R;
import com.example.first_project.databinding.ItemSetBinding;

import java.util.ArrayList;

public class setAdapters extends RecyclerView.Adapter<setAdapters.viewModel> {

    Context context ;
    ArrayList<setModel> list;

    public setAdapters(Context context, ArrayList<setModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public viewModel onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view  = LayoutInflater.from(context).inflate(R.layout.item_set,parent,false);

        return new viewModel(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewModel holder, int position) {
        final  setModel model = list.get(position);

        holder.binding.setName.setText(model.getSetName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, QuestionActivity.class);
                intent.putExtra("set",model.getSetName());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewModel extends RecyclerView.ViewHolder{

        ItemSetBinding  binding;
        public viewModel(@NonNull View itemView) {
                super(itemView);

                binding  = ItemSetBinding.bind(itemView);
            }
        }

}
