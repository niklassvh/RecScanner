package com.example.rscanner;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipOutputStream;

public class CardViewAdapter extends RecyclerView.Adapter<CardViewAdapter.CardViewHolder> {

    Context context;
    List<Receipt> allReceipts;
    Integer adapterPos;

    public CardViewAdapter(List<Receipt> allReceipts, Context context) {
        this.allReceipts=allReceipts;
        this.context = context;

    }
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.cardview, parent, false);
        return new CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
        Receipt currentReceipt = allReceipts.get(position);
        holder.receipt = currentReceipt;
        holder.usrId.setText("Användare:" + currentReceipt.usr);
        // load items.
        for(int i = 0; i  < holder.items.size();i++) {
            TextView t = holder.textViews.get(i);

           // t.setTextAppearance(context, android.R.attr.textAppearanceLarge);
            System.out.println(holder.items.get(i));
            String fullText = holder.items.get(i).getName() + " : " + holder.items.get(i).getprice()
                    + ":-";
            t.setText(fullText);
            //System.out.println(allReceipts.get(position).id);
            holder.linearLayout.addView(t);

        }
        holder.sum.setText("SUMMA: "+  currentReceipt.sum);
        holder.sum.setTypeface(null, Typeface.BOLD);
        holder.linearLayout.addView(holder.sum);

    }

    @Override
    public int getItemCount() {
        return allReceipts.size();
    }

    public class CardViewHolder extends RecyclerView.ViewHolder {
        Receipt receipt;
        Button usrId;
        TextView sum = new TextView(context);
        LinearLayout linearLayout;
        List<TextView> textViews = new ArrayList<>();
        List<Receipt.ReceiptItem> items;
        public CardViewHolder(@NonNull View holder) {
            super(holder);
            linearLayout = holder.findViewById(R.id.linearLayout);
            usrId = holder.findViewById(R.id.usrid);
            items = allReceipts.get(getAdapterPosition()+1).getItems();
            adapterPos = getAdapterPosition()+1;

            for (int i = 0; i <items.size(); i++){
                TextView t = new TextView(context);
                t.setId(i);
                t.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        TextView clicked = (TextView) view;
                        System.out.println(clicked.getId());
                       // System.out.println(clicked.getText());
                      //  clicked.didTouchFocusSelect();
                        buildAlert(clicked, clicked.getId());

                    }
                });
                textViews.add(t);
            }

        }
    }
    public void buildAlert(TextView clickedText, Integer pos){
        final Integer finalPos= pos;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Ta bort " + clickedText.getText() + " ?");
        builder.setCancelable(true);
        builder.setPositiveButton(
                "Ja",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //allReceipts.get(id+1).getItems().get(id+1);
                         System.out.println(finalPos);
                        deletePost(finalPos);
                        dialog.cancel();
                    }
                });

        builder.setNegativeButton(
                "Nej",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        System.out.println(id);
                        dialog.cancel();
                    }
                });

        AlertDialog alert = builder.create();
        alert.show();

    }

    public void deletePost(int textViewId){
       // allReceipts.get(adapterPos).getItems().get(textViewId);
        System.out.println("DeletePost: " +  allReceipts.get(adapterPos).getItems().get(textViewId));
        allReceipts.get(adapterPos).getItems().remove(textViewId);
        System.out.println("successfully removed" + allReceipts.get(adapterPos).getItems().get(textViewId));
       notifyDataSetChanged(adapterPos);


    }

    private void notifyDataSetChanged(Integer adapterPos) {


    }


}
