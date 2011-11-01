// Copyright (C) GridGain Systems, Inc. Licensed under GPLv3, http://www.gnu.org/licenses/gpl.html

/*  _________        _____ __________________        _____
 *  __  ____/___________(_)______  /__  ____/______ ____(_)_______
 *  _  / __  __  ___/__  / _  __  / _  / __  _  __ `/__  / __  __ \
 *  / /_/ /  _  /    _  /  / /_/ /  / /_/ /  / /_/ / _  /  _  / / /
 *  \____/   /_/     /_/   \_,__/   \____/   \__,_/  /_/   /_/ /_/
 */

package org.gridgain.grid.kernal.processors.cache.distributed.replicated.preloader;

import org.gridgain.grid.typedef.internal.*;

/**
 * Assignment.
 */
class GridReplicatedPreloadAssignment {
    /** */
    private final GridReplicatedPreloadSession ses;

    /** */
    private final int part;

    /** */
    private final int mod;

    /** */
    private final int cnt;

    /**
     * @param ses Session.
     * @param part Partition.
     * @param mod  Mod.
     * @param cnt  Node count.
     */
    GridReplicatedPreloadAssignment(GridReplicatedPreloadSession ses, int part, int mod, int cnt) {
        assert ses != null;

        this.ses = ses;
        this.part = part;
        this.mod = mod;
        this.cnt = cnt;
    }

    /**
     * @return Session.
     */
    GridReplicatedPreloadSession session() {
        return ses;
    }

    /**
     * @return Partition.
     */
    int partition() {
        return part;
    }

    /**
     * @return Mod.
     */
    int mod() {
        return mod;
    }

    /**
     * @return Node count.
     */
    int nodeCount() {
        return cnt;
    }

    /** {@inheritDoc} */
    @Override public String toString() {
        return S.toString(GridReplicatedPreloadAssignment.class, this);
    }
}