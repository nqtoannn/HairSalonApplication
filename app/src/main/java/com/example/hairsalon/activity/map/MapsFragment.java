//package com.example.hairsalon.activity.map;
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.fragment.app.Fragment;
//
//import android.content.Context;
//import android.content.SharedPreferences;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ArrayAdapter;
//import android.widget.Toast;
//
//import com.example.hairsalon.R;
//import com.example.hairsalon.api.ApiService;
//import com.example.hairsalon.model.ResponseData;
//import com.google.android.gms.maps.CameraUpdateFactory;
//import com.google.android.gms.maps.GoogleMap;
//import com.google.android.gms.maps.OnMapReadyCallback;
//import com.google.android.gms.maps.SupportMapFragment;
//import com.google.android.gms.maps.model.BitmapDescriptorFactory;
//import com.google.android.gms.maps.model.LatLng;
//import com.google.android.gms.maps.model.Marker;
//import com.google.android.gms.maps.model.MarkerOptions;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import retrofit2.Call;
//import retrofit2.Callback;
//
//public class MapsFragment extends Fragment implements GoogleMap.OnMarkerClickListener {
//
//    private GoogleMap googleMap;
//    private HashMap<Marker, Integer> markerSalonIdMap = new HashMap<>();
//    List<Map<String, Object>> salonList;
//    String[] salonIds;
//    String[] salonNames, salonAddresses;
//    private Marker lastSelectedMarker;
//
//    private OnMapReadyCallback callback = new OnMapReadyCallback() {
//        @Override
//        public void onMapReady(GoogleMap googleMap) {
//            MapsFragment.this.googleMap = googleMap;
//            ApiService.apiService.getAllSalons().enqueue(new Callback<ResponseData>() {
//                @Override
//                public void onResponse(Call<ResponseData> call, retrofit2.Response<ResponseData> response) {
//                    if (response.isSuccessful()) {
//                        ResponseData responseData = response.body();
//                        if (responseData != null && responseData.getStatus().equals("OK")) {
//                            salonList = responseData.getData();
//                            salonIds = new String[salonList.size()];
//                            salonNames = new String[salonList.size()];
//                            salonAddresses = new String[salonList.size()];
//                            int index = 0;
//                            for (Map<String, Object> salon : salonList) {
//                                String id = ((String) salon.get("id"));
//                                salonIds[index] = id;
//                                String salonName = (String) salon.get("name");
//                                salonNames[index] = salonName;
//                                String salonAddress = (String) salon.get("address");
//                                salonAddresses[index] = salonAddress;
//                                index++;
//                            }
//                            LatLng salon1 = new LatLng(10.844541, 106.788881);
//                            LatLng salon2 = new LatLng(10.845716, 106.778963);
//                            LatLng salon3 = new LatLng(10.849752, 106.771615);
//                            Marker marker1 = googleMap.addMarker(new MarkerOptions().position(salon1).title(salonNames[0]).snippet(salonAddresses[0]));
//                            Marker marker2 = googleMap.addMarker(new MarkerOptions().position(salon2).title(salonNames[1]).snippet(salonAddresses[1]));
//                            Marker marker3 = googleMap.addMarker(new MarkerOptions().position(salon3).title(salonNames[2]).snippet(salonAddresses[2]));
//                            markerSalonIdMap.put(marker1, 0);
//                            markerSalonIdMap.put(marker2, 1);
//                            markerSalonIdMap.put(marker3, 2);
//                            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(salon1, 12));
//                            googleMap.setOnMarkerClickListener(MapsFragment.this);
//                        } else {
//                            Log.e("Error", "No salon data found in response");
//                        }
//                    } else {
//                        Log.e("Error", "API call failed with error code: " + response.code());
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<ResponseData> call, Throwable t) {
//                    Log.e("Error", "API call failed: " + t.getMessage());
//                }
//            });
//        }
//    };
//
//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater,
//                             @Nullable ViewGroup container,
//                             @Nullable Bundle savedInstanceState) {
//        return inflater.inflate(R.layout.fragment_maps, container, false);
//    }
//
//    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//        SupportMapFragment mapFragment =
//                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
//        if (mapFragment != null) {
//            mapFragment.getMapAsync(callback);
//        }
//    }
//
//    @Override
//    public boolean onMarkerClick(@NonNull Marker marker) {
//        int salonId = markerSalonIdMap.get(marker);
//        if (lastSelectedMarker != null) {
//            // Thay đổi kích thước của Marker trước đó (nếu cần)
//            lastSelectedMarker.setIcon(BitmapDescriptorFactory.defaultMarker());
//        }
//        // Lưu trữ Marker hiện tại được chọn
//        lastSelectedMarker = marker;
//        marker.showInfoWindow();
//
//        // Thay đổi kích thước của Marker hiện tại
//        marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
//        if (salonId != 0) {
//            SharedPreferences prefs = requireContext().getSharedPreferences("SalonId", Context.MODE_PRIVATE);
//            SharedPreferences.Editor editor = prefs.edit();
//            editor.putString("SalonId", salonIds[salonId]);
//            editor.apply();
//            Toast.makeText(getContext(), "Bạn đang chọn chi nhánh số: " + salonId, Toast.LENGTH_SHORT).show();
//        }
//        return false;
//    }
//}
