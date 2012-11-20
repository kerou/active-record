package ru.phplego.core.db;

import android.content.Context;
import android.database.DataSetObserver;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import ru.phplego.core.Application;
import ru.phplego.core.debug.Log;

import java.util.Observer;

/**
 * Created with IntelliJ IDEA by Oleg Dubrov
 * User: Oleg
 * Date: 19.11.12
 * Time: 22:35
 */
abstract public class ActiveRecordViewHolder<ACTIVE_OBJECT_CLASS extends ActiveRecord>{
    private LinearLayout mView;
    private ACTIVE_OBJECT_CLASS mActiveRecord;
    private DataSetObserver mObserver = new DataSetObserver() {
        @Override
        public void onChanged() {
            // Обязятельно перенаправляем в UI поток
            new Handler(Application.getContext().getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    // Просто перезаполняем элементы отображения
                    fillViews();
                }
            });
        }
    };

    public ActiveRecordViewHolder(int res_layout){
        if(Application.getContext() == null) throw new Error("Application context does not set");
        LayoutInflater inflater = (LayoutInflater) Application.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mView = (LinearLayout) inflater.inflate(res_layout, null, false);
        mView.setTag(this);
    }

    public LinearLayout getView(){
        return mView;
    }

    public ACTIVE_OBJECT_CLASS getActiveRecord(){
        return mActiveRecord;
    }

    public View findViewById(int id){
        return mView.findViewById(id);
    }

    public void setActiveRecord(ACTIVE_OBJECT_CLASS activeRecord){
        // Удаляем обзервер у старого ActiveRecorda
        if(mActiveRecord != null) mActiveRecord.unregisterObserver(mObserver);

        // Устанавливаем обзервер на новый ActiveRecord
        mActiveRecord = activeRecord;
        mActiveRecord.registerObserver(mObserver);
        fillViews();
    };

    /**
     * Здесь реализуется перенос данных из ActiveRecord в соотвествующие элементы отображения
     */
    abstract public void fillViews();
}
