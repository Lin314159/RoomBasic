package com.example.roombasic;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddFragment extends Fragment {
    EditText english,chinese;
    Button submit;
    WordViewModel model;
    private InputMethodManager imm;

    public AddFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        model = new ViewModelProvider(requireActivity()).get(WordViewModel.class);
        english=requireActivity().findViewById(R.id.editText_en);
        chinese=requireActivity().findViewById(R.id.editText_cn);
        submit=requireActivity().findViewById(R.id.button_submit);
        english.requestFocus();
        submit.setClickable(false);
        imm= (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(english,0);
        TextWatcher textWatcher=new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String englishstr=english.getText().toString().trim();
                String chinesestr=chinese.getText().toString().trim();
                submit.setEnabled(!englishstr.isEmpty()&&!chinesestr.isEmpty());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String englishstr=english.getText().toString().trim();
                String chinesestr=chinese.getText().toString().trim();
                Word word=new Word(englishstr,chinesestr);
                model.insert(word);
                NavController controller= Navigation.findNavController(v);
                controller.navigateUp();
                imm.hideSoftInputFromWindow(v.getWindowToken(),0);
            }
        });
    }
}
