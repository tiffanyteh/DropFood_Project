package com.example.tiffany.dropfood_project;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class FoodList extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference foodList;
    RecyclerView recyler_food;
    RecyclerView.LayoutManager layoutManager2;
    String categoryId = "";
    FirebaseRecyclerAdapter<Food,FoodViewHolder> adapter;
    Database localDB;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_list);

        database = FirebaseDatabase.getInstance();
        foodList = database.getReference("Food");
        localDB = new Database(this);
        recyler_food = (RecyclerView)findViewById(R.id.recycler_food);
        recyler_food.setHasFixedSize(true);
        layoutManager2 = new LinearLayoutManager(this);
        recyler_food.setLayoutManager(layoutManager2);

        if(getIntent()!=null)
            categoryId = getIntent().getStringExtra("CategoryId");
        if(!categoryId.isEmpty() && categoryId!=null){
            loadListFood(categoryId);
        }

    }


    

    private void loadListFood(String categoryId) {
        adapter = new FirebaseRecyclerAdapter<Food, FoodViewHolder>(Food.class,R.layout.food_item,FoodViewHolder.class,foodList.orderByChild("MenuId").equalTo(categoryId)) {
            @Override
            protected void populateViewHolder(final FoodViewHolder viewHolder, final Food model, final int position) {
                viewHolder.food_name.setText(model.getName());
                Picasso.with(getBaseContext()).load(model.getImage()).into(viewHolder.food_image);

                if(localDB.isFavourite(adapter.getRef(position).getKey()))
                    viewHolder.fav_image.setImageResource(R.drawable.ic_baseline_favorite_24);

                    viewHolder.fav_image.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(!localDB.isFavourite(adapter.getRef(position).getKey())){
                                localDB.addToFavourites(adapter.getRef(position).getKey());
                                viewHolder.fav_image.setImageResource(R.drawable.ic_baseline_favorite_24);
                                Toast.makeText(FoodList.this, ""+model.getName()+" was added to Favourites", Toast.LENGTH_SHORT).show();
                            }else{
                                localDB.removeFromFavourites(adapter.getRef(position).getKey());
                                viewHolder.fav_image.setImageResource(R.drawable.ic_baseline_favorite_border_24);
                                Toast.makeText(FoodList.this, ""+model.getName()+" was removed from Favourites", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });


                final Food local = model;
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Intent foodDetail = new Intent(FoodList.this,FoodDetail.class);
                        foodDetail.putExtra("FoodId", adapter.getRef(position).getKey());
                        startActivity(foodDetail);
                    }
                });
            }
        };
        recyler_food.setAdapter(adapter);
    }
}
