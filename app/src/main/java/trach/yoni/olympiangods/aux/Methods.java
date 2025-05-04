package trach.yoni.olympiangods.aux;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Space;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import trach.yoni.olympiangods.R;

public class Methods {

    /**
     * creates a toast with the given message
     * @param theContext the context
     * @param message the message
     */
    static public void makeToast(Context theContext, String message) {
        Toast myToast = new Toast(theContext);
        myToast.setText(message);
        myToast.setDuration(Toast.LENGTH_SHORT);
        myToast.show();
    }

    /**
     * creates an alert with the given title and message beneath
     * @param theContext the context
     * @param title title of the alert
     * @param message what the alert will say
     */
    static public void makeAlert(Context theContext, String title, String message) {
        AlertDialog.Builder myAlert = new AlertDialog.Builder(theContext);
        myAlert.setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //does nothing
                    }
                })
                .create()
                .show();
    }

    /**
     * makes an alert with a title and then an image and message beside the image
     * @param theContext
     * @param title
     * @param message
     * @param imageRecourseId Resource id of the character's image
     */
    static public void makeImageAlert(Context theContext, String title, String message, int imageRecourseId) {
        AlertDialog.Builder myAlert = new AlertDialog.Builder(theContext);
        myAlert.setTitle(title)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //does nothing
                    }
                });
        LinearLayout imageLayout = new LinearLayout(theContext);
        imageLayout.setOrientation(LinearLayout.HORIZONTAL);

        ImageView boughtCharacterImage = new ImageView(theContext);
        boughtCharacterImage.setImageResource(imageRecourseId);
        boughtCharacterImage.setLayoutParams(
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT, 1f));

        Space space = new Space(theContext);
        space.setLayoutParams(
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT, 0.5f));

        TextView description = new TextView(theContext);
        description.setText(message);
        description.setLayoutParams(
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT, 1f));

        imageLayout.addView(boughtCharacterImage);
        imageLayout.addView(space);
        imageLayout.addView(description);
        myAlert.setView(imageLayout);
        myAlert.show();
    }

    /**
     * all the possible places to debug with makeDebugToast
     */
    static public enum DebugOption {
        LIFECYCLE,
        HOME_SCREEN,
        GAME_CHARACTER,
        FIGHT_FRAGMENT
    };

    /**
     * list of the places that you want to debug
     */
    final static public DebugOption[] MY_DEBUG_OPTIONS = {DebugOption.GAME_CHARACTER, DebugOption.FIGHT_FRAGMENT};


    /**
     * makes a toast only if the place it is located is in the {@link DebugOption} array
     * @param theOption if this is in the given array of places you want to debug it will make the toast
     * @param theContext the context
     * @param message what the toast will say
     */
    static public void makeDebugToast(DebugOption theOption, Context theContext, String message){
        for(int ii = 0; ii<MY_DEBUG_OPTIONS.length; ii++) {
            if(MY_DEBUG_OPTIONS[ii] == theOption) {
                Methods.makeToast(theContext, message);
                return;
            }
        }
    }
}
