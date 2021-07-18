package com.example.grabtutor.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.grabtutor.Fragment.PostDetailFragment;
import com.example.grabtutor.Model.Post;
import com.example.grabtutor.R;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> {
    private ArrayList<Post> list = new ArrayList<>();
    private Context mContext;

    public SearchAdapter(Context mContext, ArrayList<Post> locations) {
        this.mContext = mContext;
        this.list = locations;
    }
    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) { //ViewGroup is the parent class of all layout classes (ie RelativeLayout etc)
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.simple_post_item, parent, false);
        SearchViewHolder holder = new SearchViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull SearchAdapter.SearchViewHolder holder, int position) {
        holder.title.setText(list.get(position).getTitle());
        Picasso.get().load(list.get(position).getImageurl()).into(holder.postImage);
        holder.price.setText("$ " + list.get(position).getPrice());


        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String postId = list.get(position).getPostid();
                mContext.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit().putString("postid", postId).apply();

                ((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new PostDetailFragment()).addToBackStack(null).commit();

            }
        });

    }



    @Override
    public int getItemCount() {
        return list.size();
    }

    public void filterList(ArrayList<Post> filteredList){
        list = filteredList;
        notifyDataSetChanged();
    }



//    @Override
//    public Filter getFilter() {
//        return filter;
//    }
//
//    Filter filter = new Filter() {
//        private Object Collection;
//
//        @Override
//        protected FilterResults performFiltering(CharSequence constraint) {
//            List<Locations> filteredList = new ArrayList<>();
//
//            if(constraint.toString().isEmpty()){
//                filteredList.addAll(locationsAll);
//            }else{
//                for (Locations loc: locationsAll){
//                    if(loc.getName().toLowerCase().contains(constraint.toString().toLowerCase())){
//                        filteredList.add(loc);
//                    }
//                }
//            }
//
//            FilterResults filterRes = new FilterResults();
//            filterRes.values = filteredList;
//            return filterRes;
//        }
//
//        @Override
//        protected void publishResults(CharSequence constraint, FilterResults results) {
//            locations.clear();
//            locations.addAll((Collection<? extends Locations>)results.values);
//            notifyDataSetChanged();
//        }
//    };

    public class SearchViewHolder extends RecyclerView.ViewHolder{

        private TextView title, price;
        private ImageView postImage;
        private CardView parent;
        SearchViewHolder(@NonNull View itemView) {
            super(itemView);
            postImage = itemView.findViewById(R.id.post_image);
            title = itemView.findViewById(R.id.title); // need to call using itemView because we are not inside an Activity
            parent = itemView.findViewById(R.id.card);
            price = itemView.findViewById(R.id.price);



        }
    }
}
