package Service;

import android.util.Log;

import com.example.charlesgao.dynamiclistviewfromserver.MyStudentListViewActivity;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import Entity.Student;

/**
 * Created by CharlesGao on 15-08-16.
 */
public class ShowStudentsListService implements UserService {


    @Override
    public List<Student> getStudents() throws Exception {

        List<Student> studentList = new ArrayList<>();
        HttpClient httpClient = new DefaultHttpClient();
        String uri = "http://192.168.0.16:8080/ServerForDynamicLIstView/getStudent.do";
        HttpGet httpGet = new HttpGet(uri);

        HttpResponse httpResponse = httpClient.execute(httpGet);

        int stateCode = httpResponse.getStatusLine().getStatusCode();
        if (stateCode != HttpStatus.SC_OK){
            Log.d("d","NOT OK");
            throw new ServiceRulesException(MyStudentListViewActivity.MSG_CONNECTION_ERROR);
        }

        String result = EntityUtils.toString(httpResponse.getEntity(), HTTP.UTF_8);
        Log.d("d", result);


        /**
         * *****************************************************************************************
         * JSON Parsing:
         */

        JSONArray jsonArrayIn = new JSONArray(result);
        if (jsonArrayIn != null){
            for (int i = 0; i < jsonArrayIn.length(); i++) {
                JSONObject jsonStudent = jsonArrayIn.getJSONObject(i);
                int id = jsonStudent.getInt("id");
                String name = jsonStudent.getString("name");
                int age = jsonStudent.getInt("age");
                Log.i("d", id+" "+name+" "+age);

                studentList.add(new Student(id,name,age));

            }
        }



//        if (jsonResult.equals("success")) {
//            Log.i("d","REGISTER SUCCESS");
//        } else {
//            String errorMsg = jsonArrayIn.getString("errorMsg");
//            Log.i("d","REGISTER FAILED!!!!!!!!!!");
//            throw new ServiceRulesException(errorMsg);
//        }

        /**
         * *****************************************************************************************
         */
        return studentList;
    }
}