package com.example.yourbook;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.location.GnssAntennaInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.makeramen.roundedimageview.RoundedImageView;

public class MainAdapter extends FirebaseRecyclerAdapter<Book, MainAdapter.myVewiHolder> {
    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public MainAdapter(@NonNull FirebaseRecyclerOptions<Book> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myVewiHolder holder, int position, @NonNull Book model) {
        // display Book title in ViewHolder
        holder.bookName.setText(model.getName());
        // load book image via glide package and display it in view holder
        Glide.with(holder.img.getContext()).load(model.getCover()).placeholder(R.mipmap.ic_launcher).into(holder.img);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //when view holder is pressed pass this data to book details activity and start it.
                Intent i = new Intent(view.getContext(), BookDetails.class);
                i.putExtra("Name", model.getName());
                i.putExtra("url", model.getCover());
                i.putExtra("pdf", model.getPdf());
                i.putExtra("audio", model.getAudio());
                view.getContext().startActivity(i);
            }
        });
    }

    @NonNull
    @Override
    public myVewiHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // set view holder layout
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.book, parent, false);
        return new myVewiHolder(view);
    }

    class myVewiHolder extends RecyclerView.ViewHolder {
        RoundedImageView img;
        TextView bookName;
        public myVewiHolder(@NonNull View itemView) {
            super(itemView);
            //get view holder imageView by id
            img = (RoundedImageView) itemView.findViewById(R.id.bookImg);
            //get view holder TextView by id
            bookName = (TextView) itemView.findViewById(R.id.bookName);
        }

    }
}