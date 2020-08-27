package com.suri5.clubmngmt.Schedule;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.suri5.clubmngmt.R;

import java.util.List;

public class GoogleMapActivity extends AppCompatActivity implements OnMapReadyCallback {
    EditText editText_searchPlace;
    GoogleMap mMap;
    Geocoder geocoder;
    Button button_searchPlace,button_selectPlace;
    String place="없음";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_map);
        button_searchPlace=findViewById(R.id.button_searchPlace);
        button_selectPlace=findViewById(R.id.button_selectPlace);
        editText_searchPlace=findViewById(R.id.editText_searchPlace);
        SupportMapFragment mapFragment=(SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.Fragment_map);
        mapFragment.getMapAsync(this);

        button_selectPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.putExtra("place",place);
                setResult(RESULT_OK,intent);
                finish();
            }
        });
    }
    // getMapAsync 호출시 실행될 구글맵 함수
    @Override
    public void onMapReady(final GoogleMap googleMap) {
        mMap=googleMap;
        geocoder=new Geocoder(this);

        //맵 터치 이벤트
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                mMap.clear();  //전에 있던 마커는 초기화한다.
                List<Address> addressList=null;
                MarkerOptions markerOptions=new MarkerOptions();
                //마커 타이틀과 위 경도 받아오기
                markerOptions.title("장소");
                Double latitude=latLng.latitude;
                Double longitude=latLng.longitude;
                try {
                    addressList = geocoder.getFromLocation(latitude, longitude, 2);
                    //주소만 따오기
                    String []splitStr = addressList.get(0).toString().split(",");
                    String address = splitStr[0].substring(splitStr[0].indexOf("\"") + 1,splitStr[0].length() - 2); // 주소
                    markerOptions.snippet(address);
                    markerOptions.position(new LatLng(latitude,longitude));
                    googleMap.addMarker(markerOptions);
                    place=address;
                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),"주소가 올바르지 않습니다.",Toast.LENGTH_SHORT).show();
                }
            }
        });
        //직접 검색 버튼
        button_searchPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMap.clear();
                String str=editText_searchPlace.getText().toString();
                List<Address> addressList=null;
                try{
                    //edittext의 주소를 변환
                    addressList=geocoder.getFromLocationName(str,10);
                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),"장소명을 찾을 수 없습니다.",Toast.LENGTH_SHORT).show();
                }
                String []splitStr = addressList.get(0).toString().split(",");
                String address = splitStr[0].substring(splitStr[0].indexOf("\"") + 1,splitStr[0].length() - 2); // 주소
                String latitude = splitStr[10].substring(splitStr[10].indexOf("=") + 1); // 위도
                String longitude = splitStr[12].substring(splitStr[12].indexOf("=") + 1); // 경도
                System.out.println(latitude);
                System.out.println(longitude);

                // 좌표(위도, 경도) 생성
                LatLng point = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));
                // 마커 생성
                MarkerOptions mOptions2 = new MarkerOptions();
                mOptions2.title("장소");
                mOptions2.snippet(address);
                mOptions2.position(point);
                // 마커 추가
                mMap.addMarker(mOptions2);
                // 해당 좌표로 화면 줌
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(point,15));
                place=address;
            }
        });
        ////////////////////

        //초기 화면
        LatLng init = new LatLng(37, 127);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(init,10));
    }
}