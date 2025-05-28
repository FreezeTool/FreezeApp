package android.os;

import android.content.Context;
import android.util.proto.ProtoOutputStream;

import java.util.ArrayList;
import java.util.List;

public class WorkSource {

    /**
     * Create an empty work source.
     */
    public WorkSource() {
        throw new RuntimeException();
    }

    /**
     * Create a new WorkSource that is a copy of an existing one.
     * If <var>orig</var> is null, an empty WorkSource is created.
     */
    public WorkSource(WorkSource orig) {
        throw new RuntimeException();
    }


    public WorkSource(int uid) {
        throw new RuntimeException();
    }


    public WorkSource(int uid,  String packageName) {
        throw new RuntimeException();
    }

    WorkSource(Parcel in) {
        throw new RuntimeException();
    }

    public static boolean isChainedBatteryAttributionEnabled(Context context) {
        throw new RuntimeException();
    }


    public int size() {
        throw new RuntimeException();
    }

    public int get(int index) {
        throw new RuntimeException();
    }

    public int getUid(int index) {
        throw new RuntimeException();
    }


    public int getAttributionUid() {
        throw new RuntimeException();
    }

    public String getName(int index) {
        throw new RuntimeException();
    }

    public String getPackageName(int index) {
        throw new RuntimeException();
    }


    /**
     * Clear this WorkSource to be empty.
     */
    public void clear() {
        throw new RuntimeException();
    }

    public boolean diff(WorkSource other) {
        throw new RuntimeException();
    }

    public void set(WorkSource other) {
        throw new RuntimeException();
    }


    public void set(int uid) {
        throw new RuntimeException();
    }


    public void set(int uid, String name) {
        throw new RuntimeException();
    }

    public WorkSource[] setReturningDiffs(WorkSource other) {
        throw new RuntimeException();
    }

    public boolean add(WorkSource other) {
        throw new RuntimeException();
    }

    public WorkSource withoutNames() {
        throw new RuntimeException();
    }

    public WorkSource addReturningNewbs(WorkSource other) {
        throw new RuntimeException();
    }

    public boolean add(int uid) {
        throw new RuntimeException();
    }


    public boolean add(int uid, String name) {
        throw new RuntimeException();
    }

    public boolean remove(WorkSource other) {
        throw new RuntimeException();
    }

    public WorkChain createWorkChain() {
        throw new RuntimeException();
    }

    public boolean isEmpty() {
        throw new RuntimeException();
    }

    public List<WorkChain> getWorkChains() {
        throw new RuntimeException();
    }

    public void transferWorkChains(WorkSource other) {
        throw new RuntimeException();
    }

    public static final class WorkChain  {



        public int getAttributionUid() {
            throw new RuntimeException();
        }

        public String getAttributionTag() {
            throw new RuntimeException();
        }

        public int[] getUids() {
            throw new RuntimeException();
        }

        public String[] getTags() {
            throw new RuntimeException();
        }

        public int getSize() {
            throw new RuntimeException();
        }
    }

    public static ArrayList<WorkChain>[] diffChains(WorkSource oldWs, WorkSource newWs) {
        throw new RuntimeException();
    }


    public void dumpDebug(ProtoOutputStream proto, long fieldId) {
        throw new RuntimeException();
    }
}
