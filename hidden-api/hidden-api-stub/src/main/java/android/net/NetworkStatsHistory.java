package android.net;

import static android.net.NetworkStatsHistory.DataStreamUtils.writeVarLongArray;
import static android.net.NetworkStatsHistory.Entry.UNKNOWN;
import static android.net.NetworkStatsHistory.ParcelUtils.readLongArray;
import static android.net.NetworkStatsHistory.ParcelUtils.writeLongArray;
import static android.text.format.DateUtils.SECOND_IN_MILLIS;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ProtocolException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.TreeMap;

/**
 * Collection of historical network statistics, recorded into equally-sized
 * "buckets" in time. Internally it stores data in {@code long} series for more
 * efficient persistence.
 * <p>
 * Each bucket is defined by a {@link #bucketStart} timestamp, and lasts for
 * {@link #bucketDuration}. Internally assumes that {@link #bucketStart} is
 * sorted at all times.
 *
 * @hide
 */
public final class NetworkStatsHistory {
    private static final int VERSION_INIT = 1;
    private static final int VERSION_ADD_PACKETS = 2;
    private static final int VERSION_ADD_ACTIVE = 3;

    /** @hide */
    public static final int FIELD_ACTIVE_TIME = 0x01;
    /** @hide */
    public static final int FIELD_RX_BYTES = 0x02;
    /** @hide */
    public static final int FIELD_RX_PACKETS = 0x04;
    /** @hide */
    public static final int FIELD_TX_BYTES = 0x08;
    /** @hide */
    public static final int FIELD_TX_PACKETS = 0x10;
    /** @hide */
    public static final int FIELD_OPERATIONS = 0x20;
    /** @hide */
    public static final int FIELD_ALL = 0xFFFFFFFF;

    private long bucketDuration;
    private int bucketCount;
    private long[] bucketStart;
    private long[] activeTime;
    private long[] rxBytes;
    private long[] rxPackets;
    private long[] txBytes;
    private long[] txPackets;
    private long[] operations;
    private long totalBytes;

    /** @hide */
    public NetworkStatsHistory(long bucketDuration, long[] bucketStart, long[] activeTime,
                               long[] rxBytes, long[] rxPackets, long[] txBytes, long[] txPackets,
                               long[] operations, int bucketCount, long totalBytes) {
        this.bucketDuration = bucketDuration;
        this.bucketStart = bucketStart;
        this.activeTime = activeTime;
        this.rxBytes = rxBytes;
        this.rxPackets = rxPackets;
        this.txBytes = txBytes;
        this.txPackets = txPackets;
        this.operations = operations;
        this.bucketCount = bucketCount;
        this.totalBytes = totalBytes;
    }

    /**
     * An instance to represent a single record in a {@link NetworkStatsHistory} object.
     */
    public static final class Entry {
        /** @hide */
        public static final long UNKNOWN = -1;


        public long bucketDuration;
        /** @hide */
        public long bucketStart;
        /** @hide */
        public long activeTime;
        /** @hide */
        
        public long rxBytes;
        /** @hide */
        public long rxPackets;
        /** @hide */
        
        public long txBytes;
        /** @hide */
        public long txPackets;
        /** @hide */
        public long operations;
        /** @hide */
        Entry() {}

        /**
         * Construct a {@link Entry} instance to represent a single record in a
         * {@link NetworkStatsHistory} object.
         *
         * @param bucketStart Start of period for this {@link Entry}, in milliseconds since the
         *                    Unix epoch, see {@link java.lang.System#currentTimeMillis}.
         * @param activeTime Active time for this {@link Entry}, in milliseconds.
         * @param rxBytes Number of bytes received for this {@link Entry}. Statistics should
         *                represent the contents of IP packets, including IP headers.
         * @param rxPackets Number of packets received for this {@link Entry}. Statistics should
         *                  represent the contents of IP packets, including IP headers.
         * @param txBytes Number of bytes transmitted for this {@link Entry}. Statistics should
         *                represent the contents of IP packets, including IP headers.
         * @param txPackets Number of bytes transmitted for this {@link Entry}. Statistics should
         *                  represent the contents of IP packets, including IP headers.
         * @param operations count of network operations performed for this {@link Entry}. This can
         *                   be used to derive bytes-per-operation.
         */
        public Entry(long bucketStart, long activeTime, long rxBytes,
                     long rxPackets, long txBytes, long txPackets, long operations) {
            this.bucketStart = bucketStart;
            this.activeTime = activeTime;
            this.rxBytes = rxBytes;
            this.rxPackets = rxPackets;
            this.txBytes = txBytes;
            this.txPackets = txPackets;
            this.operations = operations;
        }

