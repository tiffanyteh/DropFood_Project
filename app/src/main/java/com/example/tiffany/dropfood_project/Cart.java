package com.example.tiffany.dropfood_project;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class Cart extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference requests;

    TextView txtTotalPrice;
    Button btnPlace;

    List<Order> cart = new ArrayList<>();
    CartAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        database = FirebaseDatabase.getInstance();
        requests = database.getReference("Requests");

        recyclerView = (RecyclerView)findViewById(R.id.listCart);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        txtTotalPrice = (TextView)findViewById(R.id.total);
        btnPlace = (Button)findViewById(R.id.btnPlaceOrder);

        btnPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(cart.size()>0)
                    showAlertDialog();
                else
                    Toast.makeText(Cart.this, "Your cart is empty", Toast.LENGTH_SHORT).show();
            }
        });
        
        loadListFood();
    }

    private void showAlertDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Cart.this);
        alertDialog.setTitle("One More Step!");
        alertDialog.setMessage("Enter your Address: ");

        final EditText edtAddress = new EditText(Cart.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);

        edtAddress.setLayoutParams(lp);
        alertDialog.setView(edtAddress);
        alertDialog.setIcon(R.drawable.ic_shopping_cart_black_24dp);

        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Request request = new Request(Common.currentUser.getPhone(),
                        Common.currentUser.getName(), edtAddress.getText().toString(),txtTotalPrice.getText().toString(),
                        cart);

                requests.child(String.valueOf(System.currentTimeMillis())).setValue(request);
                new Database(getBaseContext()).cleanCart();
                Toast.makeText(Cart.this, "Thank You, Order Placed", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        alertDialog.show();
    }

    private void loadListFood() {

        cart = new Database(this).getCarts();
        adapter = new CartAdapter(cart,this);
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);

        double total = 0;
        for(Order order: cart)
            total += (Double.parseDouble(order.getPrice1()))*(Double.parseDouble(order.getQuantity()));

        txtTotalPrice.setText(String.valueOf(total));

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        if(item.getTitle().equals(Common.DELETE))
            deleteCart(item.getOrder());

        return true;
    }

    private void deleteCart(int position) {

        cart.remove(position);
        new Database(this).cleanCart();
        for(Order item:cart)
            new Database(this).addToCart(item);

        loadListFood();
    }
}