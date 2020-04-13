package com.example.foodtask;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.target.ViewTarget;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    ProgressBar mainProgress, progressBar;
    LinearLayout mainLL;
    TextView itemName, itemDes, tvTotalPrice;
    Spinner foodSpinner, foodItemSpinner;
    ImageView ivItem;
    EditText etQty;
    RadioGroup radioGroup;
    RadioButton pick, delivery;
    Button btnBuy;
    double totalPrice=0;
    //list
    List<String> foodCategories = new ArrayList<String>();
    List<String> foodName= new ArrayList<String>();
    List<MainModel> mainModels = new ArrayList<>();
    List<InnerModel> innerModels = new ArrayList<>();
    int dineIn =1,qty=1,itemPrice=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        getDataFromApi("https://api.jsonbin.io/b/5e930030f6e7a342d9636352");
        clicks();
    }

    private void init() {
        mainProgress = findViewById(R.id.mainProgress);
        progressBar = findViewById(R.id.progressBar);
        mainLL = findViewById(R.id.mainLL);
        itemName = findViewById(R.id.itemName);
        itemDes = findViewById(R.id.itemDes);
        tvTotalPrice = findViewById(R.id.tvTotalPrice);
        foodSpinner = findViewById(R.id.foodSpinner);
        foodItemSpinner = findViewById(R.id.foodItemSpinner);
        ivItem = findViewById(R.id.ivItem);
        etQty = findViewById(R.id.etQty);
        radioGroup = findViewById(R.id.radioGroup);
        pick = findViewById(R.id.pick);
        delivery = findViewById(R.id.delivery);
        btnBuy = findViewById(R.id.btnBuy);
    }

    private void clicks() {
        btnBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                foodItemSpinner.setSelection(0);
                Toast.makeText(MainActivity.this, "Order successfully placed Please wait..!", Toast.LENGTH_LONG).show();
            }
        });
        foodSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i==0){
                    Toast.makeText(MainActivity.this, "Please Select Cuisines", Toast.LENGTH_SHORT).show();
                }else{
                    try {
                        loadNewData(mainModels.get(i-1).getArray());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        foodItemSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i==0){
                    mainLL.setVisibility(View.GONE);
                }else{

                    mainLL.setVisibility(View.VISIBLE);
                    itemName.setText(innerModels.get(i-1).getName());
                    itemDes.setText(innerModels.get(i-1).getDes());
                    itemPrice=innerModels.get(i-1).getPrice();
                    totalPrice=itemPrice*qty;
                    double tip = totalPrice * 15 / 100;
                    double tax = totalPrice * 13 / 100;
                    if(dineIn==1){
                        tvTotalPrice.setText("Tip is 15 % of Total bill Tip is $"+tip+"\n"
                                +"Tax is 13 % of Total bill Tax is $"+tax+"\n"
                                +"Total Price = $ "+Math.round(totalPrice+tip+tax));
                    }else {
                        tvTotalPrice.setText("Tip is 15 % of Total bill Tip is $"+tip+"\n"
                                +"Total Price = $ "+Math.round(totalPrice+tip));
                    }
                    Glide.with(MainActivity.this)
                            .load(innerModels.get(i-1).getUrl())
                            .listener(new RequestListener<Drawable>() {
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                    progressBar.setVisibility(View.GONE);
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                    progressBar.setVisibility(View.GONE);
                                    return false;
                                }
                            })
                            .into(ivItem);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                RadioButton checkedRadioButton = (RadioButton)group.findViewById(checkedId);
                boolean isChecked = checkedRadioButton.isChecked();
                if (isChecked)
                {
                    if(checkedRadioButton.getText().equals("DineIn")){
                        delivery.setChecked(false);
                        pick.setChecked(true);
                        dineIn=1;
                        qty=Integer.parseInt(etQty.getText().toString());
                        totalPrice=itemPrice*qty;
                        double tip = totalPrice * 15 / 100;
                        double tax = totalPrice * 13 / 100;
                        tvTotalPrice.setText("Tip is 15 % of Total bill Tip is $"+tip+"\n"
                                +"Tax is 13 % of Total bill Tax is $"+tax+"\n"
                                +"Total Price = $ "+Math.round(totalPrice+tip+tax));
                    }else{
                        delivery.setChecked(true);
                        pick.setChecked(false);
                        dineIn=2;
                        double tip = totalPrice * 15 / 100;
                        tvTotalPrice.setText("Tip is 15 % of Total bill Tip is $"+tip+"\n"
                                +"Total Price = $ "+Math.round(totalPrice+tip));
                    }
                }
            }
        });
        etQty.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(etQty.getText().toString().equals("")){
                    Toast.makeText(MainActivity.this, "Enter Valid Quantity", Toast.LENGTH_SHORT).show();
                    tvTotalPrice.setText("Total Price $ : 0" );
                }else {
                qty=Integer.parseInt(etQty.getText().toString());
                totalPrice=itemPrice*qty;
                double tip = totalPrice * 15 / 100;
                double tax = totalPrice * 13 / 100;
                if(dineIn==1){
                    tvTotalPrice.setText("Tip is 15 % of Total bill Tip is $"+tip+"\n"
                            +"Tax is 13 % of Total bill Tax is $"+tax+"\n"
                            +"Total Price = $ "+Math.round(totalPrice+tip+tax));
                }else {
                    tvTotalPrice.setText("Tip is 15 % of Total bill Tip is $"+tip+"\n"
                            +"Total Price = $ "+Math.round(totalPrice+tip));
                }
            }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    private void loadNewData(JSONArray array) throws JSONException {
        foodName.clear();
        foodName.add("Select");
        innerModels.clear();
        for(int j=0;j<array.length();j++){
            JSONObject object=(JSONObject)array.get(j);
            String f= object.getString("name");
            foodName.add(f);
            InnerModel innerModel=new InnerModel(f,object.getString("image"),object.getString("des"),object.getInt("price"));
            innerModels.add(innerModel);
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_item, foodName);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        foodItemSpinner.setAdapter(dataAdapter);
    }

    private void getDataFromApi(String url) {
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                mainProgress.setVisibility(View.GONE);
                try {
                    JSONObject object = new JSONObject(response);
                    JSONArray array = object.getJSONArray("data");
                    foodCategories.add("Select Cuisines");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject data = (JSONObject) array.get(i);
                        foodCategories.add(data.getString("cuisines"));
                        JSONArray list=data.getJSONArray("list");
                        MainModel mainModel=new MainModel(data.getString("cuisines"),list);
                        mainModels.add(mainModel);
                    }
                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_item, foodCategories);
                    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    foodSpinner.setAdapter(dataAdapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mainProgress.setVisibility(View.GONE);
            }
        });
        Volley.newRequestQueue(this).add(request);
    }

}