        /**
         * Get start timestamp of the bucket's time interval, in milliseconds since the Unix epoch.
         */
        public long getBucketStart() {
            return bucketStart;
        }

        /**
         * Get active time of the bucket's time interval, in milliseconds.
         */
        public long getActiveTime() {
            return activeTime;
        }

        /** Get number of bytes received for this {@link Entry}. */
        public long getRxBytes() {
            return rxBytes;
        }

        /** Get number of packets received for this {@link Entry}. */
        public long getRxPackets() {
            return rxPackets;
        }

        /** Get number of bytes transmitted for this {@link Entry}. */
        public long getTxBytes() {
            return txBytes;
        }

        /** Get number of packets transmitted for this {@link Entry}. */
        public long getTxPackets() {
            return txPackets;
        }

        /** Get count of network operations performed for this {@link Entry}. */
        public long getOperations() {
            return operations;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o.getClass() != getClass()) return false;
            Entry entry = (Entry) o;
            return bucketStart == entry.bucketStart
                    && activeTime == entry.activeTime && rxBytes == entry.rxBytes
                    && rxPackets == entry.rxPackets && txBytes == entry.txBytes
                    && txPackets == entry.txPackets && operations == entry.operations;
        }

        @Override
        public int hashCode() {
            return (int) (bucketStart * 2
                    + activeTime * 3
                    + rxBytes * 5
                    + rxPackets * 7
                    + txBytes * 11
                    + txPackets * 13
                    + operations * 17);
        }

        @Override
        public String toString() {
            return "Entry{"
                    + "bucketStart=" + bucketStart
                    + ", activeTime=" + activeTime
                    + ", rxBytes=" + rxBytes
                    + ", rxPackets=" + rxPackets
                    + ", txBytes=" + txBytes
                    + ", txPackets=" + txPackets
                    + ", operations=" + operations
                    + "}";
        }

        /**
         * Add the given {@link Entry} with this instance and return a new {@link Entry}
         * instance as the result.
         *
         * @hide
         */
        
