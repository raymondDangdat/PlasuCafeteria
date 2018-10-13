package comq.example.raymond.plasucafeteria;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import comq.example.raymond.plasucafeteria.Common.Common;
import comq.example.raymond.plasucafeteria.Database.Database;
import comq.example.raymond.plasucafeteria.ViewHolder.CartAdapter;
import comq.example.raymond.plasucafeteria.model.Order;
import comq.example.raymond.plasucafeteria.model.Request;

public class Cart extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    private FirebaseDatabase database;
    private DatabaseReference requests;

    private TextView txtTotalPrice;
    private Button btnPlace;

    private List<Order> cart = new ArrayList<>();
    private CartAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        //Firebase;
        database = FirebaseDatabase.getInstance();
        requests = database.getReference();

        //init
        recyclerView = findViewById(R.id.listCart);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


        txtTotalPrice = findViewById(R.id.total);
        btnPlace = findViewById(R.id.btnPlaceOrder);

        btnPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertDialog();

            }
        });

        loadListFood();
    }

    private void showAlertDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Cart.this);
        alertDialog.setTitle("One more step!");
        alertDialog.setMessage("Enter your address: ");

        final EditText editTextAddress = new EditText(Cart.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );

        editTextAddress.setLayoutParams(lp);
        alertDialog.setView(editTextAddress);// add edittext to alert dialog
        alertDialog.setIcon(R.drawable.ic_shopping_cart);

        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //create new request
                Request request = new Request(
                        Common.currentUser.getPhone(),
                        Common.currentUser.getName(),
                        editTextAddress.getText().toString(),
                        txtTotalPrice.getText().toString(),
                        cart
                );

                //submit to firebase
                //we will be using system.Currentmilli to key
                requests.child(String.valueOf(System.currentTimeMillis()))
                        .setValue(requests);

                //delete cart
                new Database(getBaseContext()).cleanToCart();

                Toast.makeText(Cart.this, "Thank you, order placed", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alertDialog.show();
    }

    private void loadListFood() {
        cart = new Database(this).getCarts();
        adapter = new CartAdapter(cart, this);
        recyclerView.setAdapter(adapter);

        //calculate  total price
        int total = 0;
        for (Order order:cart)
            total += (Integer.parseInt(order.getPrice()))*(Integer.parseInt(order.getQuantity()));
        Locale locale = new Locale("en", "US");
        NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);

        txtTotalPrice.setText(fmt.format(total));

    }
}
