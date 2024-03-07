package com.example.eventease2;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;

public class AdminEventView extends AppCompatActivity {

    ListView eventList;
    ArrayList<Event> eventDataList;
    AdminArrayAdapter adminArrayAdapter;
    private TextView eventNameText;
    private EditText eventTitle;
    private Button eventDetailsButton;
    private Button eventAttendeeButton;
    private Button backButton;
    private ListView eventListView;
    private FirebaseFirestore appDb;
    private CollectionReference eventIdRefrence;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_event_view);

        appDb = FirebaseFirestore.getInstance();

        eventIdRefrence = appDb.collection("Organizer");

        eventNameText = findViewById(R.id.);
        addNameText = findViewById(R.id.);
        findViewById(R.id.);
        findViewById(R.id.);
        findViewById(R.id.);
        findViewById(R.id.);


        addProvinceEditText = findViewById(R.id.province_name_edit);
        addCityButton = findViewById(R.id.add_city_button);
        cityList = findViewById(R.id.city_list);


        cityDataList = new ArrayList<>();

        cityArrayAdapter = new CityArrayAdapter(this, cityDataList);
        cityList.setAdapter(cityArrayAdapter);

        addCityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String cityName = addCityEditText.getText().toString();
                String province = addProvinceEditText.getText().toString();
                City city = new City(cityName, province);
                addNewCity(city);
            }
        });

        cityList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                City clickedCity = cityDataList.get(i);
                deleteCityButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        delete(clickedCity);
                    }
                });
            }
        });



        citiesRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot querySnapshots,
                                @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.e("Firestore", error.toString());
                    return;
                }
                if (querySnapshots != null) {
                    cityDataList.clear();
                    for (QueryDocumentSnapshot doc: querySnapshots) {
                        String city = doc.getId();
                        String province = doc.getString("Province");
                        Log.d("Firestore", String.format("City(%s, %s) fetched", city,
                                province));
                        cityDataList.add(new City(city, province));
                    }
                    cityArrayAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    /**
     * Adds the initial city objects to the ArrayList
     */
    private void addCitiesInit() {
        String []cities ={"Edmonton", "Vancouver", "Toronto", "Hamilton", "Denver", "Los Angeles"};
        String []provinces = {"AB", "BC", "ON", "ON", "CO", "CA"};
        for(int i=0;i<cities.length;i++){
            cityDataList.add((new City(cities[i], provinces[i])));
        }
    }

    private void addNewCity(City city) {
        // Add the city to the local list
        cityDataList.add(city);
        cityArrayAdapter.notifyDataSetChanged();
        // Add the city to the Firestore collection
        HashMap<String, String> data = new HashMap<>();
        data.put("Province", city.getProvinceName());
        citiesRef.document(city.getCityName()).set(data);
        addCityEditText.setText("");
        addProvinceEditText.setText("");

    }

    private void delete(City city){
        cityDataList.remove(city);
        cityArrayAdapter.notifyDataSetChanged();
        citiesRef.document(city.getCityName()).delete();
    }
}
    }
}

