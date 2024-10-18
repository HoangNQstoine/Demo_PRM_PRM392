package com.example.kingpho.adapter;

import static com.example.kingpho.DetailProductActivity.formatMoney;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.kingpho.Interface.ChangeNumberItemsListener;
import com.example.kingpho.R;
import com.example.kingpho.dto.CartDTO;
import com.example.kingpho.model.Product;
import com.example.kingpho.service.ProductService;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private ArrayList<CartDTO> cartList;
    private ProductService productService;
    private ChangeNumberItemsListener changeNumberItemsListener;
    private OnItemCheckedChangeListener checkedChangeListener;
    private HashMap<Integer, Boolean> checkedStates;
    private HashMap<Integer, Double> productPrices;

    public CartAdapter(ArrayList<CartDTO> cartList, ProductService productService,
                       ChangeNumberItemsListener changeNumberItemsListener,
                       OnItemCheckedChangeListener checkedChangeListener) {
        this.cartList = cartList;
        this.productService = productService;
        this.changeNumberItemsListener = changeNumberItemsListener;
        this.checkedChangeListener = checkedChangeListener;
        this.checkedStates = new HashMap<>();
        this.productPrices = new HashMap<>();
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartDTO cartDTO = cartList.get(position);
        holder.bind(cartDTO, position);
    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

    public class CartViewHolder extends RecyclerView.ViewHolder {
        TextView productName, productPrice;
        EditText productQuantity;
        ImageView productImage, minusBtn, plusBtn;
        CheckBox checkBox;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);

            productName = itemView.findViewById(R.id.titleMethod);
            productPrice = itemView.findViewById(R.id.price);
            productQuantity = itemView.findViewById(R.id.textViewQuantity);
            productImage = itemView.findViewById(R.id.pic);
            minusBtn = itemView.findViewById(R.id.minusBtn);
            plusBtn = itemView.findViewById(R.id.plusBtn);
            checkBox = itemView.findViewById(R.id.checkbox);

            plusBtn.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    CartDTO cartDTO = cartList.get(position);
                    int currentQuantity = cartDTO.getQuantity();
                    if (currentQuantity < 100) {
                        cartDTO.setQuantity(currentQuantity + 1);
                        productQuantity.setText(String.valueOf(currentQuantity + 1));
                        if (changeNumberItemsListener != null) {
                            changeNumberItemsListener.changed();
                        }
                    } else {
                        Toast.makeText(itemView.getContext(), "Vui lòng không vượt quá 100", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            // Minus button click listener
            minusBtn.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    CartDTO cartDTO = cartList.get(position);
                    int currentQuantity = cartDTO.getQuantity();
                    if (currentQuantity > 1) {
                        cartDTO.setQuantity(currentQuantity - 1);
                        productQuantity.setText(String.valueOf(currentQuantity - 1));
                        if (changeNumberItemsListener != null) {
                            changeNumberItemsListener.changed();
                        }
                    }
                }
            });

            productQuantity.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {}

                @Override
                public void afterTextChanged(Editable s) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        CartDTO cartDTO = cartList.get(position);
                        try {
                            int quantity = Integer.parseInt(s.toString());
                            if (quantity > 100) {
                                quantity = 100;
                                productQuantity.setText(String.valueOf(quantity));
                                Toast.makeText(itemView.getContext(), "Vui lòng không vượt quá 100", Toast.LENGTH_SHORT).show();
                            }
                            cartDTO.setQuantity(quantity);
                            if (changeNumberItemsListener != null) {
                                changeNumberItemsListener.changed();
                            }
                        } catch (NumberFormatException e) {
                            // Handle invalid input
                        }
                    }
                }
            });

            checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    checkedStates.put(position, isChecked);
                    if (checkedChangeListener != null) {
                        checkedChangeListener.onItemCheckedChanged();
                    }
                }
            });
        }

        public void bind(CartDTO cartDTO, int position) {
            productService.getProductById(cartDTO.getProductId()).enqueue(new Callback<Product>() {
                @Override
                public void onResponse(Call<Product> call, Response<Product> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Product product = response.body();
                        productName.setText(product.getName());
                        productPrice.setText(formatMoney(String.valueOf((int) product.getPrice())));
                        productPrices.put(cartDTO.getProductId(), product.getPrice());

                        if (product.getImageUrls() != null && !product.getImageUrls().isEmpty()) {
                            Glide.with(itemView.getContext())
                                    .load(product.getImageUrls().get(0))
                                    .into(productImage);
                        } else {
                            productImage.setImageResource(R.drawable.icon_pho);
                        }
                    }
                    productQuantity.setText(String.valueOf(cartDTO.getQuantity()));
                    checkBox.setChecked(checkedStates.containsKey(position) ? checkedStates.get(position) : false);
                }

                @Override
                public void onFailure(Call<Product> call, Throwable t) {
                    // Handle failure to fetch product details
                }
            });
        }
    }

    public static String formatMoney(String moneyString) {
        try {
            int moneyAmount = Integer.parseInt(moneyString);

            NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.getDefault());

            DecimalFormat decimalFormat = (DecimalFormat) numberFormat;
            decimalFormat.applyPattern("#,###");

            return decimalFormat.format(moneyAmount);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return moneyString;
        }
    }

    public double calculateTotalPrice() {
        double totalPrice = 0.0;
        for (int i = 0; i < cartList.size(); i++) {
            CartDTO cartDTO = cartList.get(i);
            if (checkedStates.containsKey(i) && checkedStates.get(i)) {
                Double price = productPrices.get(cartDTO.getProductId());
                if (price != null) {
                    totalPrice += price * cartDTO.getQuantity();
                }
            }
        }
        return totalPrice;
    }

    public int calculateTotalCheckedItems() {
        int totalCheckedItems = 0;
        for (int i = 0; i < cartList.size(); i++) {
            CartDTO cartDTO = cartList.get(i);
            if (checkedStates.containsKey(i) && checkedStates.get(i)) {
                totalCheckedItems += cartDTO.getQuantity();
            }
        }
        return totalCheckedItems;
    }

    public interface OnItemCheckedChangeListener {
        void onItemCheckedChanged();
    }
}










