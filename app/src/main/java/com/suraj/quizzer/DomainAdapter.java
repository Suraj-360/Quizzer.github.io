package com.suraj.quizzer;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class DomainAdapter extends RecyclerView.Adapter<DomainAdapter.ViewHolder> {

    ArrayList<DomainModel> arrayList;
    Context context;

    public DomainAdapter(ArrayList<DomainModel> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.domain_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DomainModel domainModel = arrayList.get(position);

        Glide.with(context).load(domainModel.getImgId()).into(holder.domainImage);
        if(holder.domainImage!=null)
        {
            holder.progressBar.setVisibility(View.GONE);
        }
        holder.domainName.setText(domainModel.getCategoryName());
        holder.itemView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                Intent startQuizIntent = new Intent(context, GetStartWithQuiz.class);
                startQuizIntent.putExtra("Domain ID",domainModel.getCategoryId());
                startQuizIntent.putExtra("Domain Name", domainModel.categoryName);
                startQuizIntent.putExtra("Domain Image", domainModel.imgId);
                context.startActivity(startQuizIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        ImageView domainImage;
        TextView domainName;
        ProgressBar progressBar;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            domainImage = itemView.findViewById(R.id.domainImage);
            domainName = itemView.findViewById(R.id.domainName);
            progressBar = itemView.findViewById(R.id.progressBar);
        }
    }
}
