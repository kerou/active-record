package ru.phplego.core;

import android.content.Context;
import ru.phplego.core.debug.Log;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;

/**
 * Created with IntelliJ IDEA.
 * User: Oleg
 * Date: 25.04.12
 * Time: 19:47
 * To change this template use File | Settings | File Templates.
 */
public class EManager {
    public static class Event{
        private Context mContext = null;
    }

    public static abstract class EventListenter<TEventClass extends Event>{
        private Context mContext = null;
        public abstract void onEvent(TEventClass e);
    }

    private class ListenerSet extends LinkedHashSet<EventListenter>{}

    private LinkedHashMap<Class<? extends Event>, ListenerSet> mListeners = new LinkedHashMap<Class<? extends Event>, ListenerSet>();

    public void riseEvent(Event event, Context context){
        event.mContext = context;
        if(!mListeners.containsKey(event.getClass())) return;
        Log.d(" RISE EVENT " + event.getClass().getSimpleName()+" Lesteners count " + mListeners.get(event.getClass()).size());
        for(EventListenter listenter: mListeners.get(event.getClass())){
            if(event.mContext == listenter.mContext) listenter.onEvent(event);
        }
    }

    public void setEventListener(Class<? extends Event> cls, EventListenter<? extends Event> listenter, Context context){
        listenter.mContext = context;
        if(!mListeners.containsKey(cls))
            mListeners.put(cls, new ListenerSet());
        listenter.mContext = null;
        mListeners.get(cls).add(listenter);
    }

}
