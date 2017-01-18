package game.com.multiplecategory;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import game.com.multiplecategory.model.CatSubCat;
import game.com.multiplecategory.model.Category;
import game.com.multiplecategory.model.SubCategory;
import game.com.multiplecategory.mypojo.CategoryArrayData;
import game.com.multiplecategory.mypojo.CatrgoryListContributer;
import game.com.multiplecategory.retrofit2.MiiRestClient;
import game.com.multiplecategory.view.FlowLayout;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    TextView add_category, txt_subcategory;
    AlertDialog alert, alert1;
    RecyclerView catrv;

    ArrayList<CatSubCat> catSubCatArr;

    ArrayList<Category> categoryArr;
    ArrayList<String> categoryArrShow;

    ArrayList<String> subcategoryArr;
    ArrayList<SubCategory> subcategoryArrShow;

    ArrayList<CategoryArrayData> contricategoryArray;

    FlowLayout fllayout;
    MiiRestClient client;
    AutoCompleteTextView edt_category, edt_sub_category;
    String strCategory, strSubCategory;
    Button btn_submit;
    LinearLayout sub_category_ll;
    int pointer1 = 0;
    int pointer2 = 0;
    int finalPointer = 0;
    RecyclerView recyclerView;
    int count;
    SubCatOptionAdapter subCatOptionAdapter;
    CatOptionAdapter catAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        client = new MiiRestClient();

        catSubCatArr = new ArrayList<>();

        categoryArr = new ArrayList<>();
        categoryArrShow = new ArrayList<>();

        subcategoryArr = new ArrayList<>();
        subcategoryArrShow = new ArrayList<>();

        strSubCategory = "";
        strCategory = "";


        contricategoryArray = new ArrayList<CategoryArrayData>();

        catrv = (RecyclerView) findViewById(R.id.catrv);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        catrv.setLayoutManager(layoutManager);

        catAdapter = new CatOptionAdapter(MainActivity.this);
        catrv.setAdapter(catAdapter);

        client.getService().getCategoryList().enqueue(new Callback<CatrgoryListContributer>() {

            @Override
            public void onResponse(Call<CatrgoryListContributer> call, Response<CatrgoryListContributer> response) {
                contricategoryArray = response.body().getData();
                int catCount = contricategoryArray.size();
                try {
                    for (int i = 0; i < catCount; i++) {
                        count = contricategoryArray.get(i).getCount();

                        String categoryListData = contricategoryArray.get(i).getCategory();
                        Category cObj = new Category();
                        cObj.setStrCategory(categoryListData);
                        cObj.setIntFlag(0);

                        categoryArr.add(cObj);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<CatrgoryListContributer> call, Throwable t) {

            }
        });

        add_category = (TextView) findViewById(R.id.add_category);
        add_category.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add_category:
                openCategoryAlertBox(view);
                break;
            case R.id.edt_category:
                openListViewCategory(view);
                break;
            case R.id.edt_sub_category:
                openListViewSubCategory(view);
                break;
            case R.id.btn_submit:
                saveData();
                break;
            case R.id.btn_subcat_submit:
                selectSubCategory();
                break;
        }
    }

    private void selectSubCategory() {
        alert1.dismiss();
    }

    public void saveData() {

        CatSubCat obj = new CatSubCat();
        obj.setStrCategory(strCategory);
        obj.setStrSubCategory(strSubCategory);
        catSubCatArr.add(finalPointer, obj);


        int catCount = categoryArr.size();
        for(int catI = 0; catI < catCount; catI++){
            if(categoryArr.get(catI).getStrCategory().equals(strCategory)){
                categoryArr.get(catI).setIntFlag(1);
            }
        }

        pointer1 = pointer1 + 1;

        subcategoryArr = new ArrayList<String>();
        subcategoryArrShow = new ArrayList<SubCategory>();
        subCatOptionAdapter.notifyDataSetChanged();

        catAdapter.notifyDataSetChanged();

        alert.dismiss();
    }


    public void openCategoryAlertBox(View view) {

        finalPointer = pointer1;
        categoryArrShow = new ArrayList<>();

        int catCount = categoryArr.size();
        for(int catI = 0; catI < catCount; catI++){
            if(categoryArr.get(catI).getIntFlag() == 0){
                categoryArrShow.add(categoryArr.get(catI).getStrCategory());
            }
        }

        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View promptView = layoutInflater.inflate(R.layout.category_alert, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setView(promptView);
        alertDialogBuilder.setCancelable(true);

        edt_category = (AutoCompleteTextView) promptView.findViewById(R.id.edt_category);
        edt_sub_category = (AutoCompleteTextView) promptView.findViewById(R.id.edt_sub_category);

        edt_category.setOnClickListener(this);
        edt_sub_category.setOnClickListener(this);

//        txt_subcategory = (TextView) promptView.findViewById(R.id.txt_subcategory);
        fllayout = (FlowLayout) promptView.findViewById(R.id.fllayout);

        btn_submit = (Button) promptView.findViewById(R.id.btn_submit);
        btn_submit.setOnClickListener(this);

        alert = alertDialogBuilder.show();
    }

    public void openListViewCategory(View view) {

        final AlertDialog alert1;
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View promptView = layoutInflater.inflate(R.layout.option_listview_category, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setView(promptView);
        alertDialogBuilder.setCancelable(true);

        ListView lv_option = (ListView) promptView.findViewById(R.id.option_list_category);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, categoryArrShow);

        lv_option.setAdapter(adapter);
        alert1 = alertDialogBuilder.show();

        lv_option.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                String option = adapterView.getItemAtPosition(i).toString();
                alert1.dismiss();
                edt_category.setText(option);
                strCategory = option;
                subcategoryArr = new ArrayList<String>();
                subcategoryArrShow = new ArrayList<SubCategory>();

                fllayout.removeAllViews();
                strSubCategory = "";

                int countCat = contricategoryArray.size();

                if (countCat > 0) {
                    for (int k = 0; k < countCat; k++) {

                        if(contricategoryArray.get(k).getCategory().equals(strCategory)){

                            int catCount = contricategoryArray.get(k).getCount();
                            for(int m = 0; m < catCount; m++){

                                SubCategory obj = new SubCategory();
                                obj.setIntFlag(0);
                                obj.setStrSubCategory(contricategoryArray.get(k).getSub_category().get(m));

                                subcategoryArr.add(contricategoryArray.get(k).getSub_category().get(m));
                                subcategoryArrShow.add(obj);
                            }
                            break;
                        }
                    }
                }
            }
        });
    }

    public void openListViewSubCategory(View view) {

        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View promptView = layoutInflater.inflate(R.layout.option_list_sublayout, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setView(promptView);
        alertDialogBuilder.setCancelable(true);
        // ListView lv_option1 = (ListView) promptView.findViewById(R.id.option_list_category);
        recyclerView = (RecyclerView) promptView.findViewById(R.id.recyclerView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        Button btn_subcat_submit = (Button) promptView.findViewById(R.id.btn_subcat_submit);

        btn_subcat_submit.setOnClickListener(this);
//        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
//                android.R.layout.simple_list_item_1, subcategoryArrShow);

        subCatOptionAdapter = new SubCatOptionAdapter(this);
        recyclerView.setAdapter(subCatOptionAdapter);
        alert1 = alertDialogBuilder.show();

    }

    private class SubCatOptionAdapter extends RecyclerView.Adapter<SubCatOptionAdapter.MyViewHolder> {

        Context context;


        public SubCatOptionAdapter(Context context) {
            this.context = context;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.subcategory_options, parent, false);

            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {

            holder.txt_subcat.setText(subcategoryArrShow.get(position).getStrSubCategory());
            if(subcategoryArrShow.get(position).getIntFlag() == 1){
                holder.chk_subcat.setChecked(true);
            }


            holder.chk_subcat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(holder.chk_subcat.isChecked()){
                        subcategoryArrShow.get(position).setIntFlag(1);
                    }else {
                        subcategoryArrShow.get(position).setIntFlag(0);
                    }

                    int lCount = subcategoryArrShow.size();
                    fllayout.removeAllViews();
                    strSubCategory = "";

                    for(int l = 0; l < lCount; l++){
                        if(subcategoryArrShow.get(l).getIntFlag() == 1){
                           strSubCategory =   subcategoryArrShow.get(l).getStrSubCategory() + ", " + strSubCategory;

                            LayoutInflater layoutInflater = LayoutInflater.from(MainActivity.this);
                            View promptView = layoutInflater.inflate(R.layout.sub_category_txt, null);
                            txt_subcategory = (TextView) promptView.findViewById(R.id.txt_subcategory);
                            txt_subcategory.setText(subcategoryArrShow.get(l).getStrSubCategory());
                            fllayout.addView(txt_subcategory);

                        }
                    }
//                    txt_subcategory.setText("");
//                    txt_subcategory.setText(strSubCategory);


                }
            });

        }

        @Override
        public int getItemCount() {
            return subcategoryArrShow.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {

            TextView txt_subcat;
            CheckBox chk_subcat;

            public MyViewHolder(View itemView) {
                super(itemView);
                txt_subcat = (TextView) itemView.findViewById(R.id.txt_subcat);
                chk_subcat = (CheckBox) itemView.findViewById(R.id.chk_subcat);
            }
        }
    }


    private class CatOptionAdapter extends RecyclerView.Adapter<CatOptionAdapter.MyViewHolder> {

        Context context;


        public CatOptionAdapter(Context context) {
            this.context = context;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_options, parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {
            holder.txt_cat.setText(catSubCatArr.get(position).getStrCategory());
            holder.txt_subcat.setText(catSubCatArr.get(position).getStrSubCategory());
        }

        @Override
        public int getItemCount() {
            return catSubCatArr.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {

            TextView txt_cat, txt_subcat;

            public MyViewHolder(View itemView) {
                super(itemView);
                txt_cat = (TextView) itemView.findViewById(R.id.txt_cat);
                txt_subcat = (TextView) itemView.findViewById(R.id.txt_subcat);

            }
        }
    }
}
