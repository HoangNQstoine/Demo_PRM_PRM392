package com.example.kingpho.helper;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.kingpho.Interface.ChangeNumberItemsListener;
import com.example.kingpho.model.Food;

import java.util.ArrayList;

public class Manager {
    private Context context;
    private TinyDB tinyDB;

    public Manager(Context context, TinyDB tinyDB) {
        this.context = context;
        this.tinyDB = tinyDB;
    }
    public void addToFavourites(Food item) {
        ArrayList<Food> list = getFavouritesList();
        list.add(item);
        tinyDB.putListObject("FavouritesList", list);
        Toast.makeText(context, "Added to favourites: " + item.getFoodTitle(), Toast.LENGTH_SHORT).show();
    }

    public void removeFromFavourites(Food item) {
        ArrayList<Food> list = getFavouritesList();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getFoodTitle().equals(item.getFoodTitle())) {
                list.remove(i);
                break;
            }
        }
        tinyDB.putListObject("FavouritesList", list);
        Toast.makeText(context, "Removed from favourites: " + item.getFoodTitle(), Toast.LENGTH_SHORT).show();
    }

    public boolean isFavourite(Food item) {
        ArrayList<Food> list = getFavouritesList();
        for (Food food : list) {
            if (food.getFoodTitle().equals(item.getFoodTitle())) {
                return true;
            }
        }
        return false;
    }

    public ArrayList<Food> getFavouritesList() {
        return tinyDB.getListObject("FavouritesList");
    }


    public  void addToCart(Food item) {
        ArrayList<Food> list = getListCart();
        boolean isExist = false;
        int n = 0;
        for (int i = 0; i < list.size(); i++){
            if(list.get(i).getFoodTitle().equals(item.getFoodTitle())){
                isExist = true;
                n = i;
                break;
            }
        }
        if (isExist) {
            int currentNumber = list.get(n).getNumberInCart();
            int newNumber = currentNumber + item.getNumberInCart();
            list.get(n).setNumberInCart(newNumber);
            Toast.makeText(context, "Updated quantity: " + newNumber, Toast.LENGTH_SHORT).show();
        } else {
            list.add(item);
            Toast.makeText(context, "Added new item: " + item.getFoodTitle(), Toast.LENGTH_SHORT).show();
        }

        tinyDB.putListObject("CartList", list);


    }

    public ArrayList<Food> getListCart(){
        return tinyDB.getListObject("CartList");
    }

    public void plusNumberFood(ArrayList<Food> list , int position, ChangeNumberItemsListener changeNumberItemsListener){
        list.get(position).setNumberInCart(list.get(position).getNumberInCart() + 1);
        tinyDB.putListObject("CartList", list);
        changeNumberItemsListener.changed();
    }

    public void minusNumberFood(ArrayList<Food> list, int position, ChangeNumberItemsListener changeNumberItemsListener) {
        int currentNumber = list.get(position).getNumberInCart();
        if (currentNumber > 1) {
            list.get(position).setNumberInCart(currentNumber - 1);
        } else if (currentNumber == 1) {
            list.remove(position);
        }
        tinyDB.putListObject("CartList", list);
        changeNumberItemsListener.changed();
    }



//    public Double getTotalPrice(){
//        ArrayList<Food> list = getListCart();
//        double price =0;
//        for (int i = 0; i < list.size(); i++){
//            price = price + (list.get(i).getFoodPrice() * list.get(i).getNumberInCart());
//        }
//        return price;
//    }

}