//            minusBtn.setOnClickListener(v -> {
//                int position = getAdapterPosition();
//                if (position != RecyclerView.NO_POSITION) {
//                    manager.minusNumberFood(cartList, position, () -> {
//                        notifyItemRemoved(position);
//                        notifyItemRangeChanged(position, cartList.size());
//                        if (changeNumberItemsListener != null) {
//                            changeNumberItemsListener.changed();
//                        }
//                    });
//                }
//            });

//            plusBtn.setOnClickListener(v -> {
//                int position = getAdapterPosition();
//                if (position != RecyclerView.NO_POSITION) {
//                    manager.plusNumberFood(cartList, position, () -> {
//                        notifyItemChanged(position);
//                        if (changeNumberItemsListener != null) {
//                            changeNumberItemsListener.changed();
//                        }
//                    });
//                }
//            });
//public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
//    private ArrayList<Food> cartItemList;
//    private Manager manager;
//    private ArrayList<Food> selectedItems;
//    private CartAdapterListener listener;
//    private ChangeNumberItemsListener changeNumberItemsListener;
//
//    public CartAdapter(ArrayList<Food> cartItemList, Manager manager, ArrayList<Food> selectedItems, CartAdapterListener listener, ChangeNumberItemsListener changeNumberItemsListener) {
//        this.cartItemList = cartItemList;
//        this.manager = manager;
//        this.selectedItems = selectedItems;
//        this.listener = listener;
//        this.changeNumberItemsListener = changeNumberItemsListener;
//    }
//
//    @NonNull
//    @Override
//    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_cart, parent, false);
//        return new ViewHolder(itemView);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//        Food item = cartItemList.get(position);
//        holder.bind(item);
//
//        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
//            if (isChecked) {
//                selectedItems.add(item);
//            } else {
//                selectedItems.remove(item);
//            }
//            listener.onItemCheckedChanged(selectedItems);
//        });
//
//        holder.minusBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                manager.minusNumberFood(cartItemList, position, new ChangeNumberItemsListener() {
//                    @Override
//                    public void changed() {
//                        notifyDataSetChanged(); // Cập nhật lại giao diện của RecyclerView
//                        changeNumberItemsListener.changed(); // Thông báo thay đổi cho Fragment hoặc Activity
//                    }
//                });
//            }
//        });
//
//
//
//
//        holder.plusBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                manager.plusNumberFood(cartItemList, position, new ChangeNumberItemsListener() {
//                    @Override
//                    public void changed() {
//                        notifyDataSetChanged();
//                        changeNumberItemsListener.changed();
//                    }
//                });
//            }
//        });
//    }
//
//    @Override
//    public int getItemCount() {
//        return cartItemList.size();
//    }
//
//    public class ViewHolder extends RecyclerView.ViewHolder {
//        TextView productName, productPrice, productQuantity;
//        ImageView productImage, minusBtn, plusBtn;
//        CheckBox checkBox;
//
//        public ViewHolder(@NonNull View itemView) {
//            super(itemView);
//            productName = itemView.findViewById(R.id.titleMethod);
//            productPrice = itemView.findViewById(R.id.price);
//            productQuantity = itemView.findViewById(R.id.textViewQuantity);
//            productImage = itemView.findViewById(R.id.pic);
//            minusBtn = itemView.findViewById(R.id.minusBtn);
//            plusBtn = itemView.findViewById(R.id.plusBtn);
//            checkBox = itemView.findViewById(R.id.checkbox);
//        }
//
//        public void bind(Food item) {
//            productName.setText(item.getFoodTitle());
//            productPrice.setText(String.valueOf(item.getFoodPrice()));
//            productQuantity.setText(String.valueOf(item.getNumberInCart()));
//            int drawableResourceId = itemView.getContext().getResources().getIdentifier(item.getFoodImage(), "drawable", itemView.getContext().getPackageName());
//            Glide.with(itemView.getContext())
//                    .load(drawableResourceId)
//                    .into(productImage);
//
//            checkBox.setChecked(selectedItems.contains(item));
//        }
//
//    }
//
//    public ArrayList<Food> getSelectedItems() {
//        return selectedItems;
//    }
//}

