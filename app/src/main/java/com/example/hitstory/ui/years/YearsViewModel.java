package com.example.hitstory.ui.years;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class YearsViewModel extends ViewModel {

    MutableLiveData<ArrayList<String>> data = new MutableLiveData<>();

    public void getData(){
        ArrayList<String> arrayList = new ArrayList<>();
        for (int i = 4; i < 21; i++) {
            if (i>9){
                arrayList.add("20"+i);
            }else{
                arrayList.add("200"+i);
            }
        }
        data.setValue(arrayList);
    }
}