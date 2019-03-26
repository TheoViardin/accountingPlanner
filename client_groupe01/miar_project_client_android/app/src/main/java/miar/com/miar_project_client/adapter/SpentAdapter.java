package miar.com.miar_project_client.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

import miar.com.miar_project_client.R;
import miar.com.miar_project_client.model.Spent;

public class SpentAdapter extends ArrayAdapter<Spent> {

    private TextView label, depense;
    private boolean isCheckout = false;

    public SpentAdapter(@NonNull Context context, List<Spent> spents, boolean isCheckout) {
        super(context, 0, spents);
        this.isCheckout = isCheckout;
    }

	@Override
	public int getViewTypeCount()
	{
		return getCount();
	}

	@Override
	public int getItemViewType( int position )
	{
		return position;
	}

	@NonNull
	@Override
	public View getView( int position, @Nullable View convertView, @NonNull ViewGroup parent )
	{
		final Spent spent = getItem( position );

		if( convertView == null )
		{
			convertView = LayoutInflater.from( getContext() ).inflate( R.layout.activity_spent_adapter, parent, false );
		}

		if( spent != null )
		{
			label = convertView.findViewById( R.id.label );
			label.setText( spent.label );

            depense = convertView.findViewById(R.id.depense);
            depense.setText(String.format("%.2f", spent.amount));
            CheckBox checkBox = convertView.findViewById(R.id.checkbox);

			if( isCheckout )
			{
				checkBox.setChecked( spent.isChecked );
			}
			else
			{
				checkBox.setVisibility( View.GONE );
			}
        }

		return convertView;
	}
}
