// Copyright (C) GridGain Systems, Inc. Licensed under GPLv3, http://www.gnu.org/licenses/gpl.html

/*  _________        _____ __________________        _____
*  __  ____/___________(_)______  /__  ____/______ ____(_)_______
*  _  / __  __  ___/__  / _  __  / _  / __  _  __ `/__  / __  __ \
*  / /_/ /  _  /    _  /  / /_/ /  / /_/ /  / /_/ / _  /  _  / / /
*  \____/   /_/     /_/   \_,__/   \____/   \__,_/  /_/   /_/ /_/
*/

package org.gridgain.grid.kernal.processors.cache;

import org.gridgain.grid.*;
import org.gridgain.grid.lang.*;
import org.gridgain.grid.lang.utils.*;
import org.gridgain.grid.typedef.*;
import org.gridgain.grid.typedef.internal.*;
import org.gridgain.grid.util.tostring.*;

import java.io.*;
import java.util.*;

/**
 * DGC response.
 *
 * @author 2005-2011 Copyright (C) GridGain Systems, Inc.
 * @version 3.1.1c.05062011
 */
class GridCacheDgcResponse<K, V> extends GridCacheMessage<K, V> implements GridCacheDeployable {
    /** */
    @GridToStringInclude
    private Map<K, Collection<GridTuple2<GridCacheVersion, Boolean>>> map =
        new HashMap<K, Collection<GridTuple2<GridCacheVersion, Boolean>>>();

    /** */
    @GridToStringExclude
    private byte[] mapBytes;

    /**
     * Constructor.
     */
    public GridCacheDgcResponse() {
        // No-op.
    }

    /** {@inheritDoc} */
    @Override public void p2pMarshal(GridCacheContext<K, V> ctx) throws GridException {
        super.p2pMarshal(ctx);

        if (map != null) {
            for (K key : map.keySet())
                prepareObject(key, ctx);

            mapBytes = CU.marshal(ctx, map).getEntireArray();
        }
    }

    /** {@inheritDoc} */
    @Override public void p2pUnmarshal(GridCacheContext<K, V> ctx, ClassLoader ldr) throws GridException {
        super.p2pUnmarshal(ctx, ldr);

        if (mapBytes != null)
            map = U.unmarshal(ctx.marshaller(), new GridByteArrayList(mapBytes), ldr);
    }

    /**
     * Add information about key, tx result and version to response.
     *
     * @param key Key.
     * @param rolledback {@code True} if tx has been rolled back on this node.
     * @param ver Version.
     */
    @SuppressWarnings({"MismatchedQueryAndUpdateOfCollection"})
    void addCandidate(K key, GridCacheVersion ver, boolean rolledback) {
        Collection<GridTuple2<GridCacheVersion, Boolean>> col = F.addIfAbsent(map, key,
            new ArrayList<GridTuple2<GridCacheVersion, Boolean>>());

        assert col != null;

        col.add(F.t(ver, rolledback));
    }

    /**
     * @return Candidates map.
     */
    Map<K, Collection<GridTuple2<GridCacheVersion, Boolean>>> candidatesMap() {
        return Collections.unmodifiableMap(map);
    }

    /** {@inheritDoc} */
    @Override public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        super.readExternal(in);

        mapBytes = U.readByteArray(in);
    }

    /** {@inheritDoc} */
    @Override public void writeExternal(ObjectOutput out) throws IOException {
        super.writeExternal(out);

        U.writeByteArray(out, mapBytes);
    }

    /** {@inheritDoc} */
    @Override public String toString() {
        return S.toString(GridCacheDgcResponse.class, this);
    }
}
