package com.example.kingpho.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;

import com.example.kingpho.DetailProductActivity;
import com.example.kingpho.R;
import com.example.kingpho.adapter.FoodAdapter;
import com.example.kingpho.callback.ProductCallback;
import com.example.kingpho.model.FoodActivity;
import com.example.kingpho.model.Product;
import com.example.kingpho.service.ProductService;
import com.example.kingpho.utils.RetrofitClient;
import com.example.kingpho.utils.SharedPrefManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ProductFragment extends Fragment {

    private ProductService productService;

    private SearchView searchView;
    private GridView gridView;
    private FoodAdapter adapter;
    private List<Product> allProductList;
    private List<Product> filteredProductList;
    private ImageButton filterButton;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_search_list, container, false);

        searchView = view.findViewById(R.id.searchView);
        gridView = view.findViewById(R.id.gridView);
        filterButton = view.findViewById(R.id.filterButton);

        Retrofit retrofit = RetrofitClient.getRetrofitInstance(getContext());
        productService = retrofit.create(ProductService.class);

        // Khởi tạo danh sách mẫu
        allProductList = new ArrayList<>();

        getAllProducts(new ProductCallback() {
            @Override
            public void onListProductFetched(List<Product> productList) {
                for (Product product : productList) {
                    allProductList.add(new Product(product.getId(), product.getName(), product.getDescription(),
                            product.getPrice(), product.getCategory(), product.getImageUrls()));
                }

                filteredProductList = new ArrayList<>(allProductList);
                adapter.updateData(filteredProductList);
            }

            @Override
            public void onProductFetched(Product product) {

            }

            @Override
            public void onError(Throwable throwable) {
                throwable.printStackTrace();
            }
        });

        // Khởi tạo danh sách filteredFoodList ban đầu
        filteredProductList = new ArrayList<>();
        Log.d("Filter Product Size", filteredProductList.size() + "");

        // Khởi tạo adapter
        adapter = new FoodAdapter(getContext(), filteredProductList);
        gridView.setAdapter(adapter);

        // Xử lý khi nhấn vào nút filterButton
        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFilterPopupMenu();
            }
        });

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Product selectedProduct = (Product) parent.getItemAtPosition(position);
                Intent intent = new Intent(getContext(), DetailProductActivity.class);
                // Truyền thông tin extra nếu cần thiết
                intent.putExtra("foodId", selectedProduct.getId());
                startActivity(intent);
                Toast.makeText(getContext(), "Bạn đã chọn sản phẩm: " + selectedProduct.getName(), Toast.LENGTH_SHORT).show();
            }
        });

        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchView.setIconified(false);
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.filter(newText);
                return true;
            }
        });

        return view;
    }

    private void showFilterPopupMenu() {
        PopupMenu popupMenu = new PopupMenu(requireContext(), filterButton);
        popupMenu.getMenuInflater().inflate(R.menu.filter_menu, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.filter_by_name) {
                    filterData("Tên A-Z");
                } else if (itemId == R.id.filter_by_price) {
                    filterData("Giá tiền");
                }

                return true;
            }
        });

        popupMenu.show();
    }

    private void filterData(String filterOption) {
        List<Product> sortedList = new ArrayList<>(allProductList); // Tạo một bản sao của danh sách gốc

        switch (filterOption) {
            case "Tên A-Z":
                // Sắp xếp danh sách theo tên từ A-Z
                Collections.sort(sortedList, new Comparator<Product>() {
                    @Override
                    public int compare(Product food1, Product food2) {
                        return food1.getName().compareTo(food2.getName());
                    }
                });
                break;
            case "Giá tiền":
                // Sắp xếp danh sách theo giá tiền
                Collections.sort(sortedList, new Comparator<Product>() {
                    @Override
                    public int compare(Product food1, Product food2) {
                        return Double.compare(food1.getPrice(), food2.getPrice());
                    }
                });
                break;
            default:
                break;
        }

        // Cập nhật lại adapter sau khi sắp xếp
        adapter.updateData(sortedList);
    }

    private void getAllProducts(ProductCallback callback) {
        try {
            Call<List<Product>> call = productService.getAllProducts();
            call.enqueue(new Callback<List<Product>>() {
                @Override
                public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        List<Product> productList = response.body();

                        callback.onListProductFetched(productList);
                    }
                    else {
                        Toast.makeText(getContext(), "Empty product", Toast.LENGTH_SHORT).show();
                        Log.w("Product", "Empty Product");
                    }
                }

                @Override
                public void onFailure(Call<List<Product>> call, Throwable throwable) {
                    throwable.printStackTrace();
                }
            });
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
