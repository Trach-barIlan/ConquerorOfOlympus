package trach.yoni.olympiangods.aux;

import android.view.View;

import trach.yoni.olympiangods.aux.Methods;

public class pressMeButtonOnClickListener implements View.OnClickListener {
    @Override
    public void onClick(View view) {
        Methods.makeToast(view.getContext(),"You pressed me!");
    }
}