        public Entry plus( Entry another, long bucketDuration) {
            if (this.bucketStart != another.bucketStart) {
                throw new IllegalArgumentException("bucketStart " + this.bucketStart
                        + " is not equal to " + another.bucketStart);
            }
            return new Entry(this.bucketStart,
                    // Active time should not go over bucket duration.
                    Math.min(this.activeTime + another.activeTime, bucketDuration),
                    this.rxBytes + another.rxBytes,
                    this.rxPackets + another.rxPackets,
                    this.txBytes + another.txBytes,
                    this.txPackets + another.txPackets,
                    this.operations + another.operations);
        }
    }

    /** @hide */
    
    public NetworkStatsHistory(long bucketDuration) {
        this(bucketDuration, 10, FIELD_ALL);
    }

    /** @hide */
    public NetworkStatsHistory(long bucketDuration, int initialSize) {
        this(bucketDuration, initialSize, FIELD_ALL);
    }

    /** @hide */
    public NetworkStatsHistory(long bucketDuration, int initialSize, int fields) {
        this.bucketDuration = bucketDuration;
        bucketStart = new long[initialSize];
        if ((fields & FIELD_ACTIVE_TIME) != 0) activeTime = new long[initialSize];
        if ((fields & FIELD_RX_BYTES) != 0) rxBytes = new long[initialSize];
        if ((fields & FIELD_RX_PACKETS) != 0) rxPackets = new long[initialSize];
        if ((fields & FIELD_TX_BYTES) != 0) txBytes = new long[initialSize];
        if ((fields & FIELD_TX_PACKETS) != 0) txPackets = new long[initialSize];
        if ((fields & FIELD_OPERATIONS) != 0) operations = new long[initialSize];
        bucketCount = 0;
        totalBytes = 0;
    }

    /** @hide */
    public NetworkStatsHistory(NetworkStatsHistory existing, long bucketDuration) {
        this(bucketDuration, existing.estimateResizeBuckets(bucketDuration));
        recordEntireHistory(existing);
    }

    public NetworkStatsHistory(Parcel in) {
        bucketDuration = in.readLong();
        bucketStart = readLongArray(in);
        activeTime = readLongArray(in);
        rxBytes = readLongArray(in);
        rxPackets = readLongArray(in);
        txBytes = readLongArray(in);
        txPackets = readLongArray(in);
        operations = readLongArray(in);
        bucketCount = bucketStart.length;
        totalBytes = in.readLong();
    }
    /** @hide */
    public NetworkStatsHistory(DataInput in) throws IOException {
        throw new RuntimeException();
    }

    /** @hide */
    public void writeToStream(DataOutput out) throws IOException {
        out.writeInt(VERSION_ADD_ACTIVE);
        out.writeLong(bucketDuration);
        writeVarLongArray(out, bucketStart, bucketCount);
        writeVarLongArray(out, activeTime, bucketCount);
        writeVarLongArray(out, rxBytes, bucketCount);
        writeVarLongArray(out, rxPackets, bucketCount);
        writeVarLongArray(out, txBytes, bucketCount);
        writeVarLongArray(out, txPackets, bucketCount);
        writeVarLongArray(out, operations, bucketCount);
    }


    public int size() {
        return bucketCount;
    }

    /** @hide */
    public long getBucketDuration() {
        return bucketDuration;
    }

    /** @hide */
    
    public long getStart() {
        if (bucketCount > 0) {
            return bucketStart[0];
        } else {
            return Long.MAX_VALUE;
        }
    }

    /** @hide */
    
    public long getEnd() {
        if (bucketCount > 0) {
            return bucketStart[bucketCount - 1] + bucketDuration;
        } else {
            return Long.MIN_VALUE;
        }
    }

    /**
     * Return total bytes represented by this history.
     * @hide
     */
    public long getTotalBytes() {
        return totalBytes;
    }

    /**
     * Return index of bucket that contains or is immediately before the
     * requested time.
     * @hide
     */

    public int getIndexBefore(long time) {
        throw new RuntimeException();
    }

    /**
     * Return index of bucket that contains or is immediately after the
     * requested time.
     * @hide
     */
    public int getIndexAfter(long time) {
        throw new RuntimeException();
    }

    /**
     * Return specific stats entry.
     * @hide
     */
    public Entry getValues(int i, Entry recycle) {
        final Entry entry = recycle != null ? recycle : new Entry();
        entry.bucketStart = bucketStart[i];
        entry.bucketDuration = bucketDuration;
        entry.activeTime = getLong(activeTime, i, UNKNOWN);
        entry.rxBytes = getLong(rxBytes, i, UNKNOWN);
        entry.rxPackets = getLong(rxPackets, i, UNKNOWN);
        entry.txBytes = getLong(txBytes, i, UNKNOWN);
        entry.txPackets = getLong(txPackets, i, UNKNOWN);
        entry.operations = getLong(operations, i, UNKNOWN);
        return entry;
    }

    /**
     * Get List of {@link Entry} of the {@link NetworkStatsHistory} instance.
     *
     * @return
     */
    
    public List<Entry> getEntries() {
        // TODO: Return a wrapper that uses this list instead, to prevent the returned result
        //  from being changed.
        final ArrayList<Entry> ret = new ArrayList<>(size());
        for (int i = 0; i < size(); i++) {
            ret.add(getValues(i, null /* recycle */));
        }
        return ret;
    }

    /** @hide */
    public void setValues(int i, Entry entry) {
        // Unwind old values
        if (rxBytes != null) totalBytes -= rxBytes[i];
        if (txBytes != null) totalBytes -= txBytes[i];

        bucketStart[i] = entry.bucketStart;
        setLong(activeTime, i, entry.activeTime);
        setLong(rxBytes, i, entry.rxBytes);
        setLong(rxPackets, i, entry.rxPackets);
        setLong(txBytes, i, entry.txBytes);
        setLong(txPackets, i, entry.txPackets);
        setLong(operations, i, entry.operations);

        // Apply new values
        if (rxBytes != null) totalBytes += rxBytes[i];
        if (txBytes != null) totalBytes += txBytes[i];
    }

    /**
     * Record that data traffic occurred in the given time range. Will
     * distribute across internal buckets, creating new buckets as needed.
     * @hide
     */
    @Deprecated
    public void recordData(long start, long end, long rxBytes, long txBytes) {
        throw new RuntimeException();
    }

    /**
     * Record that data traffic occurred in the given time range. Will
     * distribute across internal buckets, creating new buckets as needed.
     * @hide
     */
    public void recordData(long start, long end, NetworkStats.Entry entry) {
        throw new RuntimeException();
    }

    /**
     * Record an entire {@link NetworkStatsHistory} into this history. Usually
     * for combining together stats for external reporting.
     * @hide
     */
    
    public void recordEntireHistory(NetworkStatsHistory input) {
        recordHistory(input, Long.MIN_VALUE, Long.MAX_VALUE);
    }

    /**
     * Record given {@link NetworkStatsHistory} into this history, copying only
     * buckets that atomically occur in the inclusive time range. Doesn't
     * interpolate across partial buckets.
     * @hide
     */
    public void recordHistory(NetworkStatsHistory input, long start, long end) {
        throw new RuntimeException();
    }

    /**
     * Ensure that buckets exist for given time range, creating as needed.
     */
    private void ensureBuckets(long start, long end) {
        // normalize incoming range to bucket boundaries
        start -= start % bucketDuration;
        end += (bucketDuration - (end % bucketDuration)) % bucketDuration;

        for (long now = start; now < end; now += bucketDuration) {
            // try finding existing bucket
            final int index = Arrays.binarySearch(bucketStart, 0, bucketCount, now);
            if (index < 0) {
                // bucket missing, create and insert
                insertBucket(~index, now);
            }
        }
    }

    /**
     * Insert new bucket at requested index and starting time.
     */
    private void insertBucket(int index, long start) {
        // create more buckets when needed
        if (bucketCount >= bucketStart.length) {
            final int newLength = Math.max(bucketStart.length, 10) * 3 / 2;
            bucketStart = Arrays.copyOf(bucketStart, newLength);
            if (activeTime != null) activeTime = Arrays.copyOf(activeTime, newLength);
            if (rxBytes != null) rxBytes = Arrays.copyOf(rxBytes, newLength);
            if (rxPackets != null) rxPackets = Arrays.copyOf(rxPackets, newLength);
            if (txBytes != null) txBytes = Arrays.copyOf(txBytes, newLength);
            if (txPackets != null) txPackets = Arrays.copyOf(txPackets, newLength);
            if (operations != null) operations = Arrays.copyOf(operations, newLength);
        }

        // create gap when inserting bucket in middle
        if (index < bucketCount) {
            final int dstPos = index + 1;
            final int length = bucketCount - index;

            System.arraycopy(bucketStart, index, bucketStart, dstPos, length);
            if (activeTime != null) System.arraycopy(activeTime, index, activeTime, dstPos, length);
            if (rxBytes != null) System.arraycopy(rxBytes, index, rxBytes, dstPos, length);
            if (rxPackets != null) System.arraycopy(rxPackets, index, rxPackets, dstPos, length);
            if (txBytes != null) System.arraycopy(txBytes, index, txBytes, dstPos, length);
            if (txPackets != null) System.arraycopy(txPackets, index, txPackets, dstPos, length);
            if (operations != null) System.arraycopy(operations, index, operations, dstPos, length);
        }

        bucketStart[index] = start;
        setLong(activeTime, index, 0L);
        setLong(rxBytes, index, 0L);
        setLong(rxPackets, index, 0L);
        setLong(txBytes, index, 0L);
        setLong(txPackets, index, 0L);
        setLong(operations, index, 0L);
        bucketCount++;
    }

    /**
     * Clear all data stored in this object.
     * @hide
     */
    public void clear() {
        throw new RuntimeException();
    }

    /**
     * Remove buckets that start older than requested cutoff.
     *
     * This method will remove any bucket that contains any data older than the requested
     * cutoff, even if that same bucket includes some data from after the cutoff.
     *
     * @hide
     */
    public void removeBucketsStartingBefore(final long cutoff) {
        throw new RuntimeException();
    }

    /**
     * Return interpolated data usage across the requested range. Interpolates
     * across buckets, so values may be rounded slightly.
     *
     * <p>If the active bucket is not completed yet, it returns the proportional value of it
     * based on its duration and the {@code end} param.
     *
     * @param start - start of the range, timestamp in milliseconds since the epoch.
     * @param end - end of the range, timestamp in milliseconds since the epoch.
     * @param recycle - entry instance for performance, could be null.
     * @hide
     */
    
    public Entry getValues(long start, long end, Entry recycle) {
        return getValues(start, end, Long.MAX_VALUE, recycle);
    }

    /**
     * Return interpolated data usage across the requested range. Interpolates
     * across buckets, so values may be rounded slightly.
     *
     * @param start - start of the range, timestamp in milliseconds since the epoch.
     * @param end - end of the range, timestamp in milliseconds since the epoch.
     * @param now - current timestamp in milliseconds since the epoch (wall clock).
     * @param recycle - entry instance for performance, could be null.
     * @hide
     */
    
    public Entry getValues(long start, long end, long now, Entry recycle) {
        throw new RuntimeException();
    }

    /**
     * @deprecated only for temporary testing
     * @hide
     */
    @Deprecated
    public void generateRandom(long start, long end, long bytes) {
        final Random r = new Random();

        final float fractionRx = r.nextFloat();
        final long rxBytes = (long) (bytes * fractionRx);
        final long txBytes = (long) (bytes * (1 - fractionRx));

        final long rxPackets = rxBytes / 1024;
        final long txPackets = txBytes / 1024;
        final long operations = rxBytes / 2048;

        generateRandom(start, end, rxBytes, rxPackets, txBytes, txPackets, operations, r);
    }

    /**
     * @deprecated only for temporary testing
     * @hide
     */
    @Deprecated
    public void generateRandom(long start, long end, long rxBytes, long rxPackets, long txBytes,
                               long txPackets, long operations, Random r) {

    }

    /** @hide */
    public static long randomLong(Random r, long start, long end) {
        return (long) (start + (r.nextFloat() * (end - start)));
    }

    /**
     * Quickly determine if this history intersects with given window.
     * @hide
     */
    public boolean intersects(long start, long end) {
        final long dataStart = getStart();
        final long dataEnd = getEnd();
        if (start >= dataStart && start <= dataEnd) return true;
        if (end >= dataStart && end <= dataEnd) return true;
        if (dataStart >= start && dataStart <= end) return true;
        if (dataEnd >= start && dataEnd <= end) return true;
        return false;
    }

    /** @hide */
    public void dumpCheckin(PrintWriter pw) {
        pw.print("d,");
        pw.print(bucketDuration / SECOND_IN_MILLIS);
        pw.println();

        for (int i = 0; i < bucketCount; i++) {
            pw.print("b,");
            pw.print(bucketStart[i] / SECOND_IN_MILLIS); pw.print(',');
            if (rxBytes != null) { pw.print(rxBytes[i]); } else { pw.print("*"); } pw.print(',');
            if (rxPackets != null) { pw.print(rxPackets[i]); } else { pw.print("*"); } pw.print(',');
            if (txBytes != null) { pw.print(txBytes[i]); } else { pw.print("*"); } pw.print(',');
            if (txPackets != null) { pw.print(txPackets[i]); } else { pw.print("*"); } pw.print(',');
            if (operations != null) { pw.print(operations[i]); } else { pw.print("*"); }
            pw.println();
        }
    }

    /**
     * Same as "equals", but not actually called equals as this would affect public API behavior.
     * @hide
     */
    
    public boolean isSameAs(NetworkStatsHistory other) {
        return bucketCount == other.bucketCount
                && Arrays.equals(bucketStart, other.bucketStart)
                // Don't check activeTime since it can change on import due to the importer using
                // recordHistory. It's also not exposed by the APIs or present in dumpsys or
                // toString().
                && Arrays.equals(rxBytes, other.rxBytes)
                && Arrays.equals(rxPackets, other.rxPackets)
                && Arrays.equals(txBytes, other.txBytes)
                && Arrays.equals(txPackets, other.txPackets)
                && Arrays.equals(operations, other.operations)
                && totalBytes == other.totalBytes;
    }


    private static long getLong(long[] array, int i, long value) {
        return array != null ? array[i] : value;
    }

    private static void setLong(long[] array, int i, long value) {
        if (array != null) array[i] = value;
    }

    private static void addLong(long[] array, int i, long value) {
        if (array != null) array[i] += value;
    }

    /** @hide */
    public int estimateResizeBuckets(long newBucketDuration) {
        return (int) (size() * getBucketDuration() / newBucketDuration);
    }
    
    public static class DataStreamUtils {
        @Deprecated
        public static long[] readFullLongArray(DataInput in) throws IOException {
            final int size = in.readInt();
            if (size < 0) throw new ProtocolException("negative array size");
            final long[] values = new long[size];
            for (int i = 0; i < values.length; i++) {
                values[i] = in.readLong();
            }
            return values;
        }

        /**
         * Read variable-length {@link Long} using protobuf-style approach.
         */
        public static long readVarLong(DataInput in) throws IOException {
            int shift = 0;
            long result = 0;
            while (shift < 64) {
                byte b = in.readByte();
                result |= (long) (b & 0x7F) << shift;
                if ((b & 0x80) == 0)
                    return result;
                shift += 7;
            }
            throw new ProtocolException("malformed long");
        }

        /**
         * Write variable-length {@link Long} using protobuf-style approach.
         */
        public static void writeVarLong(DataOutput out, long value) throws IOException {
            while (true) {
                if ((value & ~0x7FL) == 0) {
                    out.writeByte((int) value);
                    return;
                } else {
                    out.writeByte(((int) value & 0x7F) | 0x80);
                    value >>>= 7;
                }
            }
        }

        public static long[] readVarLongArray(DataInput in) throws IOException {
            final int size = in.readInt();
            if (size == -1) return null;
            if (size < 0) throw new ProtocolException("negative array size");
            final long[] values = new long[size];
            for (int i = 0; i < values.length; i++) {
                values[i] = readVarLong(in);
            }
            return values;
        }

        public static void writeVarLongArray(DataOutput out, long[] values, int size)
                throws IOException {
            if (values == null) {
                out.writeInt(-1);
                return;
            }
            if (size > values.length) {
                throw new IllegalArgumentException("size larger than length");
            }
            out.writeInt(size);
            for (int i = 0; i < size; i++) {
                writeVarLong(out, values[i]);
            }
        }
    }

    /**
     * Utility methods for interacting with {@link Parcel} structures, mostly
     * dealing with writing partial arrays.
     * @hide
     */
    public static class ParcelUtils {
        public static long[] readLongArray(Parcel in) {
            final int size = in.readInt();
            if (size == -1) return null;
            final long[] values = new long[size];
            for (int i = 0; i < values.length; i++) {
                values[i] = in.readLong();
            }
            return values;
        }

        public static void writeLongArray(Parcel out, long[] values, int size) {
            if (values == null) {
                out.writeInt(-1);
                return;
            }
            if (size > values.length) {
                throw new IllegalArgumentException("size larger than length");
            }
            out.writeInt(size);
            for (int i = 0; i < size; i++) {
                out.writeLong(values[i]);
            }
        }
    }

    /**
     * Builder class for {@link NetworkStatsHistory}.
     */
    public static final class Builder {
        private final TreeMap<Long, Entry> mEntries;
        private final long mBucketDuration;

        /**
         * Creates a new Builder with given bucket duration and initial capacity to construct
         * {@link NetworkStatsHistory} objects.
         *
         * @param bucketDuration Duration of the buckets of the object, in milliseconds.
         * @param initialCapacity Estimated number of records.
         */
        public Builder(long bucketDuration, int initialCapacity) {
            mBucketDuration = bucketDuration;
            // Create a collection that is always sorted and can deduplicate items by the timestamp.
            mEntries = new TreeMap<>();
        }

        /**
         * Add an {@link Entry} into the {@link NetworkStatsHistory} instance. If the timestamp
         * already exists, the given {@link Entry} will be combined into existing entry.
         *
         * @param entry The target {@link Entry} object.
         * @return The builder object.
         */
        
        public Builder addEntry( Entry entry) {
            final Entry existing = mEntries.get(entry.bucketStart);
            if (existing != null) {
                mEntries.put(entry.bucketStart, existing.plus(entry, mBucketDuration));
            } else {
                mEntries.put(entry.bucketStart, entry);
            }
            return this;
        }

        private static long sum( long[] array) {
            long sum = 0L;
            for (long entry : array) {
                sum += entry;
            }
            return sum;
        }

        /**
         * Builds the instance of the {@link NetworkStatsHistory}.
         *
         * @return the built instance of {@link NetworkStatsHistory}.
         */
        
        public NetworkStatsHistory build() {
            int size = mEntries.size();
            final long[] bucketStart = new long[size];
            final long[] activeTime = new long[size];
            final long[] rxBytes = new long[size];
            final long[] rxPackets = new long[size];
            final long[] txBytes = new long[size];
            final long[] txPackets = new long[size];
            final long[] operations = new long[size];

            int i = 0;
            for (Entry entry : mEntries.values()) {
                bucketStart[i] = entry.bucketStart;
                activeTime[i] = entry.activeTime;
                rxBytes[i] = entry.rxBytes;
                rxPackets[i] = entry.rxPackets;
                txBytes[i] = entry.txBytes;
                txPackets[i] = entry.txPackets;
                operations[i] = entry.operations;
                i++;
            }

            return new NetworkStatsHistory(mBucketDuration, bucketStart, activeTime,
                    rxBytes, rxPackets, txBytes, txPackets, operations,
                    size, sum(rxBytes) + sum(txBytes));
        }
    }
}