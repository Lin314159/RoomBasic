package com.example.roombasic;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class WordsFragment extends Fragment {
    private WordViewModel model;
    private RecyclerView recyclerView;
    private MyAdapter adapter1, adapter2;
    private LiveData<List<Word>> filteredWords;
    private static final String IS_USEING_CARD_VIEW = "is_useing_card_view";
    private List<Word> allWords;
    private DividerItemDecoration dividerItemDecoration;
    private boolean undoAction = false;

    public WordsFragment() {
        // Required empty public constructor
        setHasOptionsMenu(true);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.clearData:
                new AlertDialog.Builder(requireActivity()).setTitle("清空数据!").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        model.deleteAll();
                    }
                }).setNegativeButton("取消", null).create().show();
                break;
            case R.id.choose_view_type:
                SharedPreferences shp = requireActivity().getSharedPreferences("view_type", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = shp.edit();
                boolean viewType = shp.getBoolean(IS_USEING_CARD_VIEW, false);
                if (viewType) {
                    recyclerView.addItemDecoration(dividerItemDecoration);
                    recyclerView.setAdapter(adapter1);
                    editor.putBoolean(IS_USEING_CARD_VIEW, false);
                } else {
                    recyclerView.removeItemDecoration(dividerItemDecoration);
                    recyclerView.setAdapter(adapter2);
                    editor.putBoolean(IS_USEING_CARD_VIEW, true);
                }
                editor.apply();
                break;
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main_menu, menu);
        SearchView searchView = (SearchView) menu.findItem(R.id.app_bar_search).getActionView();
        searchView.setMaxWidth(1000);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                String patten = newText.trim();
                filteredWords.removeObservers(requireActivity());
                filteredWords = model.findWordsWithPatten(patten);
                filteredWords.observe(getViewLifecycleOwner(), new Observer<List<Word>>() {
                    @Override
                    public void onChanged(List<Word> words) {
                        int temp = adapter1.getItemCount();
                        allWords = words;
                        if (temp != words.size()) {
                            recyclerView.smoothScrollBy(0, -300);
                            adapter1.submitList(words);
                            adapter2.submitList(words);
                        }
                    }
                });
                return true;
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_words, container, false);
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        model = new ViewModelProvider(requireActivity()).get(WordViewModel.class);
        adapter1 = new MyAdapter(false, model);
        adapter2 = new MyAdapter(true, model);
        recyclerView = getView().findViewById(R.id.recycle_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator() {
            @Override
            public void onAnimationFinished(@NonNull RecyclerView.ViewHolder viewHolder) {
                super.onAnimationFinished(viewHolder);
                LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int firstPosition = manager.findFirstVisibleItemPosition();
                int lastPosition = manager.findLastCompletelyVisibleItemPosition();
                for (int i = firstPosition; i <= lastPosition; i++) {
                    MyAdapter.MyViewHolder holder = (MyAdapter.MyViewHolder) recyclerView.findViewHolderForAdapterPosition(i);
                    holder.number.setText(String.valueOf(i + 1));
                }
            }
        });
        dividerItemDecoration = new DividerItemDecoration(requireActivity(), DividerItemDecoration.VERTICAL);
        SharedPreferences shp = requireActivity().getSharedPreferences("view_type", Context.MODE_PRIVATE);
        boolean viewType = shp.getBoolean(IS_USEING_CARD_VIEW, false);
        if (viewType) {
            recyclerView.setAdapter(adapter2);
        } else {
            recyclerView.addItemDecoration(dividerItemDecoration);
            recyclerView.addItemDecoration(dividerItemDecoration);
            recyclerView.setAdapter(adapter1);
        }

        filteredWords = model.getAllwords();
        filteredWords.observe(getViewLifecycleOwner(), new Observer<List<Word>>() {
            @Override
            public void onChanged(List<Word> words) {
                int temp = adapter1.getItemCount();
                allWords = words;
                if (temp != words.size()) {
                    if (temp < words.size() && !undoAction) {
                        recyclerView.smoothScrollBy(0, -300);
                        undoAction = false;
                    }
                    adapter1.submitList(words);
                    adapter2.submitList(words);
                }
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.START | ItemTouchHelper.END) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                /*Word wordFrom=allWords.get(viewHolder.getAdapterPosition());
                Word wordTo=allWords.get(target.getAdapterPosition());
                int idtemp=wordFrom.getId();
                wordFrom.setId(wordTo.getId());
                wordTo.setId(idtemp);
                model.updata(wordFrom,wordTo);
                adapter1.notifyItemMoved(viewHolder.getAdapterPosition(),target.getAdapterPosition());
                adapter2.notifyItemMoved(viewHolder.getAdapterPosition(),target.getAdapterPosition());*/
                return true;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                final Word wordToDelete = allWords.get(viewHolder.getAdapterPosition());
                model.delete(wordToDelete);
                Snackbar.make(requireActivity().findViewById(R.id.words_fragment), "删除了一个词汇", Snackbar.LENGTH_LONG)
                        .setAction("撤销", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                undoAction = true;
                                model.insert(wordToDelete);
                            }
                        }).show();

            }
        }).attachToRecyclerView(recyclerView);
        FloatingActionButton floatingActionButton = requireActivity().findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController controller = Navigation.findNavController(v);
                controller.navigate(R.id.action_wordsFragment_to_addFragment);
            }
        });
    }


}