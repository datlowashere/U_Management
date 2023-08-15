package edu.fpt.lab4.adapters;
import static java.security.AccessController.getContext;

import android.content.Context;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.divider.MaterialDivider;
import com.squareup.picasso.Picasso;

import java.util.List;

import edu.fpt.lab4.R;
import edu.fpt.lab4.models.MyEmployee;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
public class MyEmployeeAdapter extends BaseAdapter {

    private Context context;
    private List<MyEmployee> employeeList;

    public MyEmployeeAdapter(Context context, List<MyEmployee> employeeList) {
        this.context = context;
        this.employeeList = employeeList;
    }

    @Override
    public int getCount() {
        return employeeList.size();
    }

    @Override
    public Object getItem(int position) {
        return employeeList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.layout_emp, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.lnContain=convertView.findViewById(R.id.lnContain);
            viewHolder.lnMore=convertView.findViewById(R.id.lnMore);
            viewHolder.dividerLine=convertView.findViewById(R.id.dividerLine);
            viewHolder.tvName = convertView.findViewById(R.id.tvNameMain);
            viewHolder.tvEmail=convertView.findViewById(R.id.tvEmailMain);
            viewHolder.tvAddress=convertView.findViewById(R.id.tvAddressMain);
            viewHolder.tvPhone=convertView.findViewById(R.id.tvPhoneMain);
            viewHolder.tvRole=convertView.findViewById(R.id.tvRoleMain);
            viewHolder.tvGender=convertView.findViewById(R.id.tvGenderMain);
            viewHolder.img=convertView.findViewById(R.id.imgEmpMain);
            viewHolder.imgBtn=convertView.findViewById(R.id.imgMore);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        MyEmployee employee = employeeList.get(position);
        viewHolder.tvName.setText(employee.getName());
        viewHolder.tvEmail.setText(employee.getEmail());
        viewHolder.tvPhone.setText(employee.getPhone());
        viewHolder.tvAddress.setText(employee.getAddress());
        viewHolder.tvRole.setText(employee.getRole());
        viewHolder.tvGender.setText(employee.getGender());


        Animation up = AnimationUtils.loadAnimation(context, R.anim.slide_up);
        Animation down = AnimationUtils.loadAnimation(context, R.anim.slide_down);


        viewHolder.imgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(viewHolder.dividerLine.getVisibility()==View.VISIBLE && viewHolder.lnMore.getVisibility()==View.VISIBLE){
                    viewHolder.dividerLine.setVisibility(View.GONE);
                    viewHolder.lnMore.setVisibility(View.GONE);
                    viewHolder.imgBtn.setRotation(0);
                }else{
                    viewHolder.imgBtn.setRotation(90);
                    viewHolder.dividerLine.setVisibility(View.VISIBLE);
                    viewHolder.lnMore.setVisibility(View.VISIBLE);
                }
            }
        });




        String base64Image = employee.getImage();
        if (base64Image==null) {
            viewHolder.img.setImageResource(R.drawable.poly);
        } else {
            byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
            Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            viewHolder.img.setImageBitmap(decodedBitmap);
        }

        return convertView;
    }

    static class ViewHolder {
        LinearLayout lnMore,lnContain;
        MaterialDivider dividerLine;

        TextView tvName,tvEmail,tvPhone,tvAddress,tvRole,tvGender;
        ImageView img,imgBtn;
    }
}