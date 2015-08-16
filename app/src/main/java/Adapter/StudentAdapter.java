package Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.charlesgao.dynamiclistviewfromserver.R;

import java.util.List;

import Entity.Student;

/**
 * Created by CharlesGao on 15-08-16.
 */
public class StudentAdapter extends ArrayAdapter<Student>{


    private LayoutInflater layoutInflater;
    private int resource;

    /**
     * Constructor
     *
     * @param context  The current context.
     * @param resource The resource ID for a layout file containing a TextView to use when
     *                 instantiating views.
     * @param objects  The objects to represent in the ListView.
     */
    public StudentAdapter(Context context, int resource, List<Student> objects) {
        super(context, resource, objects);
        layoutInflater = LayoutInflater.from(context);
        this.resource = resource;
    }

    /**
     * {@inheritDoc}
     *
     * @param position
     * @param convertView the view that you want to convert
     * @param parent
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LinearLayout view;
        if (convertView == null){
            view = (LinearLayout)layoutInflater.inflate(resource, null);
        } else {
            view = (LinearLayout)convertView;
        }

        /**
         * BINDING
         */
        // Get data and bind to a Item (each student will have a position **student <==> item**)
        // One student one item *IMPORTANT*
        Student student = getItem(position);

        /**
         * SET VALUES
         */
        // Get Item's TextView and Set Values
        TextView id = (TextView)view.findViewById(R.id.id_each_student_id);
        TextView name = (TextView)view.findViewById(R.id.id_each_student_string);
        TextView age = (TextView)view.findViewById(R.id.id_each_student_age);

        name.setText(student.getName());
        age.setText(String.valueOf(student.getAge()));
        id.setText(String.valueOf(student.getId()));

        return view;
    }
}
