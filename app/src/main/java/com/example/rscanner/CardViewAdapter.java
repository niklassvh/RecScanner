package com.example.rscanner;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class CardViewAdapter extends RecyclerView.Adapter<CardViewAdapter.CardViewHolder> {

    Context context;
    List<Receipt> allReceipts;
    Integer currentIndex;

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
        System.out.println("position written: " + position);
        Receipt currentReceipt = allReceipts.get(position);
        holder.receipt = currentReceipt;
       // System.out.println("Pos: " + position);
        holder.usrId.setText("Användare:" + currentReceipt.usr);

        // drop items
        for(int i = 0; i < holder.textViews.size(); i++) {
            TextView t = holder.textViews.get(i);
            holder.linearLayout.removeView(t);
        }
        holder.textViews.clear();

        // load items
        for(int i = 0; i < holder.items.size(); i++) {
            final int id = i;
            Receipt.ReceiptItem item = holder.items.get(i);
            TextView t = new TextView(context);
            String fullText = item.getName()
                    + " : "
                    +item.getprice()
                    + ":-";
            t.setText(fullText);
            t.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    TextView clicked = (TextView) view;
                    System.out.println("clicked: " + clicked.getId());
                    // System.out.println(clicked.getText());
                    //  clicked.didTouchFocusSelect();
                    buildAlert(clicked, id, holder);

                }
            });
            holder.textViews.add(t);
            holder.linearLayout.addView(t);
        }

        /*
        // load items.
        for(int i = 0; i  < holder.items.size();i++) {

            TextView t = holder.textViews.get(i);
            holder.linearLayout.removeView(t);
            System.out.println(holder.textViews.size());
           // t.setTextAppearance(context, android.R.attr.textAppearanceLarge);
        //    System.out.println(holder.items.get(i));
            String fullText = holder.items.get(i).getName() + " : " + holder.items.get(i).getprice()
                    + ":-";
            t.setText(fullText);
            //System.out.println(allReceipts.get(position).id);
            holder.linearLayout.addView(t);

        }
        */
        holder.sum.setText("SUMMA: "+  currentReceipt.sum);
        holder.sum.setTypeface(null, Typeface.BOLD);
       holder.linearLayout.removeView(holder.sum);
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
        Button remove;
        public CardViewHolder(@NonNull View holder) {
            super(holder);
            linearLayout = holder.findViewById(R.id.linearLayout);
            usrId = holder.findViewById(R.id.usrid);
            remove = holder.findViewById(R.id.remove);
            System.out.println("getAdapterPosition: " + getAdapterPosition());
            if(currentIndex == null) {
                currentIndex = 0;
            } else {
                currentIndex++;
            }
            items = allReceipts.get(currentIndex).getItems();
            final CardViewHolder otherHolder = this;
            for (int i = 0; i <items.size(); i++){
                TextView t = new TextView(context);
                t.setId(i);
                //items.get(i).id = i;
                t.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        TextView clicked = (TextView) view;
                        System.out.println(clicked.getId());
                       // System.out.println(clicked.getText());
                      //  clicked.didTouchFocusSelect();
                        buildAlert(clicked, clicked.getId(), otherHolder);

                    }
                });
                textViews.add(t);

            }
            remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (currentIndex != null) {
                        currentIndex--;
                        allReceipts.remove(getAdapterPosition());
                        notifyItemRemoved(getAdapterPosition());
                        for (Receipt i : allReceipts)
                            System.out.println(allReceipts);

                    }
                }
            });
        }
    }
    public void buildAlert(TextView clickedText, Integer pos, @NonNull CardViewHolder holder){
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
                         deletePost(finalPos, holder);
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
    public void deletePost(int textViewId, @NonNull CardViewHolder holder){
        final int receiptIndex = holder.getAdapterPosition();
        System.out.println("DeletePost: " +  allReceipts.get(currentIndex).getItems().get(textViewId));
//        allReceipts.get(adapterPos).getItems().remove(textViewId);

        allReceipts.get(receiptIndex).getItems().remove(textViewId);

        System.out.println("Removal index: " + textViewId);
        notifyDataSetChanged();
        JsonWriter.tryWrite(allReceipts, context);

    }

}
