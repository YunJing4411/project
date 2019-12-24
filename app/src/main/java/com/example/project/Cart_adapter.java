package com.example.project;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.amulyakhare.textdrawable.TextDrawable;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{
    public TextView txt_cart_Name,txt_cart_Price;
    public ImageView img_cart_count;

    private ItemClickListener itemClickListener;

    public void setTxt_cart_Name(TextView txt_cart_Name) {
        this.txt_cart_Name = txt_cart_Name;
    }

    public CartViewHolder(View itemView)
    {
        super(itemView);

        txt_cart_Name=(TextView)itemView.findViewById(R.id.cart_item_Name);
        txt_cart_Price = (TextView)itemView.findViewById(R.id.cart_item_Price);
        img_cart_count = (ImageView)itemView.findViewById(R.id.cart_item_count);

    }

    @Override
    public void onClick(View v) {


    }
}


public class Cart_adapter  extends RecyclerView.Adapter<CartViewHolder>
{
    private List<Order> listData = new ArrayList<>();
    private Context context;

    public Cart_adapter(List<Order> listData, Cart_main context)
    {
        this.listData=listData;
        this.context=context;
    }

    @Override
    public CartViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        LayoutInflater inflater=LayoutInflater.from(context);
        View itemView=inflater.inflate(R.layout.activity_cartitem,parent,false);
        return new CartViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position)
    {
        TextDrawable drawable=TextDrawable.builder().buildRound(listData.get(position).getQuantity(), Color.RED);
        holder.img_cart_count.setImageDrawable(drawable);

        Locale locale=new Locale("en","US");
        NumberFormat fmt=NumberFormat.getCurrencyInstance(locale);
        int price=(Integer.parseInt(listData.get(position).getPrice()))*(Integer.parseInt(listData.get(position).getQuantity()));
        holder.txt_cart_Price.setText(fmt.format(price));
        holder.txt_cart_Name.setText(listData.get(position).getProductName());
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }
}
