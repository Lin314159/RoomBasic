package com.example.roombasic;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

public class MyAdapter extends ListAdapter<Word, MyAdapter.MyViewHolder> {
    private WordViewModel model;
    private boolean useCard;

    MyAdapter(boolean aSwitch, WordViewModel model) {
        super(new DiffUtil.ItemCallback<Word>() {
            @Override
            public boolean areItemsTheSame(@NonNull Word oldItem, @NonNull Word newItem) {
                return oldItem.getId() == newItem.getId();
            }

            @Override
            public boolean areContentsTheSame(@NonNull Word oldItem, @NonNull Word newItem) {
                return oldItem.getWord().equals(newItem.getWord()) && oldItem.getChineseMeaning().equals(newItem.getChineseMeaning())
                        && oldItem.isChinese() == newItem.isChinese();
            }
        });
        this.model = model;
        this.useCard = aSwitch;
    }

    @Override
    public void onViewAttachedToWindow(@NonNull MyViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        holder.number.setText(String.valueOf(holder.getAdapterPosition()+1));
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;
        if (useCard) {
            view = inflater.inflate(R.layout.cell_card_2, parent, false);
        } else {
            view = inflater.inflate(R.layout.cell_normal_2, parent, false);
        }

        final MyViewHolder holder = new MyViewHolder(view);
        holder.aSwitch_chinese.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Word word = (Word) holder.itemView.getTag(R.id.word_for_view_holder);
                if (isChecked) {
                    holder.chinese.setVisibility(View.GONE);
                    word.setChinese(true);
                    model.updata(word);
                } else {
                    holder.chinese.setVisibility(View.VISIBLE);
                    word.setChinese(false);
                    model.updata(word);
                }
            }
        });

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        final Word word = getItem(position);
        holder.itemView.setTag(R.id.word_for_view_holder, word);
        holder.number.setText(String.valueOf(position + 1));
        holder.english.setText(word.getWord());
        holder.chinese.setText(word.getChineseMeaning());
        if (word.isChinese()) {
            holder.chinese.setVisibility(View.GONE);
            holder.aSwitch_chinese.setChecked(true);
        } else {
            holder.chinese.setVisibility(View.VISIBLE);
            holder.aSwitch_chinese.setChecked(false);
        }
    }


    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView number, english, chinese;
        Switch aSwitch_chinese;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            number = itemView.findViewById(R.id.text_id);
            english = itemView.findViewById(R.id.text_english);
            chinese = itemView.findViewById(R.id.text_chinese);
            aSwitch_chinese = itemView.findViewById(R.id.switch_chinese);
        }

    }
}