//public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
//    private ArrayList<Food> cartItemList;
//    private Manager manager;
//    private ChangeNumberItemsListener changeNumberItemsListener;
//    private ArrayList<Food> selectedItems;
//
//    public CartAdapter(ArrayList<Food> cartItemList, Manager manager, ChangeNumberItemsListener changeNumberItemsListener) {
//        this.cartItemList = cartItemList;
//        this.manager = manager;
//        this.changeNumberItemsListener = changeNumberItemsListener;
//        this.selectedItems = new ArrayList<>();
//    }
//
//    @NonNull
//    @Override
//    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_cart, parent, false);
//        return new ViewHolder(itemView);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
//        Food item = cartItemList.get(position);
//        holder.productName.setText(item.getFoodTitle());
//        holder.productPrice.setText(String.valueOf(item.getFoodPrice()));
//        holder.productQuantity.setText(String.valueOf(item.getNumberInCart()));
//        int drawableResourceId = holder.itemView.getContext().getResources().getIdentifier(item.getFoodImage(), "drawable", holder.itemView.getContext().getPackageName());
//        Glide.with(holder.itemView.getContext())
//                .load(drawableResourceId)
//                .into(holder.productImage);
//
//        holder.checkBox.setOnCheckedChangeListener(null);
//        holder.checkBox.setChecked(selectedItems.contains(item));
//        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked) {
//                    if (!selectedItems.contains(item)) {
//                        selectedItems.add(item);
//                    }
//                } else {
//                    selectedItems.remove(item);
//                }
//            }
//        });
//
//        holder.minusBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                manager.minusNumberFood(cartItemList, position, new ChangeNumberItemsListener() {
//                    @Override
//                    public void changed() {
//                        notifyDataSetChanged();
//                        changeNumberItemsListener.changed();
//                    }
//                });
//            }
//        });
//
//        holder.plusBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                manager.plusNumberFood(cartItemList, position, new ChangeNumberItemsListener() {
//                    @Override
//                    public void changed() {
//                        notifyDataSetChanged();
//                        changeNumberItemsListener.changed();
//                    }
//                });
//            }
//        });
//    }
//
//    @Override
//    public int getItemCount() {
//        return cartItemList.size();
//    }
//
//    public ArrayList<Food> getSelectedItems() {
//        return selectedItems;
//    }
//    public double getTotalPrice() {
//        double total = 0;
//        for (Food item : selectedItems) {
//            total += item.getFoodPrice();
//        }
//        return total;
//    }
//    public static class ViewHolder extends RecyclerView.ViewHolder {
//        TextView productName, productPrice, productQuantity;
//        ImageView productImage, minusBtn, plusBtn;
//        CheckBox checkBox;
//
//        public ViewHolder(@NonNull View itemView) {
//            super(itemView);
//            productName = itemView.findViewById(R.id.titleMethod);
//            productPrice = itemView.findViewById(R.id.price);
//            productQuantity = itemView.findViewById(R.id.textViewQuantity);
//            productImage = itemView.findViewById(R.id.pic);
//            minusBtn = itemView.findViewById(R.id.minusBtn);
//            plusBtn = itemView.findViewById(R.id.plusBtn);
//            checkBox = itemView.findViewById(R.id.checkbox);
//        }
//    }
//}






