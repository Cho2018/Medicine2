package com.example.medicine;

import android.animation.ValueAnimator;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class PillListRecyclerViewAdapter  extends RecyclerView.Adapter<PillListRecyclerViewAdapter.ItemViewHolder> {
    private ArrayList<PillList> pList;
    private LayoutInflater pInflate;
    private Context context;
    View view;
    private ItemViewHolder holder;
    private SparseBooleanArray selectedItems = new SparseBooleanArray();
    private int prePosition = -1;
    private String[] time_array;
    private int times;
    private String oneTime, twoTime, threeTime, fourTime, fiveTime;

    int id;

    public PillListRecyclerViewAdapter(ArrayList<PillList> pList, Context context) {
        this.pList = pList;
        this.pInflate = LayoutInflater.from(context);
        this.context = context;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pill_item_list, parent, false);
        return new ItemViewHolder(view);
    }

    public void onBindViewHolder(@NonNull final ItemViewHolder holder, final int position) {
        holder.onBind(pList.get(position), position);
    }

    @Override
    public int getItemCount() {
        return pList.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private PillList pList;
        private int position;
        private TextView txt_pill_item_list_mediName;
        private TextView txt_pill_item_list_time1, txt_pill_item_list_time2, txt_pill_item_list_time3,
                txt_pill_item_list_time4, txt_pill_item_list_time5;
        private ImageView btn_clock;
        private LinearLayout lay1, lay2, lay3, lay4, lay5;

        ItemViewHolder(View itemView) {
            super(itemView);

            txt_pill_item_list_mediName = itemView.findViewById(R.id.txt_pill_item_list_mediName);
            txt_pill_item_list_time1 = itemView.findViewById(R.id.txt_pill_item_list_time1);
            txt_pill_item_list_time2 = itemView.findViewById(R.id.txt_pill_item_list_time2);
            txt_pill_item_list_time3 = itemView.findViewById(R.id.txt_pill_item_list_time3);
            txt_pill_item_list_time4 = itemView.findViewById(R.id.txt_pill_item_list_time4);
            txt_pill_item_list_time5 = itemView.findViewById(R.id.txt_pill_item_list_time5);
            btn_clock = itemView.findViewById(R.id.btn_clock);
            lay1 = itemView.findViewById(R.id.lay1);
            lay2 = itemView.findViewById(R.id.lay2);
            lay3 = itemView.findViewById(R.id.lay3);
            lay4 = itemView.findViewById(R.id.lay4);
            lay5 = itemView.findViewById(R.id.lay5);
        }

        void onBind(PillList pList, final int position) {
            this.pList = pList;
            this.position = position;

            times = pList.getTimesPerDay();
            oneTime = pList.getOneTime();
            twoTime = pList.getTwoTime();
            threeTime = pList.getThreeTime();
            fourTime = pList.getFourTime();
            fiveTime = pList.getFiveTime();

            time_array = new String[5];
            time_array[0] = oneTime;
            time_array[1] = twoTime;
            time_array[2] = threeTime;
            time_array[3] = fourTime;
            time_array[4] = fiveTime;

            TextView[] pill = new TextView[5];
            pill[0] = txt_pill_item_list_time1;
            pill[1] = txt_pill_item_list_time2;
            pill[2] = txt_pill_item_list_time3;
            pill[3] = txt_pill_item_list_time4;
            pill[4] = txt_pill_item_list_time5;

            for(int i = 0; i < pList.getTimesPerDay(); i++) {
                if (time_array[i] != null) {
                    if (Integer.parseInt(time_array[i].substring(0, 2)) < 12) {
                        pill[i].setText("오전 " + time_array[i].substring(0, 2) + " : "
                                + time_array[i].substring(3, 5));
                    } else if (Integer.parseInt(time_array[i].substring(0, 2)) > 12) {
                        int n = Integer.parseInt(time_array[i].substring(0, 2));
                        if (n - 12 < 10) {
                            pill[i].setText("오후 0" + Integer.toString(n - 12) + " : " + time_array[i].substring(3, 5));
                        } else {
                            pill[i].setText("오후 " + Integer.toString(n - 12) + " : " + time_array[i].substring(3, 5));
                        }
                    } else {
                        pill[i].setText("오후 " + time_array[i].substring(0, 2) + " : " + time_array[i].substring(3, 5));
                    }
                }
            }

            txt_pill_item_list_mediName.setText(pList.getMediName());

            changeVisibility(selectedItems.get(position));

            itemView.setOnClickListener(this);
            txt_pill_item_list_mediName.setOnClickListener(this);
            txt_pill_item_list_time1.setOnClickListener(this);
            txt_pill_item_list_time2.setOnClickListener(this);
            txt_pill_item_list_time3.setOnClickListener(this);
            txt_pill_item_list_time4.setOnClickListener(this);
            txt_pill_item_list_time5.setOnClickListener(this);
            btn_clock.setOnClickListener(this);
            lay1.setOnClickListener(this);
            lay2.setOnClickListener(this);
            lay3.setOnClickListener(this);
            lay4.setOnClickListener(this);
            lay5.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.lay_pill:
                    if (selectedItems.get(position)) {
                        selectedItems.delete(position);
                    } else {
                        selectedItems.put(position, true);
                    }
                    if (prePosition != -1) notifyItemChanged(prePosition);
                    notifyItemChanged(position);
                    prePosition = position;
                    break;
                case R.id.btn_clock:
                    if (selectedItems.get(position)) {
                        selectedItems.delete(position);
                    } else {
                        selectedItems.put(position, true);
                    }
                    if (prePosition != -1) notifyItemChanged(prePosition);
                    notifyItemChanged(position);
                    prePosition = position;
                    break;
                case R.id.txt_pill_item_list_mediName:
                    if (selectedItems.get(position)) {
                        selectedItems.delete(position);
                    } else {
                        selectedItems.put(position, true);
                    }
                    if (prePosition != -1) notifyItemChanged(prePosition);
                    notifyItemChanged(position);
                    prePosition = position;
                    break;
            }
        }

        private void changeVisibility(final boolean isExpanded) {
            int dpValue = 50;
            float d = context.getResources().getDisplayMetrics().density;
            int height = (int) (dpValue * d);

            final String two = pList.getTwoTime();
            final String three = pList.getThreeTime();
            final String four = pList.getFourTime();
            final String five = pList.getFiveTime();

            ValueAnimator va = isExpanded ? ValueAnimator.ofInt(0, height) : ValueAnimator.ofInt(height, 0);
            va.setDuration(150);
            va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    int value = (int) animation.getAnimatedValue();

                    lay1.getLayoutParams().height = value;
                    lay1.requestLayout();
                    lay1.setVisibility(isExpanded ? View.VISIBLE : View.GONE);

                    lay2.getLayoutParams().height = value;
                    lay2.requestLayout();
                    if (two.equals("null")) {
                        lay2.setVisibility(isExpanded ? View.GONE : View.GONE);
                    } else {
                        lay2.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
                    }

                    lay3.getLayoutParams().height = value;
                    lay3.requestLayout();
                    if (three.equals("null")) {
                        lay3.setVisibility(isExpanded ? View.GONE : View.GONE);
                    } else {
                        lay3.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
                    }

                    lay4.getLayoutParams().height = value;
                    lay4.requestLayout();
                    if (four.equals("null")) {
                        lay4.setVisibility(isExpanded ? View.GONE : View.GONE);
                    } else {
                        lay4.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
                    }

                    lay5.getLayoutParams().height = value;
                    lay5.requestLayout();
                    if (five.equals("null")) {
                        lay5.setVisibility(isExpanded ? View.GONE : View.GONE);
                    } else {
                        lay5.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
                    }
                }
            });
            va.start();
        }
    }
}
